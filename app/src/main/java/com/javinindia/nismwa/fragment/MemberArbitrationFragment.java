package com.javinindia.nismwa.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.javinindia.nismwa.modelClasses.managementlistparsing.ManagementListResponse;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 29-08-2017.
 */

public class MemberArbitrationFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    private int page = 6;
    private String ownerId;
    ImageView imgBack, imgOne, imgTwo, imgThree;
    AppCompatTextView txtOne, txtTwo, txtThree, txtNext, txtTitle;
    RelativeLayout rlOne, rlTwo, rlThree;
    LinearLayout llNext;
    int fram;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        sendDataOnApi(page);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            fram = R.id.containerMain;
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            fram = R.id.container;
            ownerId = "";
        }
        return view;
    }

    private void clickMethod(View view) {
        imgBack.setOnClickListener(this);
        txtNext.setOnClickListener(this);
    }

    private void sendDataOnApi(final int page) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MANAGEMENT_LIST_BY_PRIORITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        int status = 0;
                        String msg;
                        final ManagementListResponse candidateQuickJobsResponse = new ManagementListResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();

                            if (status == 1) {
                                if (candidateQuickJobsResponse.getDetailsArrayList().size() > 0) {
                                    int size = candidateQuickJobsResponse.getDetailsArrayList().size();

                                    if (size > 2) {
                                        final String id = candidateQuickJobsResponse.getDetailsArrayList().get(2).getMemberId().trim();
                                        String name = candidateQuickJobsResponse.getDetailsArrayList().get(2).getMc_name().trim();
                                        String desc = candidateQuickJobsResponse.getDetailsArrayList().get(2).getDesignationName().trim();
                                        String firm = candidateQuickJobsResponse.getDetailsArrayList().get(2).getFirm_name().trim();
                                        String address = candidateQuickJobsResponse.getDetailsArrayList().get(2).getAddress().trim();
                                        String phone = candidateQuickJobsResponse.getDetailsArrayList().get(2).getMobileNumber().trim();
                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(2).getImage().trim())) {
                                            String pic = candidateQuickJobsResponse.getDetailsArrayList().get(2).getImage().trim();
                                            Picasso.with(activity).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(imgThree);
                                        } else {
                                            Picasso.with(activity).load(R.drawable.profile_icon).transform(new CircleTransform()).into(imgThree);
                                        }

                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(2).getMc_name().trim())) {
                                            txtThree.setText(Utility.fromHtml("<font ><b>" + name + "</b><br>(" + desc + ")</font"));
                                        }
                                        rlThree.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                methodProfile(id);
                                            }
                                        });

                                    }
                                    if (size > 1) {
                                        final String id = candidateQuickJobsResponse.getDetailsArrayList().get(1).getMemberId().trim();
                                        String name = candidateQuickJobsResponse.getDetailsArrayList().get(1).getMc_name().trim();
                                        String desc = candidateQuickJobsResponse.getDetailsArrayList().get(1).getDesignationName().trim();
                                        String firm = candidateQuickJobsResponse.getDetailsArrayList().get(1).getFirm_name().trim();
                                        String address = candidateQuickJobsResponse.getDetailsArrayList().get(1).getAddress().trim();
                                        String phone = candidateQuickJobsResponse.getDetailsArrayList().get(1).getMobileNumber().trim();
                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(1).getImage().trim())) {
                                            String pic = candidateQuickJobsResponse.getDetailsArrayList().get(1).getImage().trim();
                                            Picasso.with(activity).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(imgTwo);
                                        } else {
                                            Picasso.with(activity).load(R.drawable.profile_icon).transform(new CircleTransform()).into(imgTwo);
                                        }

                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(1).getMc_name().trim())) {
                                            txtTwo.setText(Utility.fromHtml("<font ><b>" + name + "</b><br>(" + desc + ")</font"));
                                        }
                                        rlTwo.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                methodProfile(id);
                                            }
                                        });

                                    }
                                    if (size > 0) {
                                        final String id = candidateQuickJobsResponse.getDetailsArrayList().get(0).getMemberId().trim();
                                        String name = candidateQuickJobsResponse.getDetailsArrayList().get(0).getMc_name().trim();
                                        String desc = candidateQuickJobsResponse.getDetailsArrayList().get(0).getDesignationName().trim();
                                        String firm = candidateQuickJobsResponse.getDetailsArrayList().get(0).getFirm_name().trim();
                                        String address = candidateQuickJobsResponse.getDetailsArrayList().get(0).getAddress().trim();
                                        String phone = candidateQuickJobsResponse.getDetailsArrayList().get(0).getMobileNumber().trim();
                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(0).getImage().trim())) {
                                            String pic = candidateQuickJobsResponse.getDetailsArrayList().get(0).getImage().trim();
                                            Picasso.with(activity).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(imgOne);
                                        } else {
                                            Picasso.with(activity).load(R.drawable.profile_icon).transform(new CircleTransform()).into(imgOne);
                                        }

                                        if (!TextUtils.isEmpty(candidateQuickJobsResponse.getDetailsArrayList().get(0).getMc_name().trim())) {
                                            txtOne.setText(Utility.fromHtml("<font ><b>" + name + "</b><br>(" + desc + ")</font"));
                                        }
                                        rlOne.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                methodProfile(id);
                                            }
                                        });
                                    }
                                }

                            } else {
                                if (!TextUtils.isEmpty(msg)) {

                                }
                            }

                        } catch (Exception e) {
                            showToastMethod(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noInternetToast(error);
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("priority", String.valueOf(page));
                params.put("memberId", ownerId);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void methodProfile(String id) {
        OtherMemberProfile otherMemberProfile = new OtherMemberProfile();
        Bundle bundle = new Bundle();
        bundle.putString("Id", id);
        otherMemberProfile.setArguments(bundle);
        callFragmentMethod(otherMemberProfile, Constants.FRG_OTHER_MEMBER_PROFILE, fram);
    }

    private void initialize(View view) {
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Arbitration Panel");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgOne = (ImageView) view.findViewById(R.id.imgOne);
        imgTwo = (ImageView) view.findViewById(R.id.imgTwo);
        imgThree = (ImageView) view.findViewById(R.id.imgThree);

        txtOne = view.findViewById(R.id.txtOne);
        txtOne.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtTwo = view.findViewById(R.id.txtTwo);
        txtTwo.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtThree = view.findViewById(R.id.txtThree);
        txtThree.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtNext = view.findViewById(R.id.txtNext);
        txtNext.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtNext.setText("Previous");
        rlOne = view.findViewById(R.id.rlOne);
        rlTwo = view.findViewById(R.id.rlTwo);
        rlThree = view.findViewById(R.id.rlThree);

        llNext = view.findViewById(R.id.llNext);
        llNext.setVisibility(View.GONE);
        txtNext.setVisibility(View.GONE);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.management_three_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtNext:
                activity.onBackPressed();
                break;
        }
    }
}
