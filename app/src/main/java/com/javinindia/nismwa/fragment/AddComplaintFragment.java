package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.nismwa.R;
import com.javinindia.nismwa.constant.Constants;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 06-06-2017.
 */

public class AddComplaintFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtSubmit;
    AppCompatEditText etCompTitle, etDescription;
    ImageView imgBack;
    int fram;
    private String ownerId;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();
    int page_position = 0;
    ImageView imgAdds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            this.getArguments().clear();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            fram = R.id.containerMain;
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            fram = R.id.container;
            ownerId = "";
        }
        hitaddMethod();
        return view;
    }

    private void hitaddMethod() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        final AdsResponse candidateQuickJobsResponse = new AdsResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            if (candidateQuickJobsResponse.getAdsDetailArrayList().size() > 0) {
                                adsDetailArrayList = candidateQuickJobsResponse.getAdsDetailArrayList();
                                onResume();
                            }
                        } catch (Exception e) {
                            showToastMethod(e.toString());
                        }
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
                params.put("ownerId", ownerId);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void clickMethod(View view) {
        txtSubmit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    private void initialize(View view) {
        imgAdds =  view.findViewById(R.id.imgAdds);
        imgBack =  view.findViewById(R.id.imgBack);

        txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTitle.setText("Add Suggestion/Complaints");

        etCompTitle = view.findViewById(R.id.etCompTitle);
        etCompTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDescription =  view.findViewById(R.id.etDescription);
        etDescription.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSubmit =  view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (adsDetailArrayList.size() > 0) {
                page_position = 0;
                CountDownTimer waitTimer = new CountDownTimer(1000000 * adsDetailArrayList.size(), 5000) {
                    public void onTick(long millisUntilFinished) {
                        if (page_position == adsDetailArrayList.size() - 1) {
                            page_position = 0;
                        } else {
                            page_position = page_position + 1;
                        }
                        methodSetAds(page_position);
                    }

                    public void onFinish() {
                    }
                }.start();
            }
        } catch (Exception e) {
            showToastMethod(e.toString());
        }
    }

    private void methodSetAds(final int page_position) {
        if (!TextUtils.isEmpty(adsDetailArrayList.get(page_position).getSmall_image().trim())) {
            String ads = adsDetailArrayList.get(page_position).getSmall_image().trim();
            Picasso.with(activity).load(ads).error(R.drawable.profile_bg).placeholder(R.drawable.profile_bg).into(imgAdds);
        } else {
            Picasso.with(activity).load(R.drawable.profile_bg).into(imgAdds);
        }
        if (!TextUtils.isEmpty(adsDetailArrayList.get(page_position).getBig_image().trim())) {
            imgAdds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    FullSizeImageFragment fullSizeImageFragment = new FullSizeImageFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("img", adsDetailArrayList.get(page_position).getBig_image().trim());
                    fullSizeImageFragment.setArguments(bundle1);
                    fullSizeImageFragment.show(ft, Constants.FRG_FULLIMAGE);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(activity);
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;

            case R.id.txtSubmit:
                submitComplain();
                break;
            default:
                break;
        }
    }

    private void submitComplain() {
        String title = etCompTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();

        if (validation(title, desc)) {
            submitMethod(title, desc);
        }
    }


    private void submitMethod(final String title, final String desc) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_SUGGESTION_URL,
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
                params.put("title", title);
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("description", desc);
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
                    showToastMethod("Your suggestion successfully submitted");
                    etCompTitle.setText("");
                    etDescription.setText("");
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
            showToastMethod(e.toString());
        }
    }

    private boolean validation(String title, String desc) {
        if (TextUtils.isEmpty(title)) {
            showToastMethod("You have not entered title.");
            return false;
        } else if (TextUtils.isEmpty(desc)) {
            showToastMethod("You have not entered description.");
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.addcomplaint_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

}
