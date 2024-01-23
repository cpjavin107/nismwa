package com.javinindia.nismwa.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
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
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ashish on 25-04-2017.
 */

public class ForgotPasswordFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtForgot, txtForgotdiscription;
    AppCompatEditText etEmail;
    AppCompatButton btn_reset_password;
    ImageView imgBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        return view;
    }

    private void clickMethod(View view) {
        btn_reset_password.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        AppCompatTextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        imgBack = view.findViewById(R.id.imgBack);

        etEmail = view.findViewById(R.id.etEmail);
        etEmail.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtForgot = view.findViewById(R.id.txtForgot);
        txtForgot.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtForgotdiscription = view.findViewById(R.id.txtForgotdiscription);
        txtForgotdiscription.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        btn_reset_password = view.findViewById(R.id.btn_reset_password);
        btn_reset_password.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.forgot_password_layout;
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
        switch (view.getId()) {
            case R.id.btn_reset_password:
                if (!Utility.isEmailValid(etEmail.getText().toString().trim())) {
                    etEmail.setError("Email field is empty.");
                    etEmail.requestFocus();
                } else {
                    String uname = etEmail.getText().toString().trim();
                    sendForgetPasswordApi(uname);
                }
                break;
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            default:
                break;
        }
    }

    private void sendForgetPasswordApi(final String uname) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FORGET_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                params.put("uname", uname);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        try {
            JSONObject jsonObject = null;
            String msg = null, pass = null;
            int status = 0;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.has("status"))
                    status = jsonObject.optInt("status");
                if (jsonObject.has("msg"))
                    msg = jsonObject.optString("msg");
                if (status == 1) {
                    etEmail.setText("");
                    showToastMethod("Your have to check your registered email address");
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        showToastMethod("Invalid Email Id.");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }
}
