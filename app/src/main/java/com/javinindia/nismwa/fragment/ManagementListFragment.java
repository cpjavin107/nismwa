package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.javinindia.nismwa.modelClasses.managementlistparsing.ManagementDetail;
import com.javinindia.nismwa.modelClasses.managementlistparsing.ManagementListResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.ManagementAdopter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 06-07-2017.
 */

public class ManagementListFragment extends BaseFragment implements View.OnClickListener, ManagementAdopter.MyClickListener {

    AppCompatTextView txtLoading, txtTitle;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private ManagementAdopter adapter;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList arrayList;
    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String ownerId;
    AppCompatEditText etSearch;
    String searchTitle = "";
    ImageView imgBack, imgAdds;
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
        sendDataOnApi(page, searchTitle);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            fram = R.id.containerMain;
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            fram = R.id.container;
            ownerId = "";
        }
        return view;
    }

    private void initialize(View view) {
        imgAdds = view.findViewById(R.id.imgAdds);
        imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Management");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        arrayList = new ArrayList();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.addOnScrollListener(new replyScrollListener());
        /*LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(layoutManager);*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ManagementAdopter(activity);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(ManagementListFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        RelativeLayout rlSearch = view.findViewById(R.id.rlSearch);
        rlSearch.setVisibility(View.GONE);
    }


    private void sendDataOnApi(final int page, final String name) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MANAGEMENT_LIST_BY_PRIORITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        int status = 0;
                        String msg;
                        final ManagementListResponse candidateQuickJobsResponse = new ManagementListResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                           /* if (candidateQuickJobsResponse.getAdsDetailArrayList().size() > 0) {
                                if (!TextUtils.isEmpty(candidateQuickJobsResponse.getAdsDetailArrayList().get(0).getSmall_image().trim())) {
                                    String ads = candidateQuickJobsResponse.getAdsDetailArrayList().get(0).getSmall_image().trim();
                                    Picasso.with(activity).load(ads).error(R.drawable.profile_bg).placeholder(R.drawable.profile_bg).into(imgAdds);
                                } else {
                                    Picasso.with(activity).load(R.drawable.profile_bg).into(imgAdds);
                                }
                                if (!TextUtils.isEmpty(candidateQuickJobsResponse.getAdsDetailArrayList().get(0).getBig_image().trim())) {
                                    imgAdds.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                                            FullSizeImageFragment fullSizeImageFragment = new FullSizeImageFragment();
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("img", candidateQuickJobsResponse.getAdsDetailArrayList().get(0).getBig_image().trim());
                                            fullSizeImageFragment.setArguments(bundle1);
                                            fullSizeImageFragment.show(ft, Constants.FRG_FULLIMAGE);
                                        }
                                    });
                                }
                            }*/
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getDetailsArrayList().size() > 0) {
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
                                                adapter.setData(arrayList);
                                                if (arrayList.size() > 0) {
                                                    mSwipeRefreshLayout.setRefreshing(false);
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
                params.put("priority", String.valueOf(page));
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
        sendDataOnApi(page, searchTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
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
    public void onItemClick(int position, ManagementDetail detailsList) {
        OtherMemberProfile otherMemberProfile = new OtherMemberProfile();
        Bundle bundle = new Bundle();
        bundle.putString("Id", detailsList.getMemberId().trim());
        otherMemberProfile.setArguments(bundle);
        callFragmentMethod(otherMemberProfile, Constants.FRG_OTHER_MEMBER_PROFILE, fram);
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

                    sendDataOnApi(page, searchTitle);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}