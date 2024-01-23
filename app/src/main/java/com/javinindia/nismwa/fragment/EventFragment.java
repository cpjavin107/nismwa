package com.javinindia.nismwa.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventDetail;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventNewsRespons;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.ImageDetail;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.EventAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 27-05-2017.
 */

public class EventFragment extends BaseFragment implements EventAdapter.MyClickListener {
    AppCompatTextView txtTitle, txtLoading;
    ImageView imgBack, imgAdds;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private EventAdapter adapter;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList arrayList;
    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String postId;
    private ArrayList<ImageDetail> images;
    private String ownerId;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();
    int page_position = 0;

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.EVENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        int status = 0;
                        String msg;
                        final EventNewsRespons candidateQuickJobsResponse = new EventNewsRespons();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                            if (candidateQuickJobsResponse.getAdsDetailArrayList().size() > 0) {
                                adsDetailArrayList = candidateQuickJobsResponse.getAdsDetailArrayList();
                                onResume();
                            }
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getEventDetailArrayList().size() > 0) {
                                    arrayList = candidateQuickJobsResponse.getEventDetailArrayList();
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
        sendDataOnApi(page);
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
        imgAdds = view.findViewById(R.id.imgAdds);
        mSwipeRefreshLayout = view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new EventAdapter(activity, 1);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(EventFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        RelativeLayout rlSearch = view.findViewById(R.id.rlSearch);
        RelativeLayout rlHeader = view.findViewById(R.id.rlHeader);
        rlSearch.setVisibility(View.GONE);
        rlHeader.setVisibility(View.GONE);
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
    public void onEventClick(int position, EventDetail detailEventItem) {
        if (detailEventItem.getImageDetailArrayList().size() > 0) {
            images = new ArrayList<>();
            images = detailEventItem.getImageDetailArrayList();
            Bundle bundle = new Bundle();
            bundle.putSerializable("images", images);
            bundle.putInt("position", 0);
            bundle.putString("text", detailEventItem.getDescription().trim());
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            PostImagesFragment newFragment = PostImagesFragment.newInstance().newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(ft, "slideshow");
        }
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
