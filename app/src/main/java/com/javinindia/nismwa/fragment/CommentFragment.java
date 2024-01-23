package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.javinindia.nismwa.modelClasses.commentparsing.CommentDetail;
import com.javinindia.nismwa.modelClasses.commentparsing.CommentResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.CommentAdapter;
import com.javinindia.nismwa.utility.Utility;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashish on 28-06-2017.
 */

public class CommentFragment extends BaseFragment implements View.OnClickListener, CommentAdapter.MyClickListener {

    private RequestQueue requestQueue;
    RecyclerView recylerComment;
    EditText etComment;
    private ArrayList list;
    private int page = 1;
    private String pid, position;
    CommentAdapter mAdapter;
    private int startLimit = 0;
    private int countLimit = 10;
    private boolean loading = true;
    // private SwipeRefreshLayout mSwipeRefreshLayout;
    AppCompatTextView txtNoData, txtTitle, imgReply;
    Dialog dialog;
    int width;
    int height;

    private OnCallBackCommentListener onCallBackCommentListener;


    public interface OnCallBackCommentListener {
        void OnCallBackComment(String position, String trim);
    }

    public void setMyCallBackCommentListener(OnCallBackCommentListener callback) {
        this.onCallBackCommentListener = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        if (getArguments() != null) {
            pid = getArguments().getString("pid");
            position = getArguments().getString("position");
        }
        initializeMethod(view);
        sendRequestOnCommentFeed(page);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            this.getArguments().clear();
        }
    }

    private void sendRequestOnCommentFeed(final int page) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.COMMENT_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status = 0;
                        CommentResponse gossipResponseParsing = new CommentResponse();
                        gossipResponseParsing.responseParseMethod(response);
                        status = gossipResponseParsing.getStatus();
                        if (status == 1) {
                            onCallBackCommentListener.OnCallBackComment(position, gossipResponseParsing.getCommentCount().trim());
                            list = gossipResponseParsing.getDetailsArrayList();
                            if (list != null && list.size() > 0) {
                                txtNoData.setVisibility(View.GONE);
                                if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
                                    mAdapter.getData().addAll(list);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    mAdapter.setData(list);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (startLimit > 0) {
                                    txtNoData.setVisibility(View.GONE);
                                } else {
                                    txtNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("postId", pid);
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                // params.put("societyId", SharedPreferencesManager.getSocietyId(activity));
                params.put("page", String.valueOf(page));
                params.put("type", "wall");
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
        sendRequestOnCommentFeed(page);
    }


    private void initializeMethod(View view) {
        txtTitle =  view.findViewById(R.id.txtTitle);
        txtTitle.setText("Comments");
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        ImageView imgBack = view.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(this);
        mAdapter = new CommentAdapter(activity);
        mAdapter.setMyClickListener(CommentFragment.this);
        recylerComment =  view.findViewById(R.id.recyclerView_comment);
        recylerComment.setAdapter(mAdapter);
        etComment =  view.findViewById(R.id.et_Comment);
        etComment.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        imgReply =  view.findViewById(R.id.img_reply);
        imgReply.setOnClickListener(this);
        LinearLayoutManager layoutMangerDestination
                = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        layoutMangerDestination.setReverseLayout(true);
        recylerComment.setLayoutManager(layoutMangerDestination);
        recylerComment.addOnScrollListener(new gossipScrollListener());
        txtNoData =  view.findViewById(R.id.txtNoData);
        txtNoData.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.comment_list_layout;
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
            case R.id.img_reply:
                if (!TextUtils.isEmpty(etComment.getText().toString().trim())) {
                    sendRequestOnInsertCommentFeed();
                } else {
                    showToastMethod("you have not entered any comment");
                }
                break;
            case R.id.imgBack:
                Utility.hideKeyboard(activity);
                activity.onBackPressed();
                break;
        }
    }


    private void sendRequestOnInsertCommentFeed() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.COMMENT_INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status = 0;
                        loading.dismiss();
                        CommentResponse gossipResponseParsing = new CommentResponse();
                        gossipResponseParsing.responseParseMethod(response);
                        try {

                        } catch (Exception e) {
                            status = gossipResponseParsing.getStatus();
                            if (status == 1) {
                                onCallBackCommentListener.OnCallBackComment(position, gossipResponseParsing.getCommentCount().trim());
                                etComment.setText(" ");
                                list = gossipResponseParsing.getDetailsArrayList();
                                if (list != null && list.size() > 0) {
                                    txtNoData.setVisibility(View.GONE);
                                    if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
                                        mAdapter.addItem(0, gossipResponseParsing.getDetailsArrayList().get(0));
                                    } else {
                                        mAdapter.setData(list);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                showToastMethod(gossipResponseParsing.getMsg().trim());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                //   params.put("societyId", SharedPreferencesManager.getSocietyId(activity));
                params.put("postId", pid);
                params.put("comment", etComment.getText().toString());
                params.put("type", "wall");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onLongItemClick(final int pos, CommentDetail gossipDetailList) {
        final String commentId = gossipDetailList.getId().trim();
        final String comment = gossipDetailList.getDescription().trim();
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure you want to edit comment?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dialogMethod(commentId, comment, pos);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void dialogMethod(final String commentId, final String comment, final int pos) {
        screenSizeMethod();
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        LinearLayout.LayoutParams dialogParams = new LinearLayout.LayoutParams(
                width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);//set height(300) and width(match_parent) here, ie (width,height)
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View dislogView = inflater
                .inflate(R.layout.custom_dialog_comment_layout, null);
        dialog.setContentView(dislogView, dialogParams);

        AppCompatTextView txtTitle = (AppCompatTextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtCancel = (AppCompatTextView) dialog.findViewById(R.id.txtCancel);
        txtCancel.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtUpdate = (AppCompatTextView) dialog.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        final AppCompatEditText etDialog = (AppCompatEditText) dialog.findViewById(R.id.etDialog);
        etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDialog.setText(comment);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etDialog.getText().toString().trim())) {
                    methodUpdateComment(commentId, etDialog.getText().toString().trim(), pos);
                } else {
                    showToastMethod("you have not entered any comment");
                }
            }
        });
        dialog.show();
    }

    private void methodUpdateComment(final String commentId, final String comment, final int pos) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.COMMENT_EDIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int status = 0;
                        loading.dismiss();
                        CommentResponse gossipResponseParsing = new CommentResponse();
                        gossipResponseParsing.responseParseMethod(response);
                        try {
                            status = gossipResponseParsing.getStatus();
                            if (status == 1) {
                                onCallBackCommentListener.OnCallBackComment(position, gossipResponseParsing.getCommentCount().trim());
                                List list = mAdapter.getData();
                                CommentDetail detail = (CommentDetail) list.get(pos);
                                detail.setDescription(gossipResponseParsing.getDetailsArrayList().get(0).getDescription().trim());
                                mAdapter.notifyItemChanged(pos);
                                dialog.dismiss();
                            } else {
                                showToastMethod(gossipResponseParsing.getMsg().trim());
                            }
                        }catch (Exception e){
                           showToastMethod(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                //params.put("societyId", SharedPreferencesManager.getSocietyId(activity));
                params.put("postId", pid);
                params.put("comment", comment);
                params.put("commentId", commentId);
                params.put("type", "wall");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void screenSizeMethod() {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 13) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            height = display.getWidth();
        }
    }

    @Override
    public void onUserPicClick(int position, CommentDetail detailsList) {
        String memberId = detailsList.getMemberId().trim();
        String type = "all";
        if (memberId.equals(SharedPreferencesManager.getUserID(activity))) {
            type = "me";
        } else {
            type = "all";
        }
        OtherMemberProfile otherMemberProfile = new OtherMemberProfile();
        Bundle bundle = new Bundle();
        bundle.putString("Id", detailsList.getMemberId().trim());
        otherMemberProfile.setArguments(bundle);
        callFragmentMethod(otherMemberProfile, Constants.FRG_OTHER_MEMBER_PROFILE, R.id.containerMain);
    }


    public class gossipScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 20) ? totalItemCount / 2 : 20;
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

                    sendRequestOnCommentFeed(page);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}
