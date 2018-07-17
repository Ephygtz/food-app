package com.wrx.quickeats.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.wrx.quickeats.R;
import com.wrx.quickeats.activities.Login;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.bean.ProfileResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.util.TakePhoto;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
    private String imagePath = null;
    private String imageFilePath = null;


    private int START_VERIFICATION = 1001;
    View view;
    ImageView iv_profile_pic;
    EditText user_name, user_email, phone_no, address;
    TextView tvEdit;
    private final int CAMERA_PIC_REQUEST = 11, REQ_CODE_PICK_IMAGE = 1;
    private File fileFlyer;

    private final int CAMERA_REQ_CODE = 10;
    private final int GALLERY_REQ_CODE = 12;
    private String imageFilePathProfile = null;
    private Uri imageUriProfile = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        iv_profile_pic = (ImageView) view.findViewById(R.id.iv_profile_pic);
        user_name = (EditText) view.findViewById(R.id.user_name);
        user_email = (EditText) view.findViewById(R.id.user_email);
        phone_no = (EditText) view.findViewById(R.id.phone_no);
        address = (EditText) view.findViewById(R.id.address);
        tvEdit = (TextView) view.findViewById(R.id.tvEdit);

        callGetProfileApi();
        setDisable();

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Edit".equalsIgnoreCase(tvEdit.getText().toString())) {
                    setEnable();
                } else {
                    callGetProfileApi();
                    setDisable();
                }

            }
        });
        setHasOptionsMenu(true);

        return view;
    }

    private void setDisable() {
        iv_profile_pic.setEnabled(false);
        user_name.setEnabled(false);
        user_email.setEnabled(false);
        phone_no.setEnabled(false);
        address.setEnabled(false);

        tvEdit.setText("Edit");
        tvEdit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);


    }

    private void setEnable() {
        iv_profile_pic.setEnabled(true);
        user_name.setEnabled(true);
        // user_email.setEnabled(true);
        phone_no.setEnabled(true);
        address.setEnabled(true);

        tvEdit.setText("Save");
        tvEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvEdit.setGravity(Gravity.CENTER);


        iv_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void dispatchCameraIntent() {
        Intent i = new Intent(getActivity(), TakePhoto.class);
        i.putExtra(TakePhoto.FROM, TakePhoto.CAMERA);
        i.putExtra(TakePhoto.SIZE, TakePhoto.WANT_FULL_SIZE);
        startActivityForResult(i, CAMERA_REQ_CODE);
    }

    private void dispatchGalleryIntent() {
        Intent i = new Intent(getActivity(), TakePhoto.class);
        i.putExtra(TakePhoto.FROM, TakePhoto.GALLERY);
        i.putExtra(TakePhoto.SIZE, TakePhoto.WANT_FULL_SIZE);
        startActivityForResult(i, GALLERY_REQ_CODE);
    }

    private void selectImage() {
        final CharSequence[] items = {
            //                getResources().getString(R.string.Take_Photo),
            //                getResources().getString(R.string.Choose_from_Library),
            "take photo", "take libarary",
            getResources().getString(R.string.cancel)};

        final Dialog dialog = new Dialog(getActivity(), R.style.ThemeDialogCustom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_select_photo_dialog);

        LinearLayout llCamera = (LinearLayout) dialog.findViewById(R.id.llCamera);
        LinearLayout llGallary = (LinearLayout) dialog.findViewById(R.id.llGallary);
        LinearLayout llCancle = (LinearLayout) dialog.findViewById(R.id.llCancle);
        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchCameraIntent();
                dialog.dismiss();
                //                Intent intent = new Intent(getActivity(), TakeImage.class);
                //                intent.putExtra("from", "camera");
                //                startActivityForResult(intent, CAMERA_PIC_REQUEST);
                //                dialog.dismiss();
            }
        });
        llGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchGalleryIntent();
                dialog.dismiss();
                //                Intent intent = new Intent(getActivity(), TakeImage.class);
                //                intent.putExtra("from", "gallery");
                //                startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
                //                dialog.dismiss();
            }
        });

        llCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        if (requestCode == START_VERIFICATION) {
        //            if (resultCode == RESULT_OK) {
        //                getActivity().setResult(RESULT_OK);
        //                getActivity().finish();
        //            }
        //        } else if (resultCode == RESULT_OK) {
        //            if (data.getStringExtra("filePath") != null) {
        //                imagePath = data.getStringExtra("filePath");
        //                fileFlyer = new File(data.getStringExtra("filePath"));
        //
        //                if (fileFlyer.exists() && fileFlyer != null) {
        //                    iv_profile_pic.setImageURI(Uri.fromFile(fileFlyer));
        //                }
        //            }
        //        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
        //            getActivity().finish();
        //        }
        //        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_REQ_CODE: //CAMERA
                if (resultCode == RESULT_OK) {
                    try {

                        imageFilePath = data.getStringExtra("path");
                        final Uri imageUri = data.getParcelableExtra("uri");

                        imageFilePathProfile = imageFilePath;
                        imageUriProfile = imageUri;


                        fileFlyer = new File(imageFilePathProfile);

                        if (fileFlyer.exists() && fileFlyer != null) {
                            iv_profile_pic.setImageURI(Uri.fromFile(fileFlyer));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case GALLERY_REQ_CODE: //GALLERY
                if (resultCode == RESULT_OK) {
                    try {

                        imageFilePath = data.getStringExtra("path");
                        final Uri imageUri = data.getParcelableExtra("uri");
                        String uriString = imageUri.toString();

                        imageFilePathProfile = imageFilePath;
                        imageUriProfile = imageUri;

                        fileFlyer = new File(imageFilePathProfile);

                        if (fileFlyer.exists() && fileFlyer != null) {
                            iv_profile_pic.setImageURI(Uri.fromFile(fileFlyer));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);
        }


    }

    @Override
    public void onCreateOptionsMenu(
        Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                callLogoutApi();

                //                getActivity().finish();
                //                Intent intent = new Intent(getActivity(), Login.class) ;
                //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //                startActivity(intent);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callLogoutApi() {
        MyDialog.getInstance(getActivity()).showDialog();
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<CommonResponse> call = apiInterface.getLogoutResult(SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.token));

        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                MyDialog.getInstance(getActivity()).hideDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("SUCCESS")) {
                        getActivity().finish();
                        SharedPreferenceWriter.getInstance(getActivity()).writeBooleanValue(SharedPreferenceKey.currentLogin, false);
                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                MyDialog.getInstance(getActivity()).hideDialog();
            }
        });
    }


    private Map<String, RequestBody> setUpMapData(String the_token) {

        Map<String, RequestBody> fields = new HashMap<>();

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), the_token);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), user_name.getText().toString());
        RequestBody email_id = RequestBody.create(MediaType.parse("text/plain"), user_email.getText().toString());
        RequestBody phn_no = RequestBody.create(MediaType.parse("text/plain"), phone_no.getText().toString());
        RequestBody addrs = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString());

        fields.put("token", token);
        fields.put("name", name);
        fields.put("email", email_id);
        fields.put("mobile_number", phn_no);
        fields.put("address", addrs);

        return fields;
    }


    private void callGetProfileApi() {
        MyDialog.getInstance(getActivity()).showDialog();

        String token = SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.token);

        if (!token.isEmpty()) {
            RequestBody profile_body;
            MultipartBody.Part profilePart;

            if (imageFilePath != null) {

                File file = new File(imageFilePath);
                profile_body = RequestBody.create(MediaType.parse("image/*"), file);
                profilePart = MultipartBody.Part.createFormData("image", file.getName(), profile_body);
            } else {
                profilePart = MultipartBody.Part.createFormData("image", "");
            }

            Retrofit retrofit = ApiClient.getClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Map<String, RequestBody> map = setUpMapData(token);

            Call<CommonResponse> call = apiInterface.getProfileResult(map, profilePart);

            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    MyDialog.getInstance(getActivity()).hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals("SUCCESS")) {
                            ProfileResponse profileResponse = response.body().getProfile();

                            if (!response.body().getProfile().getImage_url().equals("")) {
                                AQuery aQuery = new AQuery(getActivity());
                                aQuery.id(iv_profile_pic).image(response.body().getProfile().getImage_url(), false, false, 0, R.drawable.default_prof_pic);
                            }
                            user_name.setText(profileResponse.getName());
                            user_email.setText(profileResponse.getEmail());
                            phone_no.setText(profileResponse.getMobile_number());
                            address.setText(profileResponse.getAddress());
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    MyDialog.getInstance(getActivity()).hideDialog();
                }
            });
        }

    }
}

