package com.javinindia.nismwa.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.javinindia.nismwa.preference.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Ashish on 02-05-2017.
 */

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtOldPassword, txtNewPassword, txtConfirmPassword, txtUpdate;
    AppCompatEditText etOldPassword, etNewPassword, etConfirmPassword;
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
        txtUpdate.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }


    private void initialize(View view) {
        imgBack =  view.findViewById(R.id.imgBack);

        txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setText("Change Password");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtOldPassword =  view.findViewById(R.id.txtOldPassword);
        txtOldPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtNewPassword =  view.findViewById(R.id.txtNewPassword);
        txtNewPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtConfirmPassword =  view.findViewById(R.id.txtConfirmPassword);
        txtConfirmPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUpdate =  view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etOldPassword =  view.findViewById(R.id.etOldPassword);
        etOldPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etNewPassword =  view.findViewById(R.id.etNewPassword);
        etNewPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etConfirmPassword =  view.findViewById(R.id.etConfirmPassword);
        etConfirmPassword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtUpdate:
                updateMethod();
                break;
            default:
                break;
        }
    }

    private void updateMethod() {
        String old = etOldPassword.getText().toString().trim();
        String passwordNew = etNewPassword.getText().toString().trim();
        String passwordCon = etConfirmPassword.getText().toString().trim();
        SharedPreferencesManager.setPassword(activity, passwordNew);
        if (validation(old, passwordNew, passwordCon)) {
            sendDataOnChangePassApi(old, passwordNew, passwordCon);
        }
    }

    private void sendDataOnChangePassApi(final String old, final String passwordNew, String passwordCon) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.CHANGE_PASSWORD_URL,
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
                params.put("id", SharedPreferencesManager.getUserID(activity));
                params.put("oldPassword", old);
                params.put("newPassword", passwordNew);
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
                if (jsonObject.has("pass")) {
                    pass = jsonObject.optString("pass");
                    SharedPreferencesManager.setPassword(activity, pass);
                }
                if (status == 1) {
                    etOldPassword.setText("");
                    etNewPassword.setText("");
                    etConfirmPassword.setText("");
                    showToastMethod("Your password successfully changed");
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        showToastMethod(msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            showToastMethod(e.toString());
        }
    }

    private boolean validation(String old, String newP, String comf) {
        if (TextUtils.isEmpty(old)) {
            etOldPassword.setError("Old password field is empty.");
            etOldPassword.requestFocus();
            return false;
        } else if (newP.length() < 6) {
            etNewPassword.setError("Invalid new password.");
            etNewPassword.requestFocus();
            return false;
        } else if (!comf.equalsIgnoreCase(newP)) {
            etConfirmPassword.setError("Confirm password does not matched");
            etConfirmPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.chage_password_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}
