package com.javinindia.nismwa.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.javinindia.nismwa.modelClasses.DataModel;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.adsparsing.AdsResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.ManagementAdopter;
import com.javinindia.nismwa.recyclerView.RecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashis on 1/30/2018.
 */

public class SearchAlphabetFragment extends BaseFragment implements RecyclerViewAdapter.ItemListener {
    AppCompatTextView txtLoading, txtTitle;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private RecyclerViewAdapter adapter;
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
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();
    int page_position = 0;

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
        hitaddMethod();
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
            fram = R.id.containerMain;
            ownerId = SharedPreferencesManager.getUserID(activity);
        } else {
            fram = R.id.container;
            ownerId = "";
        }
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

    private void initialize(View view) {
        imgAdds = view.findViewById(R.id.imgAdds);
        imgBack = view.findViewById(R.id.imgBack);
        //imgBack.setOnClickListener(this);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Filter");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        arrayList = new ArrayList();
        mSwipeRefreshLayout = view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress);
        recyclerview = view.findViewById(R.id.recyclerview);

        arrayList = new ArrayList();
        arrayList.add(new DataModel("A"));
        arrayList.add(new DataModel("B"));
        arrayList.add(new DataModel("C"));
        arrayList.add(new DataModel("D"));
        arrayList.add(new DataModel("E"));
        arrayList.add(new DataModel("F"));
        arrayList.add(new DataModel("G"));
        arrayList.add(new DataModel("H"));
        arrayList.add(new DataModel("I"));
        arrayList.add(new DataModel("J"));
        arrayList.add(new DataModel("K"));
        arrayList.add(new DataModel("L"));
        arrayList.add(new DataModel("M"));
        arrayList.add(new DataModel("N"));
        arrayList.add(new DataModel("O"));
        arrayList.add(new DataModel("P"));
        arrayList.add(new DataModel("Q"));
        arrayList.add(new DataModel("R"));
        arrayList.add(new DataModel("S"));
        arrayList.add(new DataModel("T"));
        arrayList.add(new DataModel("U"));
        arrayList.add(new DataModel("V"));
        arrayList.add(new DataModel("W"));
        arrayList.add(new DataModel("X"));
        arrayList.add(new DataModel("Y"));
        arrayList.add(new DataModel("Z"));


        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 4);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new RecyclerViewAdapter(activity, arrayList, this);
        recyclerview.setAdapter(adapter);
        // adapter.setMyClickListener(SearchAlphabetFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());


        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLoading.setVisibility(View.GONE);

        RelativeLayout rlSearch = view.findViewById(R.id.rlSearch);
        rlSearch.setVisibility(View.GONE);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
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
    public void onItemClick(DataModel item) {
        // Toast.makeText(activity, item.text + " is clicked ", Toast.LENGTH_SHORT).show();
        ResultAlphabetFragment resultAlphabetFragment = new ResultAlphabetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search", item.text);
        resultAlphabetFragment.setArguments(bundle);
        callFragmentMethod(resultAlphabetFragment, Constants.FRG_ABOUT, fram);
    }
}
