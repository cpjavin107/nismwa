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
import com.javinindia.nismwa.modelClasses.adsparsing.AdsResponse;
import com.javinindia.nismwa.modelClasses.membertypeparsing.MemberTypeResponse;
import com.javinindia.nismwa.modelClasses.membertypeparsing.TypeDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsfullMasterResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.CalenderAdopter;
import com.javinindia.nismwa.recyclerView.ComplaintCategoryAdopter;
import com.javinindia.nismwa.recyclerView.UsefullAdopter;
import com.javinindia.nismwa.recyclerView.UsefullMasterAdopter;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 13-04-2017.
 */

public class HomePageFragment extends BaseFragment implements View.OnClickListener, ComplaintCategoryAdopter.MyClickListener, UsefullMasterAdopter.MyClickListener, FilterMemberFragment.OnCallBackQuickJobFilterListener {
    private RequestQueue requestQueue;
    RelativeLayout rlMemberList, rlMemberCommittee, rlEvent, rlCalculator, rlComplaints, rlBroker, rlCalender, rlUseful, rlSearchAlpha, rlAbout, rlBloodDonate, rlOther;
    AppCompatTextView txtMemberList, txtMemberCommittee, txtEvent, txtCalculator, txtComplaints, txtBills, txtUseful, txtCalender, txtSearchAlpha, txtBloodDonate;
    ImageView imgAdds;
    ProgressBar progressBar;
    Dialog dialog;
    int width;
    int height;
    ComplaintCategoryAdopter adopterCategory;
    UsefullMasterAdopter usefullAdopter;
    int fram;
    private String ownerId;
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
        screenSizeMethod();
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

    private void clickMethod(View view) {
        rlMemberList.setOnClickListener(this);
        rlMemberCommittee.setOnClickListener(this);
        rlEvent.setOnClickListener(this);
        rlCalculator.setOnClickListener(this);
        rlComplaints.setOnClickListener(this);
        rlBroker.setOnClickListener(this);
        rlCalender.setOnClickListener(this);
        rlUseful.setOnClickListener(this);
        rlSearchAlpha.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        rlBloodDonate.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        imgAdds = view.findViewById(R.id.imgAdds);
        rlMemberList = view.findViewById(R.id.rlMemberList);
        rlMemberCommittee = view.findViewById(R.id.rlMemberCommittee);
        rlEvent = view.findViewById(R.id.rlEvent);
        rlCalculator = view.findViewById(R.id.rlCalculator);
        rlComplaints = view.findViewById(R.id.rlComplaints);
        rlBroker = view.findViewById(R.id.rlBroker);
        rlUseful = view.findViewById(R.id.rlUseful);
        rlCalender = view.findViewById(R.id.rlCalender);
        rlSearchAlpha = view.findViewById(R.id.rlSearchAlpha);
        rlAbout = view.findViewById(R.id.rlAbout);
        rlBloodDonate = view.findViewById(R.id.rlBloodDonate);
        rlOther = view.findViewById(R.id.rlOther);

        txtMemberList = view.findViewById(R.id.txtMemberList);
        txtMemberList.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtMemberCommittee = view.findViewById(R.id.txtMemberCommittee);
        txtMemberCommittee.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtEvent = view.findViewById(R.id.txtEvent);
        txtEvent.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtCalculator = view.findViewById(R.id.txtCalculator);
        txtCalculator.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtComplaints = view.findViewById(R.id.txtSuggestion);
        txtComplaints.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtBills = view.findViewById(R.id.txtBroker);
        txtBills.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtUseful = view.findViewById(R.id.txtUseful);
        txtUseful.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtCalender = view.findViewById(R.id.txtCalender);
        txtCalender.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtSearchAlpha = view.findViewById(R.id.txtSearchAlpha);
        txtSearchAlpha.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtBloodDonate = view.findViewById(R.id.txtBloodDonate);
        txtBloodDonate.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());


        RelativeLayout rlMemberList = view.findViewById(R.id.rlMemberList);
        ViewGroup.LayoutParams params = rlMemberList.getLayoutParams();
        params.height = width / 2 - 14;
        params.width = width / 2;
        rlMemberList.setLayoutParams(params);

        rlMemberCommittee.setLayoutParams(params);
        rlEvent.setLayoutParams(params);
        rlCalculator.setLayoutParams(params);
        rlComplaints.setLayoutParams(params);
        rlBroker.setLayoutParams(params);
        rlUseful.setLayoutParams(params);
        rlCalender.setLayoutParams(params);
        rlSearchAlpha.setLayoutParams(params);
        rlAbout.setLayoutParams(params);
        rlBloodDonate.setLayoutParams(params);
        rlOther.setLayoutParams(params);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.home_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlMemberList:
                /*MemberListFragment societyMembersFragment = new MemberListFragment();
                methodOpenFragment(societyMembersFragment, Constants.FRG_MEMBER_LIST, fram);*/
                FilterMemberFragment filterQuickJobsFragment = new FilterMemberFragment();
                filterQuickJobsFragment.setMyCallBackQuickJobFilterListener(this);
                callFragmentMethod(filterQuickJobsFragment, Constants.FRG_FILTER, fram);
                break;
            case R.id.rlMemberCommittee:
                EventTabFragment managementFragment = new EventTabFragment();
                callFragmentMethod(managementFragment, Constants.FRG_MANAGEMENT_ONE_LIST, fram);
                break;
            case R.id.rlEvent:
                NewsFragment eventFragment = new NewsFragment();
                callFragmentMethod(eventFragment, Constants.FRG_NEWS, fram);
                break;
            case R.id.rlBroker:
                if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
                    dialogMethod("category");
                } else {
                    showToastMethod("This feature is for members only.");
                }
                break;
            case R.id.rlComplaints:
                if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
                    AddComplaintFragment complaintsFragment = new AddComplaintFragment();
                    callFragmentMethod(complaintsFragment, Constants.FRG_ADD_COMPLAINT, fram);
                } else {
                    showToastMethod("This feature is for members only.");
                }
                break;
            case R.id.rlCalculator:
                CalculatorFragment calculatorFragment = new CalculatorFragment();
                callFragmentMethod(calculatorFragment, Constants.FRG_CALCULATOR, fram);
                break;
            case R.id.rlCalender:
                CalenderFragment calenderFragment = new CalenderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("year", "2017");
                calenderFragment.setArguments(bundle);
                callFragmentMethod(calenderFragment, Constants.FRG_CALENDER, fram);
                //alertCalender();
                break;
            case R.id.rlUseful:
                dialogMethod("useful");
                break;
            case R.id.rlSearchAlpha:
                SearchAlphabetFragment searchAlphabetFragment = new SearchAlphabetFragment();
                callFragmentMethod(searchAlphabetFragment, Constants.FRG_CALCULATOR, fram);
                break;
            case R.id.rlAbout:
                AboutFragment aboutFragment = new AboutFragment();
                callFragmentMethod(aboutFragment, Constants.FRG_ABOUT, fram);
                break;
            case R.id.rlBloodDonate:
                if (!TextUtils.isEmpty(SharedPreferencesManager.getUserID(activity)) && !SharedPreferencesManager.getUserID(activity).equalsIgnoreCase("null")) {
                    BloodTabFragment bloodTabFragment = new BloodTabFragment();
                    callFragmentMethod(bloodTabFragment, Constants.FRG_NEWS, fram);
                } else {
                    showToastMethod("This feature is for members only.");
                }

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

    private void alertCalender() {
        final String TypeArray[] = {"2017", "2018"};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select Year");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                CalenderFragment calenderFragment = new CalenderFragment();
                Bundle bundle = new Bundle();
                bundle.putString("year", TypeArray[item]);
                calenderFragment.setArguments(bundle);
                callFragmentMethod(calenderFragment, Constants.FRG_CALENDER, fram);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
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
        progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        RecyclerView recyclerview = (RecyclerView) dialog.findViewById(R.id.recyclerViewDialog);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        AppCompatTextView txtTitle = (AppCompatTextView) dialog.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatEditText etDialog = (AppCompatEditText) dialog.findViewById(R.id.etDialog);
        etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        if (tt.equalsIgnoreCase("useful")) {
            txtTitle.setText("Select Useful Details");
            etDialog.setVisibility(View.GONE);
            usefullAdopter = new UsefullMasterAdopter(activity);
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(usefullAdopter);
            usefullAdopter.setMyClickListener(this);
            sendRequestUsefullListFeed(test);
        } else if (tt.equalsIgnoreCase("category")) {
            txtTitle.setText("Select Category");
            etDialog.setVisibility(View.GONE);
            adopterCategory = new ComplaintCategoryAdopter(activity);
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(adopterCategory);
            adopterCategory.setMyClickListener(this);
            sendRequestStateListFeed(test);
        }
        dialog.show();
    }

    private void sendRequestUsefullListFeed(String test) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_USEFUL_MASTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        UsfullMasterResponse responseparsing = new UsfullMasterResponse();
                        responseparsing.responseParseMethod(response);
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                ArrayList arrayList = responseparsing.getUsefullMasterDetailArrayList();
                                if (arrayList.size() > 0) {
                                    usefullAdopter.setData(arrayList);
                                    usefullAdopter.notifyDataSetChanged();
                                }
                            } else {
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
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void sendRequestStateListFeed(final String test) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_TYPE_MASTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        MemberTypeResponse responseparsing = new MemberTypeResponse();
                        responseparsing.responseParseMethod(response);
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                ArrayList arrayList = responseparsing.getTypeDetailArrayList();
                                if (arrayList.size() > 0) {
                                    adopterCategory.setData(arrayList);
                                    adopterCategory.notifyDataSetChanged();
                                }
                            } else {
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
    public void onItemCatClick(int position, TypeDetail model) {
        dialog.dismiss();
        Bundle bundle = new Bundle();
        bundle.putString("id", model.getId().trim());
        bundle.putString("type", model.getType_name().trim());
        OtherMemerListFragment otherMemerListFragment = new OtherMemerListFragment();
        otherMemerListFragment.setArguments(bundle);
        callFragmentMethod(otherMemerListFragment, Constants.FRG_OTHER_MEMBER_LIST, fram);
    }

    @Override
    public void onItemUseClick(int position, UsefullMasterDetail model) {
        UsefullFragment usefullFragment = new UsefullFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", model.getId().trim());
        bundle.putString("name", model.getuName().trim());
        usefullFragment.setArguments(bundle);
        callFragmentMethod(usefullFragment, Constants.FRG_USEFUL, fram);
        dialog.dismiss();
    }

    @Override
    public void OnCallBackQuickJobFilter(String title, String location, String keyword, String day, String block) {

    }
}
