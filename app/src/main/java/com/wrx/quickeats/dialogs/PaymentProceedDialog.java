package com.wrx.quickeats.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.wrx.quickeats.activities.PaymentBackground;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.Generate256Hash;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.databinding.InfoMpesaProceedDialogBinding;

import org.json.JSONException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class PaymentProceedDialog extends Dialog implements View.OnClickListener {
    Context context;
    private LayoutInflater inflater;
    InfoMpesaProceedDialogBinding binding;
    String mobileNo="";
    String currency = "";
    private static final String HASH_ALGORITHM = "HmacSHA256";
    private String cbk;
    private String cst, curr, vid, live, oid, inv, amount, tel, eml, sid;
    private String mySecret;

    public PaymentProceedDialog(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        binding = DataBindingUtil.inflate(inflater, R.layout.info_mpesa_proceed_dialog, null, false);
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(false);
        binding.btnDone.setOnClickListener(this);
        binding.selectCurrency.setOnClickListener(this);
        binding.llIpesaDialog.setOnClickListener(this);
    }

    private boolean validateForm() {
         boolean validate = true;
         mobileNo = binding.phoneno.getText().toString().trim();
         currency = binding.selectCurrency.getText().toString().trim();
        if (mobileNo.length() == 0) {
            Toast.makeText(context, "Please enter mobile No.", Toast.LENGTH_LONG).show();
            return false;

        } else if (currency.length() == 0) {
            Toast.makeText(context, "Please select currency.", Toast.LENGTH_LONG).show();
            return false;
        }
        return validate;
    }

    private void showCurrency() {
        final List<String> currenclyList = new ArrayList<>();
        currenclyList.add("KES");
        currenclyList.add("USD");
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, currenclyList);
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
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
        if (view==binding.selectCurrency){
            showCurrency();
        }else if (view==binding.btnDone){
            if (validateForm()){
                callMpessa(mobileNo,currency);
            }
        }else if (view==binding.llIpesaDialog){
            dismiss();
        }

    }
    private void callMpessa(String mobileNo, String currency) {
        //first we have to generate hash_id
        live = "1";
        oid = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.order_id);
        inv = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.order_id);
        amount = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.AMOUNT);
        tel = mobileNo;
        eml = SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.Email);
        vid = "quickeats";
        curr = currency;
        cst = "1";
        cbk = "callback";

        mySecret = "54dshukic8f87d8dod3d589"; //Replace with your own Secret
        String myDataString = live + "" + oid + "" + inv + "" + amount + "" + tel + "" + eml + "" + vid + "" + curr + "" + cst + "" + cbk; // Replace with your data

        try {
            //Log.i("HASH IPAY", "" + hashMac("54dshukic8f87d8dod3d589", "15035031201.08859339243aht@gmail.comquickeatsKES1callback"));
            String hashId = hashMac(mySecret,myDataString);
            Log.d("TAG", "result : " +hashId);
            paymentMpaisaApi(hashId );

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

    private void paymentMpaisaApi(String hashId) throws JSONException {

        MyDialog.getInstance(context).showDialog();
        Retrofit retrofit = ApiClient.getClientPaymentMpesa();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        Call<CommonResponse> call = apiInterface.transact(
                live,
                oid,
                inv,
                SharedPreferenceWriter.getInstance(context).getString(SharedPreferenceKey.AMOUNT),
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
                MyDialog.getInstance(context).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1"))
                    {

                        sid=response.body().getmPesaResponse().getSid();
                        try {

                            String dataString=tel+vid+sid;
                            mpesaServiceHit(Generate256Hash.hashMac(mySecret, dataString));
                        }
                        catch (Exception e)
                        {
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
                MyDialog.getInstance(context).hideDialog();
            }
        });
    }

    private void mpesaServiceHit(String hashId) throws JSONException {

        MyDialog.getInstance(context).showDialog();
        Retrofit retrofit = ApiClient.getClientPaymentMpesa();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        Call<CommonResponse> call = apiInterface.getMpessa(
                tel,
                sid,
                vid,
                hashId);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(context).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("1"))
                    {
                        Intent intent=new Intent(context, PaymentBackground.class);
                        intent.putExtra("vid", vid);
                        intent.putExtra("sid", sid);
                        intent.putExtra("secretkey", mySecret);
                        context.startActivity(intent);
                        dismiss();

                        Toast.makeText(context, ""+response.body().getText(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(context).hideDialog();
            }
        });
    }


}
