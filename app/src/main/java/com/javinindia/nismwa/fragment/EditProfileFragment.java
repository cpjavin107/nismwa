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
import com.javinindia.nismwa.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 28-07-2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtName, txtOffice, txtUpdate, txtResidence, txtEmail, txtWeb, txtOffice2;
    AppCompatEditText etName, etOffice, etResidence, etEmail, etWebsite, etOffice2;
    ImageView imgBack;
    String name = "", office = "", office2 = "", residence = "", email = "", website = "";

    private OnCallBackEditProfileViewListener backProfileEditListener;


    public interface OnCallBackEditProfileViewListener {
        void OnCallBackEditProfileView();
    }

    public void setMyCallBackProfileEditListener(OnCallBackEditProfileViewListener callback) {
        this.backProfileEditListener = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        office = getArguments().getString("off");
        office2 = getArguments().getString("off2");
        residence = getArguments().getString("res");
        email = getArguments().getString("mail");
        website = getArguments().getString("website");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            getArguments().clear();
        }
    }

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

        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Edit Profile");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtName = view.findViewById(R.id.txtName);
        txtName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtOffice = view.findViewById(R.id.txtOffice);
        txtOffice.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffice2 = view.findViewById(R.id.txtOffice2);
        txtOffice2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtResidence = view.findViewById(R.id.txtResidence);
        txtResidence.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmail = view.findViewById(R.id.txtEmail);
        txtEmail.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtWeb = view.findViewById(R.id.txtWeb);
        txtWeb.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtUpdate = view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etName = view.findViewById(R.id.etName);
        etName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etOffice = view.findViewById(R.id.etOffice);
        etOffice.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etOffice2 = view.findViewById(R.id.etOffice2);
        etOffice2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etResidence = view.findViewById(R.id.etResidence);
        etResidence.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmail = view.findViewById(R.id.etEmail);
        etEmail.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etWebsite = view.findViewById(R.id.etWebsite);
        etWebsite.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etName.setText(Utility.fromHtml(name));
        etOffice.setText(Utility.fromHtml(office));
        etOffice2.setText(Utility.fromHtml(office2));
        etResidence.setText(Utility.fromHtml(residence));
        etEmail.setText(Utility.fromHtml(email));
        etWebsite.setText(Utility.fromHtml(website));
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
        String name = etName.getText().toString().trim();
        String office = etOffice.getText().toString().trim();
        String residence = etResidence.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String web = etWebsite.getText().toString().trim();
        String off2 = etOffice2.getText().toString().trim();
        if (validation(name, office, residence, email)) {
            sendDataOnEditApi(name, office, residence, email, web, off2);
        }
    }

    private void sendDataOnEditApi(final String name, final String office, final String residence, final String email, final String web, final String off2) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_EDIT_PROFILE_URL,
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
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                params.put("name", name);
                params.put("officeNo", office);
                params.put("officeNo2", off2);
                params.put("residenceNo", residence);
                params.put("email", email);
                params.put("website", web);
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
                    backProfileEditListener.OnCallBackEditProfileView();
                    showToastMethod("Your profile details successfully updated");
                    activity.onBackPressed();
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        showToastMethod(msg);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }

    private boolean validation(String name, String office, String residence, String email) {
        if (!TextUtils.isEmpty(office) && office.length() < 6) {
            showToastMethod("Invalid Office number");
            return false;
        } else if (!TextUtils.isEmpty(residence) && residence.length() < 6) {
            showToastMethod("Invalid Residence number");
            return false;
        } else if (!TextUtils.isEmpty(email) && !Utility.isEmailValid(email)) {
            showToastMethod("Invalid Email");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.edit_profile_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}