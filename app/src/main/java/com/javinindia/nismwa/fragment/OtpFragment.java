package com.javinindia.nismwa.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.activity.MainActivity;
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberLoginResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 05-07-2017.
 */

public class OtpFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtSubmit, txtResend;
    AppCompatEditText etOTP;
    ImageView imgBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        imgBack =  view.findViewById(R.id.imgBack);
        AppCompatTextView txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtResend =  view.findViewById(R.id.txtResend);
        txtResend.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSubmit =  view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etOTP =view.findViewById(R.id.etOTP);
        etOTP.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void clickMethod(View view) {
        txtSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtResend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(activity);
        switch (view.getId()) {
            case R.id.txtSubmit:
                loginMethod("submit");
                break;
            case R.id.txtResend:
                loginMethod("resend");
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            default:
                break;
        }
    }

    private void loginMethod(String test) {
        if (test.equalsIgnoreCase("submit")) {
            String username = etOTP.getText().toString().trim();
            if (validation(username)) {
                sendDataOnLoginApi(username, test);
            }
        } else {
            sendDataOnLoginApi(" ", test);
        }

    }

    private void sendDataOnLoginApi(final String username, final String test) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        responseImplement(response, test);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", SharedPreferencesManager.getMobile(activity));
                params.put("password", username);
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("deviceToken", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("deviceToken", " ");
                }
                if (!TextUtils.isEmpty(SharedPreferencesManager.getAndroidId(activity))) {
                    params.put("deviceId", SharedPreferencesManager.getAndroidId(activity));
                } else {
                    params.put("deviceId", " ");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response, String test) {
        String userid = " ", msg = " ", password = null, mobile = "", email = "", pic = "", firstName = "", empType = "", middleName = "", lastName = "", firmName = "";
        MemberLoginResponse responseparsing = new MemberLoginResponse();
        responseparsing.responseParseMethod(response);
        try {
            if (responseparsing.getStatus() == 1) {
                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getMemberId().trim())) {
                    userid = responseparsing.getDetailsArrayList().get(0).getMemberId().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getType().trim())) {
                    empType = responseparsing.getDetailsArrayList().get(0).getType().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getName().trim())) {
                    firstName = responseparsing.getDetailsArrayList().get(0).getName().trim();
                }


                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getMobileNumber().trim())) {
                    mobile = responseparsing.getDetailsArrayList().get(0).getMobileNumber().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getEmail().trim())) {
                    email = responseparsing.getDetailsArrayList().get(0).getEmail().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getImageUrl().trim())) {
                    pic = responseparsing.getDetailsArrayList().get(0).getImageUrl().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getPassword().trim())) {
                    password = responseparsing.getDetailsArrayList().get(0).getPassword().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getFirm_name().trim())) {
                    firmName = responseparsing.getDetailsArrayList().get(0).getFirm_name().trim();
                }


                if (test.equalsIgnoreCase("submit")) {
                    saveDataOnPreference(empType, firstName, mobile, email, userid, pic, password, firmName);
                    Intent refresh = new Intent(activity, MainActivity.class);
                    startActivity(refresh);//Start the same Activity
                    activity.finish();
                } else {
                    etOTP.setText("");
                }

            } else {
                if (!TextUtils.isEmpty(responseparsing.getMsg())) {
                    showDialogMethod(responseparsing.getMsg());
                } else {
                    showDialogMethod("Invalid username/password.");
                }
            }
        } catch (Exception e) {
            showToastMethod(e.toString());
        }
    }

    private void saveDataOnPreference(String empType, String username, String mobile, String email, String userid, String pic, String password, String FirmName) {
        SharedPreferencesManager.setType(activity, empType);
        SharedPreferencesManager.setUserID(activity, userid);
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, mobile);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setPassword(activity, password);
        SharedPreferencesManager.setProfileImage(activity, pic);
        SharedPreferencesManager.setFirmName(activity, FirmName);
    }


    private boolean validation(String username) {
        if (TextUtils.isEmpty(username)) {
            showToastMethod("you have not entered OTP.");
            return false;
        } else if (username.length() != 6) {
            showToastMethod("Entered OTP is invalid");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.otp_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

}
