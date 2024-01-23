package com.javinindia.nismwa.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.javinindia.nismwa.font.FontRobotoCondensedBoldSingleTonClass;
import com.javinindia.nismwa.font.FontRobotoCondensedRegularSingleTonClass;
import com.javinindia.nismwa.modelClasses.dealparsing.DealDetail;
import com.javinindia.nismwa.modelClasses.dealparsing.DealResponse;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberLoginResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.DealAdopter;
import com.javinindia.nismwa.recyclerView.MembersAdopter;
import com.javinindia.nismwa.utility.CheckConnection;
import com.javinindia.nismwa.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ashish on 26-04-2017.
 */

public class FilterMemberFragment extends BaseFragment implements View.OnClickListener, CheckConnectionFragment.OnCallBackInternetListener, DealAdopter.MyClickListener, MembersAdopter.MyClickListener {
    //, TextWatcher
    private RequestQueue requestQueue;
    AppCompatTextView txtTitle, txtLocationHd, txtLocation, txtFreshnessHd, txtSearchTitleHd, txtKeywordHd, txtApplyBtn, txtBlockHd, txtBlock, etSearchTitle, etKeyword;
    AppCompatEditText txtFreshness;
    ImageView imgBack;
    DealAdopter adopter;
    private MembersAdopter membersAdopter;

    ProgressBar progressBar;
    Dialog dialog;
    int width;
    int height;
    String day = "";

    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String search = "";
    ArrayList arrayList = new ArrayList();
    ArrayList arrayListMember = new ArrayList();

    int fram;
    private String ownerId;
    int option = 0;

    private OnCallBackQuickJobFilterListener backQuickJobFilterListener;


    public interface OnCallBackQuickJobFilterListener {
        void OnCallBackQuickJobFilter(String title, String location, String keyword, String day, String block);
    }

    public void setMyCallBackQuickJobFilterListener(OnCallBackQuickJobFilterListener callback) {
        this.backQuickJobFilterListener = callback;
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
        return view;
    }

    private void clickMethod(View view) {
        etSearchTitle.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        txtApplyBtn.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtFreshness.setOnClickListener(this);
        txtBlock.setOnClickListener(this);
        etKeyword.setOnClickListener(this);
    }

    private void initialize(View view) {
        imgBack = view.findViewById(R.id.imgBack);

        txtTitle = view.findViewById(R.id.txtTitle);
        txtTitle.setText("Filter Members");
        txtTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtLocationHd = view.findViewById(R.id.txtLocationHd);
        txtLocationHd.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtLocation = view.findViewById(R.id.txtLocation);
        txtLocation.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtFreshnessHd = view.findViewById(R.id.txtFreshnessHd);
        txtFreshnessHd.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSearchTitleHd = view.findViewById(R.id.txtSearchTitleHd);
        txtSearchTitleHd.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtKeywordHd = view.findViewById(R.id.txtKeywordHd);
        txtKeywordHd.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtApplyBtn = view.findViewById(R.id.txtApplyBtn);
        txtApplyBtn.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etKeyword = view.findViewById(R.id.etKeyword);
        etKeyword.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etSearchTitle = view.findViewById(R.id.etSearchTitle);
        etSearchTitle.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtFreshness = view.findViewById(R.id.etAddress);
        txtFreshness.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtBlockHd = view.findViewById(R.id.txtBlockHd);
        txtBlockHd.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtBlock = view.findViewById(R.id.txtBlock);
        txtBlock.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(activity);
        page = 1;
        startLimit = 0;
        switch (view.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtLocation:
                option = 0;
                dialogMethod("state");
                break;
            case R.id.etSearchTitle:
                option = 1;
                dialogMethod("name");
                break;
            case R.id.etKeyword:
                option = 2;
                dialogMethod("name");
                break;
            case R.id.txtApplyBtn:
                String blockId = "";
                String name = etSearchTitle.getText().toString().trim();
                String firm = etKeyword.getText().toString().trim();
                if (!TextUtils.isEmpty(firm)) {
                    firm = firm.replace(".", "");
                }
                String deal = txtLocation.getText().toString().trim();
                String address = txtFreshness.getText().toString().trim();
                String block = txtBlock.getText().toString().trim();
                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(firm) || !TextUtils.isEmpty(deal) || !TextUtils.isEmpty(address) || !TextUtils.isEmpty(block)) {
                    if (block.equalsIgnoreCase("X")) {
                        blockId = "1";
                    } else if (block.equalsIgnoreCase("Y")) {
                        blockId = "2";
                    } else if (block.equalsIgnoreCase("Z")) {
                        blockId = "3";
                    }
                    //ownerName = "", address = "", deal = "", firmName = "", blockId = ""
                    MemberListFragment societyMembersFragment = new MemberListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("ownerName", name);
                    bundle.putString("address", address);
                    bundle.putString("deal", deal);
                    bundle.putString("firmName", firm);
                    bundle.putString("blockId", blockId);
                    societyMembersFragment.setArguments(bundle);
                    methodOpenFragment(societyMembersFragment, Constants.FRG_MEMBER_LIST, fram);
                    etSearchTitle.setText("");
                    etKeyword.setText("");
                    txtLocation.setText("");
                    txtFreshness.setText("");
                    txtBlock.setText("");
                    // backQuickJobFilterListener.OnCallBackQuickJobFilter(name, firm, deal, address, blockId);
                } else {
                    Toast.makeText(activity, "You have not entered any search entity.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.txtBlock:
                methodBlockPopup();
                break;
            default:
                break;
        }
    }

    private void methodOpenFragment(BaseFragment baseFragment, String frgMemberList, int containerMain) {
        if (containerMain == R.id.containerMain) {
            callFragmentMethod(baseFragment, frgMemberList, R.id.containerMain);
        } else {
            callFragmentMethod(baseFragment, frgMemberList, R.id.container);
        }
    }

    private void dialogMethod(String tt) {
        String test = "";
        screenSizeMethod();
        // custom_dialog_layout dialog
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout.LayoutParams dialogParams = new LinearLayout.LayoutParams(
                width - 100, ViewGroup.LayoutParams.WRAP_CONTENT);//set height(300) and width(match_parent) here, ie (width,height)
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        View dislogView = inflater
                .inflate(R.layout.custom_dialog_layout, null);
        dialog.setContentView(dislogView, dialogParams);
        progressBar = dialog.findViewById(R.id.progress);
        RecyclerView recyclerview = dialog.findViewById(R.id.recyclerViewDialog);
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        AppCompatTextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());


        if (tt.equalsIgnoreCase("name")) {
            if (option == 1) {
                txtTitle.setText("Select Member");
                membersAdopter = new MembersAdopter(activity, 0);
            } else {
                txtTitle.setText("Select Firm");
                membersAdopter = new MembersAdopter(activity, 1);
            }
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(membersAdopter);
            membersAdopter.setMyClickListener(this);
            page = 1;
            startLimit = 0;
            search = "";
            Log.e("limit", page + " limit " + startLimit + "search " + search);
            SearchMember(search, page);
            AppCompatEditText etDialog = (AppCompatEditText) dialog.findViewById(R.id.etDialog);
            etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
            etDialog.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (CheckConnection.haveNetworkConnection(activity)) {
                        String text = s.toString().toLowerCase(Locale.getDefault());
                        if (text.length() > 2) {
                            sendRequestMemberListFeed(text);
                        } else if (text.length() == 0) {
                            sendRequestMemberListFeed(text);
                        }

                    } else {
                        methodCallCheckInternet();
                    }
                }
            });

        } else if (tt.equalsIgnoreCase("state")) {
            txtTitle.setText("Select Category");
            adopter = new DealAdopter(activity);
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(adopter);
            adopter.setMyClickListener(this);
            page = 1;
            startLimit = 0;
            search = "";
            Log.e("limit", page + " limit " + startLimit + "search " + search);
            sendRequestStateListFeed(search, page);
            AppCompatEditText etMember = (AppCompatEditText) dialog.findViewById(R.id.etDialog);
            etMember.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
            etMember.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (CheckConnection.haveNetworkConnection(activity)) {
                        String text = s.toString().toLowerCase(Locale.getDefault());
                        if (text.length() > 2) {
                            sendRequestListFeed(text);
                        } else if (text.length() == 0) {
                            sendRequestListFeed(text);
                        }

                    } else {
                        methodCallCheckInternet();
                    }
                }
            });
        }

        dialog.show();
    }


    private void SearchMember(final String search, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBERS_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        int status = 0;
                        String msg;
                        final MemberLoginResponse candidateQuickJobsResponse = new MemberLoginResponse();
                        candidateQuickJobsResponse.responseParseMethod(response);
                        try {
                            status = candidateQuickJobsResponse.getStatus();
                            msg = candidateQuickJobsResponse.getMsg().trim();
                            if (status == 1) {
                                if (candidateQuickJobsResponse.getDetailsArrayList().size() > 0) {
                                    arrayListMember = candidateQuickJobsResponse.getDetailsArrayList();
                                    if (arrayListMember.size() > 0) {
                                        if (page == 1) {
                                            membersAdopter.setData(arrayListMember);
                                            membersAdopter.notifyDataSetChanged();
                                        } else {
                                            if (membersAdopter.getData() != null && membersAdopter.getData().size() > 0) {
                                                membersAdopter.getData().addAll(arrayListMember);
                                                membersAdopter.notifyDataSetChanged();
                                            } else {
                                                membersAdopter.setData(arrayListMember);
                                                membersAdopter.notifyDataSetChanged();
                                            }
                                        }

                                    } else {

                                    }

                                }

                            } else {

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
                if (option == 1) {
                    params.put("ownerName", search);
                    params.put("firmName", "");
                } else {
                    params.put("ownerName", "");
                    params.put("firmName", search);
                }
                params.put("address", "");
                params.put("deal", "");
                params.put("block", "");
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
    public void onItemCatClick(int position, DealDetail model) {
        page = 1;
        startLimit = 0;
        txtLocation.setText(model.getDealsName().trim());
        dialog.dismiss();
    }

    @Override
    public void onItemClick(int position, MemberDetail detailsList) {
        page = 1;
        startLimit = 0;
        if (option == 1) {
            etSearchTitle.setText(detailsList.getName().trim());
            etKeyword.setText(detailsList.getFirm_name().trim());
        } else {
            etSearchTitle.setText("");
            etKeyword.setText(detailsList.getFirm_name().trim());
        }
        dialog.dismiss();
    }

    private void sendRequestStateListFeed(final String test, final int page) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DEAL_TYPE_MASTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        DealResponse responseparsing = new DealResponse();
                        responseparsing.responseParseMethod(response);
                        try {
                            if (response.length() != 0) {
                                int status = responseparsing.getStatus();
                                if (status == 1) {
                                    arrayList = responseparsing.getDealDetailArrayList();
                                    if (arrayList.size() > 0) {
                                        if (page == 1) {
                                            adopter.setData(arrayList);
                                            adopter.notifyDataSetChanged();
                                        } else {
                                            if (adopter.getData() != null && adopter.getData().size() > 0) {
                                                adopter.getData().addAll(arrayList);
                                                adopter.notifyDataSetChanged();
                                            } else {
                                                adopter.setData(arrayList);
                                                adopter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                } else {
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
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("searchTitle", test);
                params.put("page", String.valueOf(page));
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
    protected int getFragmentLayout() {
        return R.layout.filter_quick_job_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {
    }


    /* @Override
     public void beforeTextChanged(CharSequence s, int start, int count, int after) {

     }

     @Override
     public void onTextChanged(CharSequence s, int start, int before, int count) {

     }

     @Override
     public void afterTextChanged(Editable editable) {
         if (CheckConnection.haveNetworkConnection(activity)) {
             String text = editable.toString().toLowerCase(Locale.getDefault());
             if (text.length() > 2) {
                 sendRequestListFeed(text);
             } else if (text.length() == 0) {
                 sendRequestListFeed(text);
             }

         } else {
             methodCallCheckInternet();
         }
     }
 */
    private void sendRequestListFeed(String text) {
        arrayList.removeAll(arrayList);
        adopter.setData(null);
        adopter.notifyDataSetChanged();
        search = text;
        page = 1;
        startLimit = 0;
        sendRequestStateListFeed(search, page);
    }

    private void sendRequestMemberListFeed(String text) {
        arrayListMember.removeAll(arrayListMember);
        membersAdopter.setData(null);
        membersAdopter.notifyDataSetChanged();
        search = text;
        page = 1;
        startLimit = 0;
        SearchMember(search, page);
    }

    public void methodCallCheckInternet() {
        CheckConnectionFragment fragment = new CheckConnectionFragment();
        fragment.setMyCallBackInternetListener(this);
        callFragmentMethod(fragment, Constants.FRG_CHECK_INTERNET, R.id.containerMain);
    }

    @Override
    public void OnCallBackInternet() {
        activity.onBackPressed();
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
                    if (option == 0) {
                        sendRequestStateListFeed(search, page);
                    } else if (option == 1) {
                        SearchMember(search, page);
                    } else if (option == 2) {
                        SearchMember(search, page);
                    }

                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    private void methodBlockPopup() {
        final String TypeArray[] = {"X", "Y", "Z"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                txtBlock.setText(TypeArray[item].trim());
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

}
