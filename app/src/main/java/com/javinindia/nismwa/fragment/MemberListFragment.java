package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberLoginResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.MembersAdopter;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 06-07-2017.
 */

public class MemberListFragment extends BaseFragment implements View.OnClickListener, MembersAdopter.MyClickListener, CheckConnectionFragment.OnCallBackInternetListener, FilterMemberFragment.OnCallBackQuickJobFilterListener {

    AppCompatTextView txtLoading, txtTitle;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private MembersAdopter adapter;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList arrayList = new ArrayList();

    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String ownerId;
    AppCompatEditText etSearch;
    String ownerName = "", address = "", deal = "", firmName = "", blockId = "";
    boolean state = false;
    ImageView imgBack, imgAdds;
    int fram;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();
    int page_position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownerName = getArguments().getString("ownerName");
        address = getArguments().getString("address");
        deal = getArguments().getString("deal");
        firmName = getArguments().getString("firmName");
        blockId = getArguments().getString("blockId");
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
        //  sendDataOnApi(page, ownerName, address, deal, firmName, blockId);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            fram = R.id.containerMain;
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            fram = R.id.container;
            ownerId = "";
        }
        sendDataOnApi(page, ownerName, address, deal, firmName, blockId);
        /*FilterMemberFragment filterQuickJobsFragment = new FilterMemberFragment();
        filterQuickJobsFragment.setMyCallBackQuickJobFilterListener(this);
        callFragmentMethod(filterQuickJobsFragment, Constants.FRG_FILTER, fram);*/
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        try {
            if (adsDetailArrayList.size() > 0 && page == 1) {
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

    private void initialize(View view) {
        imgBack = view.findViewById(R.id.imgBack);
        imgAdds = view.findViewById(R.id.imgAdds);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Members");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        mSwipeRefreshLayout = view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress);

        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new MembersAdopter(activity, 0);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(MemberListFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLoading.setText(Utility.fromHtml("Please Enter Keyword to Search"));

        RelativeLayout rlSearch = view.findViewById(R.id.rlSearch);
        rlSearch.setVisibility(View.VISIBLE);

        etSearch = view.findViewById(R.id.etSearch);
        etSearch.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etSearch.setFocusable(false);
        etSearch.setFocusableInTouchMode(false);
        etSearch.setClickable(true);

        imgBack.setOnClickListener(this);
        etSearch.setOnClickListener(this);
    }

    private void sendDataOnApi(final int page, final String name, final String add, final String del, final String fname, final String blc) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBERS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("resssss", response);
                        progressBar.setVisibility(View.GONE);
                        int status = 0;
                        String msg;
                        final MemberLoginResponse candidateQuickJobsResponse = new MemberLoginResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                            if (candidateQuickJobsResponse.getAdsDetailArrayList().size() > 0) {
                                adsDetailArrayList = candidateQuickJobsResponse.getAdsDetailArrayList();
                                onResume();
                            }
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getDetailsArrayList().size() > 0) {
                                    state = true;
                                    arrayList = candidateQuickJobsResponse.getDetailsArrayList();
                                    if (arrayList.size() > 0) {
                                        txtLoading.setText(" ");

                                        if (adapter.getData() != null && adapter.getData().size() > 0) {
                                            adapter.getData().addAll(arrayList);
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            adapter.setData(arrayList);
                                            adapter.notifyDataSetChanged();
                                        }

                                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                            @Override
                                            public void onRefresh() {
                                                arrayList.removeAll(arrayList);
                                                adapter.notifyDataSetChanged();
                                                adapter.setData(null);
                                                mSwipeRefreshLayout.setRefreshing(false);
                                                if (arrayList.size() > 0) {

                                                } else {
                                                    methodSwipe();
                                                }
                                            }
                                        });
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    } else {
                                        txtLoading.setText("Not found");
                                    }

                                }

                            } else {
                                mSwipeRefreshLayout.setRefreshing(false);
                                if (!TextUtils.isEmpty(msg)) {
                                    if (page == 1) {
                                        txtLoading.setText(msg);
                                    } else {
                                        txtLoading.setText(" ");
                                    }
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(page));
                params.put("ownerName", name);
                params.put("address", add);
                params.put("deal", del);
                params.put("firmName", fname);
                params.put("block", blc);
                params.put("ownerId", ownerId);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void methodSwipe() {
        page = 1;
        startLimit = 0;
        sendDataOnApi(page, ownerName, address, deal, firmName, blockId);
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(activity);
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.etSearch:
                activity.onBackPressed();
                /*FilterMemberFragment filterQuickJobsFragment = new FilterMemberFragment();
                filterQuickJobsFragment.setMyCallBackQuickJobFilterListener(this);
                callFragmentMethod(filterQuickJobsFragment, Constants.FRG_FILTER, fram);*/
                break;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.member_list_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onItemClick(int position, MemberDetail detailsList) {
        OtherMemberProfile otherMemberProfile = new OtherMemberProfile();
        Bundle bundle = new Bundle();
        bundle.putString("Id", detailsList.getMemberId().trim());
        otherMemberProfile.setArguments(bundle);
        callFragmentMethod(otherMemberProfile, Constants.FRG_OTHER_MEMBER_PROFILE, fram);
    }

    @Override
    public void OnCallBackInternet() {
        activity.onBackPressed();
    }

    @Override
    public void OnCallBackQuickJobFilter(String title, String location, String keyword, String day, String block) {
        /*arrayList.removeAll(arrayList);
        adapter.notifyDataSetChanged();
        adapter.setData(null);
        page = 1;
        startLimit = 0;
        ownerName = title;
        address = day;
        deal = keyword;
        firmName = location;
        blockId = block;
        adapter.setData(null);
        sendDataOnApi(page, ownerName, address, deal, firmName, blockId);
        activity.onBackPressed();*/
    }


    public class replyScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 10) ? totalItemCount / 2 : 10;
            int firstVisibleItem = recyclerLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > startLimit) {
                    loading = false;
                    startLimit = totalItemCount;
                }
            } else {
                int nonVisibleItemCounts = totalItemCount - visibleItemCount;
                int effectiveVisibleThreshold = firstVisibleItem + visibleThreshold;

                if (nonVisibleItemCounts <= effectiveVisibleThreshold) {
                    startLimit = startLimit + 1;
                    page = page + 1;
                    /// countLimit = 10;

                    sendDataOnApi(page, ownerName, address, deal, firmName, blockId);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
