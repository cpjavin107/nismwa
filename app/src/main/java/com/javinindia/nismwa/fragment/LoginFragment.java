package com.javinindia.nismwa.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
 * Created by Ashish on 06-04-2017.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtSubmit;
    AppCompatEditText etMobile;


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
        AppCompatTextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSubmit = view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etMobile = view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void clickMethod(View view) {
        txtSubmit.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.login_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(activity);
        switch (view.getId()) {
            case R.id.txtSubmit:
                loginMethod();
                break;
            default:
                break;
        }
    }


    private void loginMethod() {
        String username = etMobile.getText().toString().trim();
        sendDataOnLoginApi(username);
    }


    private void sendDataOnLoginApi(final String username) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("res ", SharedPreferencesManager.getDeviceToken(activity) + " " + response);
                        loading.dismiss();
                        responseImplement(response);
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
                params.put("mobile", username);
                params.put("password", " ");
                if (!TextUtils.isEmpty(SharedPreferencesManager.getDeviceToken(activity))) {
                    params.put("deviceToken", SharedPreferencesManager.getDeviceToken(activity));
                } else {
                    params.put("deviceToken", "deviceToken");
                }
                if (!TextUtils.isEmpty(SharedPreferencesManager.getAndroidId(activity))) {
                    params.put("deviceId", SharedPreferencesManager.getAndroidId(activity));
                } else {
                    params.put("deviceId", "deviceId");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
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

                saveDataOnPreference(empType, firstName, mobile, email, userid, pic, password, firmName);
                OtpFragment societyMembersFragment = new OtpFragment();
                callFragmentMethod(societyMembersFragment, Constants.FRG_OTP, R.id.container);
            /*Intent refresh = new Intent(activity, MainActivity.class);
            startActivity(refresh);//Start the same Activity
            activity.finish();*/
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
        // SharedPreferencesManager.setUserID(activity, userid);
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, mobile);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setPassword(activity, password);
        SharedPreferencesManager.setProfileImage(activity, pic);
        SharedPreferencesManager.setFirmName(activity, FirmName);
    }

    private boolean validation(String username) {
        if (TextUtils.isEmpty(username)) {
            showToastMethod("you have not entered mobile number.");
            return false;
        } else if (username.length() != 10) {
            showToastMethod("Entered mobile number is invalid");
            return false;
        } else {
            return true;
        }
    }

}
