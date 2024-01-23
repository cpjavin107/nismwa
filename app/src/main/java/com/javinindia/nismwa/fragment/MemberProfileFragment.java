package com.javinindia.nismwa.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.TextView;
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
import com.javinindia.nismwa.modelClasses.dealparsing.DealDetail;
import com.javinindia.nismwa.modelClasses.dealparsing.DealResponse;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberLoginResponse;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.DealAdopter;
import com.javinindia.nismwa.utility.CheckConnection;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ashish on 03-07-2017.
 */

public class MemberProfileFragment extends BaseFragment implements View.OnClickListener, DealAdopter.MyClickListener, TextWatcher, CheckConnectionFragment.OnCallBackInternetListener, EditProfileFragment.OnCallBackEditProfileViewListener {
    private RequestQueue requestQueue;
    private Uri mCropImageUri;
    Bitmap photo = null;
    String pic = "", name = "";
    ImageView imgProfile, imgWhite, imgAdds, imgBack;
    LinearLayout llVehicle;
    AppCompatTextView imgEdit, txtCompany, txtName, txtAddress, txtPartnerName, txtMobile, txtMobileHead, txtOfficeHead, txtOffice, txtResidence, txtResidenceHead, txtEmail, txtEmailHead, txtAddContact, txtDealHead, txtDealEdit, txtDeal, txtShareContact, txtWebHead, txtWeb, txtOffice2Head, txtOffice2;

    ProgressBar progressBar;
    Dialog dialog;
    int width;
    int height;
    DealAdopter adopter;
    String topAction;

    private int page = 1;
    private int startLimit = 0;
    private boolean loading = true;
    private String search = "";
    ArrayList arrayList;
    int page_position = 0;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();

    private OnCallBackProfileViewListener backProfileViewListener;

    public interface OnCallBackProfileViewListener {
        void OnCallBackProfileView();
    }

    public void setMyCallBackProfileViewListener(OnCallBackProfileViewListener callback) {
        this.backProfileViewListener = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        hitApi(SharedPreferencesManager.getUserID(activity));
        return view;
    }


    private void hitApi(final String userID) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Log.e("profile", response);
                        responseImplement(response);
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
                params.put("memberId", userID);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void responseImplement(String response) {
        String userid = " ", msg = " ", password = null, mobile = "", office = "", office2 = "", email = "", pic = "", firstName = "", empType = "", firmName = "", deal1 = "", deal2 = "", deal3 = "", deal4 = "", deal = "", address = "";
        final ArrayList<String> data = new ArrayList<>();
        final ArrayList<String> partner = new ArrayList<>();
        final MemberLoginResponse responseparsing = new MemberLoginResponse();
        responseparsing.responseParseMethod(response);
        try {
            if (responseparsing.getAdsDetailArrayList().size() > 0) {
                adsDetailArrayList = responseparsing.getAdsDetailArrayList();
                onResume();
            }
            if (responseparsing.getStatus() == 1) {
                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getMemberId().trim())) {
                    userid = responseparsing.getDetailsArrayList().get(0).getMemberId().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getType().trim())) {
                    empType = responseparsing.getDetailsArrayList().get(0).getType().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getName().trim()) && !responseparsing.getDetailsArrayList().get(0).getName().equalsIgnoreCase("null")) {
                    firstName = responseparsing.getDetailsArrayList().get(0).getName().trim();
                    name = responseparsing.getDetailsArrayList().get(0).getName().trim();
                    txtName.setText(Utility.fromHtml(name));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getAddress().trim()) && !responseparsing.getDetailsArrayList().get(0).getAddress().equalsIgnoreCase("null")) {
                    address = responseparsing.getDetailsArrayList().get(0).getAddress().trim();
                    // firstName = "<font>(" + address + ")<br><b>" + firstName + "</b></font>";
                    txtAddress.setText(Utility.fromHtml("(" + address + ")"));
                }

                if (responseparsing.getDetailsArrayList().get(0).getMemberDetailArrayList().size() > 0) {
                    int siz = responseparsing.getDetailsArrayList().get(0).getMemberDetailArrayList().size();
                    for (int i = 0; i < siz; i++) {
                        partner.add(responseparsing.getDetailsArrayList().get(0).getMemberDetailArrayList().get(i).getName().trim());
                    }

                    String str = Arrays.toString(partner.toArray());
                    String test = str.replaceAll("[\\[\\](){}]", "");
                    if (partner.size() != 1) {
                        int position = test.lastIndexOf(",");
                        StringBuilder myName = new StringBuilder(test);
                        myName.setCharAt(position, ' ');
                        myName.insert(position + 1, '&');
                        test = myName.toString();
                        txtPartnerName.setText(Utility.fromHtml(test));
                    } else {
                        test = test;
                        txtPartnerName.setText(Utility.fromHtml(test));
                    }
                }


                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getMobileNumber().trim()) && !responseparsing.getDetailsArrayList().get(0).getMobileNumber().equalsIgnoreCase("null")) {
                    mobile = responseparsing.getDetailsArrayList().get(0).getMobileNumber().trim();
                    txtMobile.setText(Utility.fromHtml(mobile));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().trim()) && !responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().equalsIgnoreCase("null")) {
                    office = responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().trim();
                    if (office.length() >= 10) {
                        office = office;
                    } else {
                        office = "011" + office;
                    }
                    txtOffice.setText(Utility.fromHtml(office));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getLandlineNumber().trim()) && !responseparsing.getDetailsArrayList().get(0).getLandlineNumber().equalsIgnoreCase("null")) {
                    office2 = responseparsing.getDetailsArrayList().get(0).getLandlineNumber().trim();
                    if (office2.length() >= 10) {
                        office2 = office2;
                    } else {
                        office2 = "011" + office2;
                    }
                    txtOffice2.setText(Utility.fromHtml(office2));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getResidence().trim()) && !responseparsing.getDetailsArrayList().get(0).getResidence().equalsIgnoreCase("null")) {
                    String res = responseparsing.getDetailsArrayList().get(0).getResidence().trim();
                    if (res.length() >= 10) {
                        res = res;
                    } else {
                        res = "011" + res;
                    }
                    txtResidence.setText(Utility.fromHtml(res));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getEmail().trim()) && !responseparsing.getDetailsArrayList().get(0).getEmail().equalsIgnoreCase("null")) {
                    email = responseparsing.getDetailsArrayList().get(0).getEmail().trim();
                    txtEmail.setText(Utility.fromHtml(email));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getImageUrl().trim())) {
                    pic = responseparsing.getDetailsArrayList().get(0).getImageUrl().trim();
                    Picasso.with(activity).load(pic).error(R.drawable.profile_icon).placeholder(R.drawable.profile_icon).transform(new CircleTransform()).into(imgProfile);
                } else {
                    Picasso.with(activity).load(R.drawable.profile_icon).transform(new CircleTransform()).into(imgProfile);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getPassword().trim())) {
                    password = responseparsing.getDetailsArrayList().get(0).getPassword().trim();
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getFirm_name().trim()) && !responseparsing.getDetailsArrayList().get(0).getFirm_name().equalsIgnoreCase("null")) {
                    firmName = responseparsing.getDetailsArrayList().get(0).getFirm_name().trim();
                    txtCompany.setText(Utility.fromHtml(firmName));
                } else {
                    txtCompany.setText(Utility.fromHtml("Firm name not found"));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getWebsite().trim()) && !responseparsing.getDetailsArrayList().get(0).getWebsite().equalsIgnoreCase("null")) {
                    String web = responseparsing.getDetailsArrayList().get(0).getWebsite().trim();
                    txtWeb.setText(Utility.fromHtml(web));
                } else {
                    txtWeb.setText(Utility.fromHtml(""));
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim().equalsIgnoreCase("null")) {
                    deal1 = responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim();
                    data.add(deal1);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim().equalsIgnoreCase("null")) {
                    deal2 = responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim();
                    data.add(deal2);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_3().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_3().trim().equalsIgnoreCase("null")) {
                    deal3 = responseparsing.getDetailsArrayList().get(0).getDeals_in_3().trim();
                    data.add(deal3);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_4().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_4().trim().equalsIgnoreCase("null")) {
                    deal4 = responseparsing.getDetailsArrayList().get(0).getDeals_in_4().trim();
                    data.add(deal4);
                }


                if (data.size() > 0) {
                    String str = Arrays.toString(data.toArray());
                    String test = str.replaceAll("[\\[\\](){}]", "");
                    if (data.size() != 1) {
                        int position = test.lastIndexOf(",");
                        StringBuilder myName = new StringBuilder(test);
                        myName.setCharAt(position, ' ');
                        myName.insert(position + 1, '&');
                        deal = myName.toString();

                    } else {
                        deal = test;
                    }

                } else {
                    txtDeal.setText("No deals found");
                }
                setDealMethod(deal1, deal2, deal3, deal4, deal);

                saveDataOnPreference(empType, name, mobile, email, userid, pic, password, firmName);
                backProfileViewListener.OnCallBackProfileView();

            } else {
                if (!TextUtils.isEmpty(msg)) {
                    showDialogMethod(msg);
                } else {
                    showDialogMethod("Something went wrong.");
                }
            }
        } catch (Exception e) {
            showToastMethod(e.toString());
        }
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

    private void setDealMethod(final String deal1, final String deal2, final String deal3, final String deal4, final String deal) {
        if (!TextUtils.isEmpty(deal)) {
            txtDeal.setText("No deals found");
        } else {
            txtDeal.setText(Utility.fromHtml(deal));
        }

        llVehicle.removeAllViews();
//---------------------------one----------------------------------//
        RelativeLayout relativeLayout1 = new RelativeLayout(activity);

        TextView tv1 = new TextView(activity);
        ImageView imageView1 = new ImageView(activity);
        imageView1.setImageResource(android.R.drawable.ic_menu_edit);
        if (!TextUtils.isEmpty(deal1)) {
            tv1.setText(Utility.fromHtml(deal1));
        } else {
            tv1.setText(Utility.fromHtml("Select deal one"));
        }
        tv1.setBackgroundResource(R.drawable.button_white_border_gray);
        tv1.setPadding(16, 8, 16, 8);
        tv1.setTextSize(16);
        tv1.setTextColor(Utility.getColor(activity, android.R.color.black));
        tv1.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        tv1.setId(1 + 1);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodVechilePopup(deal1, "1");
            }
        });

        final RelativeLayout.LayoutParams cb =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cb.addRule(RelativeLayout.BELOW, 1 + 1);
        relativeLayout1.addView(tv1, cb);

        final RelativeLayout.LayoutParams cb1 = new RelativeLayout.LayoutParams(40, 40);
        cb1.setMargins(10, 10, 10, 10);
        cb1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1 + 1);
        relativeLayout1.addView(imageView1, cb1);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        relativeLayout1.setLayoutParams(params);

        llVehicle.addView(relativeLayout1);

//---------------------------two----------------------------------//
        RelativeLayout relativeLayout = new RelativeLayout(activity);

        TextView tv = new TextView(activity);
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(android.R.drawable.ic_menu_edit);
        if (!TextUtils.isEmpty(deal2)) {
            tv.setText(Utility.fromHtml(deal2));
        } else {
            tv.setText(Utility.fromHtml("Select deal two"));
        }
        tv.setBackgroundResource(R.drawable.button_white_border_gray);
        tv.setPadding(16, 8, 16, 8);
        tv.setTextSize(16);
        tv.setTextColor(Utility.getColor(activity, android.R.color.black));
        tv.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        tv.setId(1 + 2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodVechilePopup(deal2, "2");
            }
        });

        final RelativeLayout.LayoutParams cbn =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbn.addRule(RelativeLayout.BELOW, 1 + 2);
        relativeLayout.addView(tv, cbn);

        final RelativeLayout.LayoutParams cb1n = new RelativeLayout.LayoutParams(40, 40);
        cb1n.setMargins(10, 10, 10, 10);
        cb1n.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1 + 2);
        relativeLayout.addView(imageView, cb1n);

        LinearLayout.LayoutParams paramsn = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
        paramsn.setMargins(10, 10, 10, 10);
        relativeLayout.setLayoutParams(paramsn);

        llVehicle.addView(relativeLayout);

        //---------------------------two----------------------------------//
        RelativeLayout relativeLayout3 = new RelativeLayout(activity);

        TextView tv3 = new TextView(activity);
        ImageView imageView3 = new ImageView(activity);
        imageView3.setImageResource(android.R.drawable.ic_menu_edit);
        if (!TextUtils.isEmpty(deal3)) {
            tv3.setText(Utility.fromHtml(deal3));
        } else {
            tv3.setText(Utility.fromHtml("Select deal three"));
        }
        tv3.setBackgroundResource(R.drawable.button_white_border_gray);
        tv3.setPadding(16, 8, 16, 8);
        tv3.setTextSize(16);
        tv3.setTextColor(Utility.getColor(activity, android.R.color.black));
        tv3.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        tv3.setId(1 + 2);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodVechilePopup(deal3, "3");
            }
        });

        final RelativeLayout.LayoutParams cbn3 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbn3.addRule(RelativeLayout.BELOW, 1 + 2);
        relativeLayout3.addView(tv3, cbn3);

        final RelativeLayout.LayoutParams cb1n3 = new RelativeLayout.LayoutParams(40, 40);
        cb1n3.setMargins(10, 10, 10, 10);
        cb1n3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1 + 2);
        relativeLayout3.addView(imageView3, cb1n3);

        LinearLayout.LayoutParams paramsn3 = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
        paramsn3.setMargins(10, 10, 10, 10);
        relativeLayout3.setLayoutParams(paramsn3);

        llVehicle.addView(relativeLayout3);

        //---------------------------two----------------------------------//
        RelativeLayout relativeLayout4 = new RelativeLayout(activity);

        TextView tv4 = new TextView(activity);
        ImageView imageView4 = new ImageView(activity);
        imageView4.setImageResource(android.R.drawable.ic_menu_edit);
        if (!TextUtils.isEmpty(deal4)) {
            tv4.setText(Utility.fromHtml(deal4));
        } else {
            tv4.setText(Utility.fromHtml("Select deal four"));
        }
        tv4.setBackgroundResource(R.drawable.button_white_border_gray);
        tv4.setPadding(16, 8, 16, 8);
        tv4.setTextSize(16);
        tv4.setTextColor(Utility.getColor(activity, android.R.color.black));
        tv4.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        tv4.setId(1 + 2);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodVechilePopup(deal4, "4");
            }
        });

        final RelativeLayout.LayoutParams cbn4 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbn4.addRule(RelativeLayout.BELOW, 1 + 2);
        relativeLayout4.addView(tv4, cbn4);

        final RelativeLayout.LayoutParams cb1n4 = new RelativeLayout.LayoutParams(40, 40);
        cb1n4.setMargins(10, 10, 10, 10);
        cb1n4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1 + 2);
        relativeLayout4.addView(imageView4, cb1n4);

        LinearLayout.LayoutParams paramsn4 = new LinearLayout.LayoutParams(ActionBarOverlayLayout.LayoutParams.MATCH_PARENT, ActionBarOverlayLayout.LayoutParams.WRAP_CONTENT);
        paramsn4.setMargins(10, 10, 10, 10);
        relativeLayout4.setLayoutParams(paramsn4);

        llVehicle.addView(relativeLayout4);


    }

    private void methodVechilePopup(final String deal, final String type) {
        dialogMethod("state", deal, type, "Edit");
       /* final String TypeArray[] = {"Edit", "Delete"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (TypeArray[item].equalsIgnoreCase("Edit")) {
                    dialogMethod("state", deal, type, "Edit");
                    //methodEditVechile(deal, type, "Edit");
                } else {
                    methodEditVechile(" ", type, "Delete");
                }
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();*/
    }

    private void methodEditVechile(final String deal, final String type, final String action) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Deal " + action);
        alertDialogBuilder.setMessage("Are you sure you want to " + action + " this deal");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (action.equalsIgnoreCase("Edit")) {
                            dialogMethod("state", deal, type, action);
                        } else {
                            sendDataOnDeleteVehicle(deal, type, action);
                        }
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

    private void dialogMethod(String tt, String deal, final String type, String action) {
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
        recyclerview.addOnScrollListener(new replyScrollListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        AppCompatTextView txtTitle = dialog.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtButtonOk = dialog.findViewById(R.id.txtButtonOk);
        txtButtonOk.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtButtonOk.setVisibility(View.VISIBLE);
        txtButtonOk.setText("Delete");
        txtButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                methodEditVechile(" ", type, "Delete");
            }
        });
        AppCompatEditText etDialog = (AppCompatEditText) dialog.findViewById(R.id.etDialog);
        etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDialog.addTextChangedListener(this);

        if (tt.equalsIgnoreCase("city")) {

        } else if (tt.equalsIgnoreCase("state")) {
            txtTitle.setText("Select Deal");
            adopter = new DealAdopter(activity);
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(adopter);
            adopter.setMyClickListener(this);
            topAction = type;
            page = 1;
            startLimit = 0;
            sendRequestStateListFeed(test, page);
        }

        dialog.show();
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
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                arrayList = responseparsing.getDealDetailArrayList();
                                if (arrayList.size() > 0) {

                                    if (adopter.getData() != null && adopter.getData().size() > 0) {
                                        adopter.getData().addAll(arrayList);
                                        adopter.notifyDataSetChanged();
                                    } else {
                                        adopter.setData(arrayList);
                                        adopter.notifyDataSetChanged();
                                    }

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
    public void onItemCatClick(int position, DealDetail model) {
        String del = model.getDealsId().trim();
        sendDataOnDeleteVehicle(del, topAction, "Edit");
        dialog.dismiss();
    }

    private void sendDataOnDeleteVehicle(final String deal, final String type, final String action) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.EDIT_DEAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            JSONObject jsonObject = null;
                            String msg = null, pass = null, a = "", b = "", c = "", d = "", x = "", a1 = "", a2 = "", a3 = "", a4 = "";
                            final ArrayList<String> data = new ArrayList<>();
                            int status = 0;
                            try {
                                jsonObject = new JSONObject(response);
                                if (jsonObject.has("status"))
                                    status = jsonObject.optInt("status");
                                if (jsonObject.has("msg"))
                                    msg = jsonObject.optString("msg");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status == 1) {
                                if (jsonObject.has("deals_in_1"))
                                    a = jsonObject.optString("deals_in_1");
                                if (jsonObject.has("deals_in_2"))
                                    b = jsonObject.optString("deals_in_2");
                                if (jsonObject.has("deals_in_3"))
                                    c = jsonObject.optString("deals_in_3");
                                if (jsonObject.has("deals_in_4"))
                                    d = jsonObject.optString("deals_in_4");

                                if (!TextUtils.isEmpty(a.trim()) && !a.equalsIgnoreCase("null")) {
                                    a1 = a.trim();
                                    data.add(a1);
                                }

                                if (!TextUtils.isEmpty(b.trim()) && !b.equalsIgnoreCase("null")) {
                                    a2 = b.trim();
                                    data.add(a2);
                                }
                                if (!TextUtils.isEmpty(c.trim()) && !c.equalsIgnoreCase("null")) {
                                    a3 = c.trim();
                                    data.add(a3);
                                }

                                if (!TextUtils.isEmpty(d.trim()) && !d.equalsIgnoreCase("null")) {
                                    a4 = d.trim();
                                    data.add(a4);
                                }

                                if (data.size() > 0) {
                                    String str = Arrays.toString(data.toArray());
                                    String test = str.replaceAll("[\\[\\](){}]", "");
                                    if (data.size() != 1) {
                                        int position = test.lastIndexOf(",");
                                        StringBuilder myName = new StringBuilder(test);
                                        myName.setCharAt(position, ' ');
                                        myName.insert(position + 1, '&');
                                        x = myName.toString();

                                    } else {
                                        x = test;
                                    }
                                } else {
                                    txtDeal.setText("No deals found");
                                }

                                setDealMethod(a1, a2, a3, a4, x);

                                if (!TextUtils.isEmpty(x)) {

                                }

                                llVehicle.setVisibility(View.GONE);
                                txtDeal.setVisibility(View.VISIBLE);
                                txtDealEdit.setText("Edit");
                                showToastMethod("Your deal successfully " + action);
                                dialog.dismiss();

                            } else {
                                if (!TextUtils.isEmpty(msg)) {
                                    showToastMethod(msg);
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
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deal", deal);
                params.put("type", type);
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void saveDataOnPreference(String empType, String username, String mobile, String email, String userid, String pic, String password, String FirmName) {
        SharedPreferencesManager.setType(activity, empType);
        SharedPreferencesManager.setUserID(activity, userid);
        SharedPreferencesManager.setUsername(activity, username);
        SharedPreferencesManager.setMobile(activity, mobile);
        SharedPreferencesManager.setEmail(activity, email);
        SharedPreferencesManager.setPassword(activity, password);
        SharedPreferencesManager.setProfileImage(activity, pic);
        SharedPreferencesManager.setFirmName(activity, FirmName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void initialize(View view) {
        imgBack = view.findViewById(R.id.imgBack);
        imgProfile = view.findViewById(R.id.imgProfile);
        imgAdds = view.findViewById(R.id.imgAdds);
        imgWhite = view.findViewById(R.id.imgWhite);

        imgEdit = view.findViewById(R.id.imgEdit);
        imgEdit.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        imgEdit.setVisibility(View.VISIBLE);

        txtCompany = view.findViewById(R.id.txtCompany);
        txtCompany.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());

        txtName = view.findViewById(R.id.txtName);
        txtName.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        txtAddress = view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtPartnerName = view.findViewById(R.id.txtPartnerName);
        txtPartnerName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobile = view.findViewById(R.id.txtMobile);
        txtMobile.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHead = view.findViewById(R.id.txtMobileHead);
        txtMobileHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOfficeHead = view.findViewById(R.id.txtOfficeHead);
        txtOfficeHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffice = view.findViewById(R.id.txtOffice);
        txtOffice.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffice2Head = view.findViewById(R.id.txtOffice2Head);
        txtOffice2Head.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtOffice2 = view.findViewById(R.id.txtOffice2);
        txtOffice2.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResidence = view.findViewById(R.id.txtResidence);
        txtResidence.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtResidenceHead = view.findViewById(R.id.txtResidenceHead);
        txtResidenceHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmail = view.findViewById(R.id.txtEmail);
        txtEmail.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHead = view.findViewById(R.id.txtEmailHead);
        txtEmailHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtWeb = view.findViewById(R.id.txtWeb);
        txtWeb.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtWebHead = view.findViewById(R.id.txtWebHead);
        txtWebHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtAddContact = view.findViewById(R.id.txtAddContact);
        txtAddContact.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddContact.setVisibility(View.GONE);
        txtShareContact = view.findViewById(R.id.txtShareContact);
        txtShareContact.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtShareContact.setVisibility(View.GONE);
        //txtDealHead,txtDealEdit,txtDeal
        txtDealHead = view.findViewById(R.id.txtDealHead);
        txtDealHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDealEdit = view.findViewById(R.id.txtDealEdit);
        txtDealEdit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDeal = view.findViewById(R.id.txtDeal);
        txtDeal.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        llVehicle =  view.findViewById(R.id.llVehicle);
        imgWhite.setVisibility(View.VISIBLE);
        imgProfile.setVisibility(View.VISIBLE);
    }

    private void clickMethod(View view) {
        imgBack.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        txtAddContact.setOnClickListener(this);
        txtDealEdit.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        txtWeb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Utility.hideKeyboard(activity);
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.imgProfile:
                if (CropImage.isExplicitCameraPermissionRequired(activity)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                } else {
                    startActivityForResult(CropImage.getPickImageChooserIntent(activity), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
                }
                break;
            case R.id.txtDealEdit:
                if (txtDealEdit.getText().toString().equalsIgnoreCase("Edit")) {
                    llVehicle.setVisibility(View.VISIBLE);
                    txtDeal.setVisibility(View.GONE);
                    txtDealEdit.setText("Cancel");
                } else {
                    llVehicle.setVisibility(View.GONE);
                    txtDeal.setVisibility(View.VISIBLE);
                    txtDealEdit.setText("Edit");
                }
                break;
            case R.id.imgEdit:
                String off = txtOffice.getText().toString().trim();
                String off2 = txtOffice2.getText().toString().trim();
                String res = txtResidence.getText().toString().trim();
                // String name = txtName.getText().toString().trim();
                String mail = txtEmail.getText().toString().trim();
                String website = txtWeb.getText().toString().trim();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("off", off);
                bundle.putString("off2", off2);
                bundle.putString("res", res);
                bundle.putString("mail", mail);
                bundle.putString("name", name);
                bundle.putString("website", website);
                editProfileFragment.setArguments(bundle);
                editProfileFragment.setMyCallBackProfileEditListener(this);
                callFragmentMethod(editProfileFragment, Constants.FRG_EDIT_PROFILE, R.id.containerMain);
                break;
            case R.id.txtWeb:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + txtWeb.getText().toString().trim()));
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.member_profile_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(activity, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(activity, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == activity.RESULT_OK) {
                photo = decodeFile(result.getUri().getPath());
                setProfilePicMethod();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(activity, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(CropImage.getPickImageChooserIntent(activity), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
            } else {
                Toast.makeText(activity, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(activity, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
    }

    private void setProfilePicMethod() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_EDIT_PIC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        responseImplement(response);
                        Log.e("res", response);
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
                params.put("memberId", SharedPreferencesManager.getUserID(activity));
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    Log.e("img", encodedImage);
                    params.put("images", encodedImage + "image/jpeg");
                } else {
                    params.put("images", pic);
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(activity, this);

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
                    sendRequestStateListFeed(search, page);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
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

    private void sendRequestListFeed(String text) {
        arrayList.removeAll(arrayList);
        adopter.setData(null);
        adopter.notifyDataSetChanged();
        search = text;
        page = 1;
        startLimit = 0;
        sendRequestStateListFeed(search, page);
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

    @Override
    public void OnCallBackEditProfileView() {
        hitApi(SharedPreferencesManager.getUserID(activity));
    }
}
