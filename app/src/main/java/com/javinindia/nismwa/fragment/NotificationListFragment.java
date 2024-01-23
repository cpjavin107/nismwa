package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.Button;
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
import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventDetail;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventNewsRespons;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.ImageDetail;
import com.javinindia.nismwa.modelClasses.notificationparsing.NotificationDetail;
import com.javinindia.nismwa.modelClasses.notificationparsing.NotificationResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.EventAdapter;
import com.javinindia.nismwa.recyclerView.NotificationAdopter;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashis on 9/23/2017.
 */

public class NotificationListFragment extends BaseFragment implements NotificationAdopter.MyClickListener {
    AppCompatTextView txtTitle, txtLoading;
    ImageView imgBack, imgAdds;
    ProgressBar progressBar;
    private RecyclerView recyclerview;
    private NotificationAdopter adapter;
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.NOTIFICATION_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("noti", response);
                        int status = 0;
                        String msg;
                        final NotificationResponse candidateQuickJobsResponse = new NotificationResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getNotificationDetails().size() > 0) {
                                    arrayList = candidateQuickJobsResponse.getNotificationDetails();
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
        AppCompatTextView txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Notification");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        imgAdds = view.findViewById(R.id.imgAdds);
        mSwipeRefreshLayout = view.findViewById(R.id.offer_swipe_refresh_layout);
        progressBar = view.findViewById(R.id.progress);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerview.setLayoutManager(layoutManager);
        adapter = new NotificationAdopter(activity, 2);
        recyclerview.setAdapter(adapter);
        adapter.setMyClickListener(NotificationListFragment.this);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        txtLoading = view.findViewById(R.id.txtLoading);
        txtLoading.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        RelativeLayout rlHeader = view.findViewById(R.id.rlHeader);
        rlHeader.setVisibility(View.VISIBLE);

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

    @Override
    public void onEventClick(int position, NotificationDetail detailEventItem) {
        if (detailEventItem.getType().equalsIgnoreCase("news")) {
            NewsFragment newsFragment = new NewsFragment();
            callFragmentMethod(newsFragment, Constants.FRG_NEWS, R.id.containerMain);
        } else if (detailEventItem.getType().equalsIgnoreCase("inbox")) {
            MySuggestionFragment mySuggestionFragment = new MySuggestionFragment();
            callFragmentMethod(mySuggestionFragment, Constants.FRG_MY_SUGGESTION_LIST, R.id.containerMain);
        } else {
            dialogMethod(detailEventItem.getDescription().trim(), detailEventItem.getTitle().trim());
        }
    }

    @Override
    public void onLongClick(final int position, final NotificationDetail detailEventItem) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete this notification");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sendDataOnDeleteApi(detailEventItem.getId().trim(), position);
                    }
                });

        alertDialogBuilder.setNegativeButton("Take me back",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(Utility.getColor(activity, R.color.colorBackground));
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(Utility.getColor(activity, R.color.colorBackground));
    }


    private void sendDataOnDeleteApi(final String id, final int position) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.NOTIFICATION_DELETE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
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
                                    adapter.deleteItem(position);
                                    adapter.notifyDataSetChanged();
                                    showToastMethod("Your notification successfully deleted");
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
                params.put("memid", SharedPreferencesManager.getUserID(activity));
                params.put("id", id);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }


    private void dialogMethod(String thick, String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_popup_layout);

        AppCompatTextView text = dialog.findViewById(R.id.text_dialog);
        text.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        text.setText(Utility.fromHtml(thick));
        AppCompatTextView tit = dialog.findViewById(R.id.txtTit);
        tit.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        tit.setText(Utility.fromHtml(title));

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
