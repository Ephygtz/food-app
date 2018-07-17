package com.wrx.quickeats.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.databinding.ActivityCreditCardProceedBinding;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.MyDialog;

import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Dev-PC on 1/22/2018.
 */

public class CreditCard extends Activity implements View.OnClickListener {
    ActivityCreditCardProceedBinding binding;
    String cardNo = "";
    String firstName = "";
    String lastName = "";
    String expiryMonth = "";
    String expiryYear = "";
    String cvv = "";
    String phoneNo = "";
    String currency = "";
    String customerCardBillingAddress = "KE";
    String customerCardBillingPostalCode = "KE";
    String customerCity = "KE";
    String customerState = "KE";
    String customerCountry = "KE";

    private static final String HASH_ALGORITHM = "HmacSHA256";
    private String cbk = "";
    private String cst = "", curr = "", vid = "", live = "", oid = "", inv = "", amount = "", tel = "", eml = "", sid = "";
    private String mySecret = "";
    MyDialog myDialog;
    private String transcationId = "";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
    private String myDataString="";
    private String dataString="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_credit_card_proceed);
        binding.backButton.setOnClickListener(this);
        binding.btnDone.setOnClickListener(this);
        binding.selectCurrency.setOnClickListener(this);

    }

    private boolean validateForm() {
        boolean validate = true;
        cardNo = binding.cardNo.getText().toString().trim();
        firstName = binding.firstName.getText().toString().trim();
        lastName = binding.lastName.getText().toString().trim();
        expiryMonth = binding.expMonth.getText().toString().trim();
        expiryYear = binding.expYear.getText().toString().trim();
        cvv = binding.cvv.getText().toString().trim();
        currency = binding.selectCurrency.getText().toString().trim();
        //phoneNo = binding.phoneNo.getText().toString().trim();
        //customerCardBillingAddress = binding.customerCardBillingAddress.getText().toString().trim();
        //customerCardBillingPostalCode = binding.customerCardBillingPostalCode.getText().toString().trim();
        //customerCity = binding.customerCity.getText().toString().trim();
        //customerState = binding.customerState.getText().toString().trim();
        //customerCountry = binding.customerCountry.getText().toString().trim();

        if (cardNo.length() == 0) {
            Toast.makeText(this, "please enter card no", Toast.LENGTH_LONG).show();
            return false;
        } else if (cardNo.length() < 16) {
            Toast.makeText(this, "please enter 16 digit card no", Toast.LENGTH_LONG).show();
            return false;
        } else if (firstName.length() == 0) {
            Toast.makeText(this, "please enter first name", Toast.LENGTH_LONG).show();
            return false;
        } else if (lastName.length() == 0) {
            Toast.makeText(this, "please enter last name", Toast.LENGTH_LONG).show();
            return false;
        } else if (expiryMonth.length() < 2) {
            Toast.makeText(this, "please enter expiry month", Toast.LENGTH_LONG).show();
            return false;
        } else if (expiryYear.length() < 2) {
            Toast.makeText(this, "please enter expiry year", Toast.LENGTH_LONG).show();
            return false;
        } else if (cvv.length() < 3) {
            Toast.makeText(this, "please enter cvv no", Toast.LENGTH_LONG).show();
            return false;
        } else if (currency.length() == 0) {
            Toast.makeText(this, "Please select currency.", Toast.LENGTH_LONG).show();
            return false;
        } else if (phoneNo.length() == 0) {
            Toast.makeText(this, "please enter phone no", Toast.LENGTH_LONG).show();
            return false;
        } else if (phoneNo.length() < 10) {
            Toast.makeText(this, "please enter 10 digit phone no", Toast.LENGTH_LONG).show();
            return false;
        } else if (customerCardBillingAddress.length() == 0) {
            Toast.makeText(this, "please enter billing address", Toast.LENGTH_LONG).show();
            return false;
        } else if (customerCardBillingPostalCode.length() == 0) {
            Toast.makeText(this, "please enter billing postal code", Toast.LENGTH_LONG).show();
            return false;
        } else if (customerCity.length() == 0) {
            Toast.makeText(this, "please enter city", Toast.LENGTH_LONG).show();
            return false;
        } else if (customerState.length() == 0) {
            Toast.makeText(this, "please enter state", Toast.LENGTH_LONG).show();
            return false;
        } else if (customerCountry.length() == 0) {
            Toast.makeText(this, "please enter country", Toast.LENGTH_LONG).show();
            return false;

        }
        return validate;
    }

    private void showCurrency() {
        final List<String> currenclyList = new ArrayList<>();
        currenclyList.add("KES");
        currenclyList.add("USD");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currenclyList);
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCurrency = currenclyList.get(i);
                binding.selectCurrency.setText(selectedCurrency);
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setAnchorView(binding.selectCurrency);
        listPopupWindow.setAdapter(arrayAdapter);
        listPopupWindow.show();
    }

    @Override
    public void onClick(View view) {

        if (view == binding.selectCurrency) {
            showCurrency();
        } else if (view == binding.btnDone) {
            if (validateForm()) {
                callCreditCard(cardNo,firstName,lastName,expiryMonth,expiryYear,cvv,currency,phoneNo,customerCardBillingAddress,customerCardBillingPostalCode,customerCity,customerState,customerCountry);
            }
        }else if(view==binding.backButton){
            finish();
        }

    }

    private void callCreditCard(String cardNo,String firstName,String lastName,String expiryMonth,String expiryYear,String cvv,String currency,String phoneNo,String customerCardBillingAddress,String customerCardBillingPostalCode,String customerCity,String customerState,String customerCountry) {
        //first we have to generate hash_id
        live = "1";
        oid = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.order_id);
        inv = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.order_id);
        amount = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.AMOUNT);
        //amount = "1";
        tel = phoneNo;
        eml = SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.Email);
        vid = "quickeats";
        curr = currency;
        cst = "1";
        cbk = "callback";

        mySecret = "54dshukic8f87d8dod3d589"; //Replace with your own Secret
//        myDataString = live + "" + oid + "" + inv + "" + amount + "" + tel + "" + eml + "" +
//                       vid + "" + curr + "" + cst + "" + cbk; // Replace with your data
        myDataString=live+oid+inv+amount+tel+eml+vid+curr+cst+cbk;
        try {
            //Log.i("HASH IPAY", "" + hashMac("54dshukic8f87d8dod3d589", "15035031201.08859339243aht@gmail.comquickeatsKES1callback"));
            String hashId = hashMac(mySecret, myDataString);
            Log.w("TAG", "mySecret : " + mySecret);
            Log.w("TAG", "dataString : " + myDataString);
            Log.d("TAG", "result1 : " + hashId);
            paymentCreditCardApi(hashId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashMac(String MySecret, String MyDataString)
            throws SignatureException {

        try {
            Key sk = new SecretKeySpec(MySecret.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(MyDataString.getBytes());
            return toHexString(hmac);
        } catch (NoSuchAlgorithmException e1) {
            // throw an exception or pick a different encryption method
            throw new SignatureException(
                    "error building signature, no such algorithm in device "
                            + HASH_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new SignatureException(
                    "error building signature, invalid key " + HASH_ALGORITHM);
        }
    }

    public static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return sb.toString();
    }

    private void paymentCreditCardApi(String hashId) throws JSONException {

        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClientPaymentMpesa();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<CommonResponse> call = apiInterface.transact(
                live,
                oid,
                inv,
                //amount,
                SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.AMOUNT),
                tel,
                eml,
                vid,
                curr,
                cst,
                "callback",
                hashId);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(CreditCard.this).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1")) {
                        sid = response.body().getmPesaResponse().getSid();
                        try {
/* $sid.$vid.$cardno.$cvv.$month.$year.$cust_address.$cust_city.$cust_Country.$postcode.$stateprov.$fname.$lname;*/

//                            dataString = sid + "" + vid + "" + cardNo + "" + cvv + "" + expiryMonth + "" + expiryYear +
//                                         "" + customerCardBillingAddress + "" + customerCity + "" + customerCountry +
//                                         "" + customerCardBillingPostalCode + "" + customerState + "" + firstName + "" + lastName;

                            dataString=sid+vid+cardNo+cvv+expiryMonth+expiryYear+customerCardBillingAddress+
                                    customerCity+customerCountry+customerCardBillingPostalCode+customerState+firstName+lastName;
                            //  dataString=sid+"quickeats45145700000792397230919H-35 Sector 63NoidaIndia201307Uttar PradeshGIRISHNAILWAL";
                            Log.w("TAG", "mySecret : " + mySecret);
                            Log.w("TAG", "dataString : " + dataString);

                            String hash = hashMac(mySecret, dataString);
                            Log.d("TAG", "result2 : " + hash);

                           creditCardServiceHit(hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //alertDialogcodAndproceed();
                    }
//                    else {
//                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(CreditCard.this).hideDialog();
            }
        });
    }


    private void creditCardServiceHit(final String hashId) throws JSONException {

        MyDialog.getInstance(this).showDialog();
        Retrofit retrofit = ApiClient.getClientPaymentMpesa();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CommonResponse> call = apiInterface.transactCreditCard(
                sid,
                vid,
                cardNo,
                cvv,
                expiryMonth,
                expiryYear,
                curr,
                customerCardBillingAddress,
                customerCity,
                customerCountry,
                customerCardBillingPostalCode,
                customerState,
                firstName,
                lastName,
                hashId);

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                //MyDialog.getInstance(CreditCard.this).hideDialog();
                if (response.isSuccessful()) {

                    Log.w("TAG", "result final : " + hashId);
                    Log.w("status", response.body().getStatus());

                    switch (response.body().getStatus()) {
                        case "bdi6p2yy76etrs":  // pending request
                            //Toast.makeText(CreditCard.this, "Payment pending", Toast.LENGTH_LONG).show();
                            //alertDialog1();
                            break;

                        case "aei7p7yrx4ae34":  // success request
                            Toast.makeText(CreditCard.this, "Payment Successfully", Toast.LENGTH_LONG).show();

                            if (!myDialog.isShowing()) {
                                myDialog.showDialog();
                            }
                            transcationId = response.body().getTxncd();
                            paymentApi();
                            break;
                    }
                    MyDialog.getInstance(CreditCard.this).hideDialog();

                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(CreditCard.this).hideDialog();
            }
        });
    }

    private void alertDialog1() {
        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_pending_payment);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout llproceed = (LinearLayout) dialog.findViewById(R.id.ll_proceed);
        llproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void paymentApi() {

        try {
            Date date = new Date();
            MyDialog.getInstance(this).showDialog();
            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<CommonResponse> call = apiInterface.payment(
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.token),
                    SharedPreferenceWriter.getInstance(this).getString(SharedPreferenceKey.order_id),
                    "CREDIT CARD",
                    transcationId, "CREDIT CARD", dateFormat.format(date));

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    MyDialog.getInstance(CreditCard.this).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("SUCCESS")) {

                            Intent intent = new Intent(CreditCard.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            //  Toast.makeText(CreditCard.this, "Payment Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(CreditCard.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    MyDialog.getInstance(CreditCard.this).hideDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
