package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.javinindia.nismwa.modelClasses.blooddonorparsing.DonorResponse;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.ImageDetail;
import com.javinindia.nismwa.modelClasses.notificationparsing.NotificationDetail;
import com.javinindia.nismwa.modelClasses.notificationparsing.NotificationResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.DonorAdopter;
import com.javinindia.nismwa.recyclerView.NotificationAdopter;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonnerListFragment extends BaseFragment implements DonorAdopter.MyClickListener {
    AppCompatTextView txtTitle, txtLoading;
    ImageView imgBack, imgAdds;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private DonorAdopter adapter;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList arrayList;
    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String ownerId;
    private ArrayList<ImageDetail> images;
    int page_position = 0;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        sendDataOnApi(page);
        Constants.COUNT = 0;
        SharedPreferencesManager.setCount(activity, 0);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            ownerId = "";
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void sendDataOnApi(final int page) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BLOOD_DONOR_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("noti", response);
                        int status = 0;
                        String msg;
                        final DonorResponse candidateQuickJobsResponse = new DonorResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getDonorDetailArrayList().size() > 0) {
                                    arrayList = candidateQuickJobsResponse.getDonorDetailArrayList();
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
                params.put("page", String.valueOf(page));
                params.put("id", ownerId);
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
        sendDataOnApi(page);
    }


    private void initialize(View view) {
        AppCompatTextView txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setText("Donors");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        ImageView imgBack =  view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        imgAdds =  view.findViewById(R.id.imgAdds);
        mSwipeRefreshLayout =  view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar =  view.findViewById(R.id.progress);
        recyclerview =  view.findViewById(R.id.recyclerview);
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new DonorAdopter(activity, 2);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(DonnerListFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        txtLoading =  view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        RelativeLayout rlHeader =  view.findViewById(R.id.rlHeader);
        rlHeader.setVisibility(View.GONE);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.notification_list_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

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

                    sendDataOnApi(page);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
