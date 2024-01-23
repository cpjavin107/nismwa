package com.javinindia.nismwa.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberLoginResponse;
import com.javinindia.nismwa.picasso.CircleTransform;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 07-07-2017.
 */

public class OtherMemberProfile extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    ImageView imgProfile, imgWhite, imgAdds, imgBack;
    AppCompatTextView imgEdit, txtCompany, txtName, txtAddress, txtPartnerName, txtMobile, txtMobileHead, txtOfficeHead, txtOffice, txtResidence, txtResidenceHead, txtEmail, txtEmailHead, txtAddContact, txtDealHead, txtDealEdit, txtDeal, txtShareContact, txtWebHead, txtWeb, txtOffice2Head, txtOffice2;
    String memberId = "";
    LinearLayout llVehicle, llDealMain, llWebsite, llOffice2;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    final ArrayList<String> contact = new ArrayList<>();
    int page_position = 0;
    ArrayList<AdsDetail> adsDetailArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberId = getArguments().getString("Id");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            this.getArguments().clear();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        hitApi(memberId);
        return view;
    }

    private void hitApi(final String userID) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBER_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
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

                if (responseparsing.getDetailsArrayList().get(0).getType().equalsIgnoreCase("1")) {
                    imgWhite.setVisibility(View.VISIBLE);
                    imgProfile.setVisibility(View.VISIBLE);
                } else {
                    imgWhite.setVisibility(View.GONE);
                    imgProfile.setVisibility(View.GONE);
                }

                if (responseparsing.getDetailsArrayList().get(0).getType_name().equalsIgnoreCase("Broker")) {
                    if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getName().trim()) && !responseparsing.getDetailsArrayList().get(0).getName().equalsIgnoreCase("null")) {
                        firmName = responseparsing.getDetailsArrayList().get(0).getName().trim();
                        txtCompany.setText(Utility.fromHtml(firmName));
                        contact.add(firmName);
                    } else {
                        txtCompany.setText(Utility.fromHtml("Name not found"));
                    }

                    if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getAddress()) && !responseparsing.getDetailsArrayList().get(0).getAddress().equalsIgnoreCase("null")) {
                        address = responseparsing.getDetailsArrayList().get(0).getAddress().trim();
                        txtAddress.setText(Utility.fromHtml("(" + address + ")"));
                        contact.add(address);
                    }
                } else {
                    if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getFirm_name().trim()) && !responseparsing.getDetailsArrayList().get(0).getFirm_name().equalsIgnoreCase("null")) {
                        firmName = responseparsing.getDetailsArrayList().get(0).getFirm_name().trim();
                        txtCompany.setText(Utility.fromHtml(firmName));
                        contact.add(firmName);
                    } else {
                        txtCompany.setText(Utility.fromHtml("Firm name not found"));
                    }
                    if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getName().trim()) && !responseparsing.getDetailsArrayList().get(0).getName().equalsIgnoreCase("null")) {
                        firstName = responseparsing.getDetailsArrayList().get(0).getName().trim();
                        txtName.setText(Utility.fromHtml(firstName));
                        contact.add(firstName);
                    }
                    if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getAddress().trim()) && !responseparsing.getDetailsArrayList().get(0).getAddress().equalsIgnoreCase("null")) {
                        address = responseparsing.getDetailsArrayList().get(0).getAddress().trim();
                        //  firstName = firstName + "<br>(" + address + ")";
                        txtAddress.setText(Utility.fromHtml("(" + address + ")"));
                        contact.add(address);
                    }
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
                    txtMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_call, 0);
                    contact.add("Mobile no. " + mobile);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().trim()) && !responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().equalsIgnoreCase("null")) {
                    office = responseparsing.getDetailsArrayList().get(0).getOfficeContactNumber().trim();
                    if (office.length() >= 10) {
                        office = office;
                    } else {
                        office = "011" + office;
                    }
                    txtOffice.setText(Utility.fromHtml(office));
                    txtOffice.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_call, 0);
                    contact.add("Office no. 1" + office);
                }
                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getLandlineNumber().trim()) && !responseparsing.getDetailsArrayList().get(0).getLandlineNumber().equalsIgnoreCase("null")) {
                    office2 = responseparsing.getDetailsArrayList().get(0).getLandlineNumber().trim();
                    if (office2.length() >= 10) {
                        office2 = office2;
                    } else {
                        office2 = "011" + office2;
                    }
                    txtOffice2.setText(Utility.fromHtml(office2));
                    txtOffice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_call, 0);
                    llOffice2.setVisibility(View.VISIBLE);
                    contact.add("Office no. 2" + office2);
                } else {
                    llOffice2.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getResidence().trim()) && !responseparsing.getDetailsArrayList().get(0).getResidence().equalsIgnoreCase("null")) {
                    String res = responseparsing.getDetailsArrayList().get(0).getResidence().trim();
                    if (res.length() >= 10) {
                        res = res;
                    } else {
                        res = "011" + res;
                    }
                    txtResidence.setText(Utility.fromHtml(res));
                    txtResidence.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_call, 0);
                }

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getEmail().trim()) && !responseparsing.getDetailsArrayList().get(0).getEmail().equalsIgnoreCase("null")) {
                    email = responseparsing.getDetailsArrayList().get(0).getEmail().trim();
                    txtEmail.setText(Utility.fromHtml(email));
                    txtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.sym_action_email, 0);
                    contact.add("Email " + email);
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

                if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getWebsite().trim()) && !responseparsing.getDetailsArrayList().get(0).getWebsite().equalsIgnoreCase("null")) {
                    String web = responseparsing.getDetailsArrayList().get(0).getWebsite().trim();
                    txtWeb.setText(Utility.fromHtml(web));
                    llWebsite.setVisibility(View.VISIBLE);
                } else {
                    txtWeb.setText(Utility.fromHtml(""));
                    llWebsite.setVisibility(View.VISIBLE);
                }

           /* if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim().equalsIgnoreCase("null")) {
                deal = responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim();
                deal1 = responseparsing.getDetailsArrayList().get(0).getDeals_in_1().trim();
            }

            if (!TextUtils.isEmpty(responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim()) && !responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim().equalsIgnoreCase("null")) {
                deal2 = responseparsing.getDetailsArrayList().get(0).getDeals_in_2().trim();
                if (!TextUtils.isEmpty(deal1)) {
                    deal = deal1 + " & " + deal2;
                } else {
                    deal = deal2;
                }

            }*/

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
                    txtDeal.setText(" ");
                }

                if (responseparsing.getDetailsArrayList().get(0).getType().equalsIgnoreCase("1")) {
                    llDealMain.setVisibility(View.VISIBLE);
                } else {
                    llDealMain.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(deal)) {
                    txtDeal.setText(Utility.fromHtml(deal));
                } else {
                    txtDeal.setText(Utility.fromHtml(" "));
                }

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
        imgEdit.setVisibility(View.GONE);

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
        txtShareContact = view.findViewById(R.id.txtShareContact);
        txtShareContact.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        if (memberId.equalsIgnoreCase(SharedPreferencesManager.getUserID(activity))) {
            txtAddContact.setVisibility(View.GONE);
        } else {
            txtAddContact.setVisibility(View.VISIBLE);
        }


        txtDealHead = view.findViewById(R.id.txtDealHead);
        txtDealHead.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDealEdit = view.findViewById(R.id.txtDealEdit);
        txtDealEdit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDeal = view.findViewById(R.id.txtDeal);
        txtDeal.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        llVehicle = view.findViewById(R.id.llVehicle);
        llDealMain = view.findViewById(R.id.llDealMain);
        llWebsite = view.findViewById(R.id.llWebsite);
        llOffice2 = view.findViewById(R.id.llOffice2);

        txtDealEdit.setVisibility(View.GONE);
        llVehicle.setVisibility(View.GONE);
        llOffice2.setVisibility(View.GONE);
    }

    private void clickMethod(View view) {
        imgBack.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        txtAddContact.setOnClickListener(this);
        txtMobile.setOnClickListener(this);
        txtOffice.setOnClickListener(this);
        txtOffice2.setOnClickListener(this);
        txtEmail.setOnClickListener(this);
        txtResidence.setOnClickListener(this);
        txtShareContact.setOnClickListener(this);
        txtWeb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                activity.onBackPressed();
                break;
            case R.id.txtAddContact:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    methodAddContact();
                }
                break;
            case R.id.txtMobile:
                if (!TextUtils.isEmpty(txtMobile.getText().toString())) {
                    mobileMethod(txtMobile.getText().toString().trim());
                }
                break;
            case R.id.txtOffice:
                if (!TextUtils.isEmpty(txtOffice.getText().toString())) {
                    mobileMethod(txtOffice.getText().toString().trim());
                }
                break;
            case R.id.txtOffice2:
                if (!TextUtils.isEmpty(txtOffice2.getText().toString())) {
                    mobileMethod(txtOffice2.getText().toString().trim());
                }
                break;
            case R.id.txtResidence:
                if (!TextUtils.isEmpty(txtResidence.getText().toString())) {
                    mobileMethod(txtResidence.getText().toString().trim());
                }
                break;
            case R.id.txtEmail:
                if (!TextUtils.isEmpty(txtEmail.getText().toString())) {
                    emailMethod(txtEmail.getText().toString().trim());
                }
                break;
            case R.id.txtShareContact:
                if (contact.size() > 0) {
                    methodShare();
                }
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

    private void methodShare() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Share contact ");
        alertDialogBuilder.setMessage("Are you sure you want to share this contact");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {

                            if (contact.size() > 0) {
                               /* String str = Arrays.toString(contact.toArray());
                                String test = str.replaceAll("[\\[\\](){}]", "");
                                String sAux = "\n" + test + "\n\n";
                                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                whatsappIntent.setType("text/plain");
                                whatsappIntent.putExtra(Intent.EXTRA_SUBJECT, "Nismwa App");
                                whatsappIntent.setPackage("com.whatsapp");
                                whatsappIntent.putExtra(Intent.EXTRA_TEXT, sAux);
                                try {
                                    activity.startActivity(whatsappIntent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                    //activity.ToastHelper.MakeShortText("Whatsapp have not been installed.");
                                    showToastMethod("Whatsapp have not been installed.");
                                }*/


                               /* String str = Arrays.toString(contact.toArray());
                                String test = str.replaceAll("[\\[\\](){}]", "");

                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("text/plain");
                                i.putExtra(Intent.EXTRA_SUBJECT, "Nismwa App");
                                String sAux = "\n" + test + "\n\n";
                                // sAux = sAux + "https://play.google.com/store/apps/details?id=com.javinindia.nismwa\n\n";
                                i.putExtra(Intent.EXTRA_TEXT, sAux);
                                startActivity(Intent.createChooser(i, "Choose one"));*/


                                Intent sendIntent = new Intent("android.intent.action.MAIN");
                                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("918743007244") + "@s.whatsapp.net");//phone number without "+" prefix

                                startActivity(sendIntent);
                            }

                        } catch (Exception e) {

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

    private void emailMethod(String mail) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "write here...");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void mobileMethod(String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (num.length() >= 10) {
            num = num;
        } else {
            num = "011" + num;
        }
        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                methodAddContact();
            } else {
                showToastMethod("Until you grant the permission, we can not save details");
            }
        }
    }

    private void methodAddContact() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Add contact ");
        alertDialogBuilder.setMessage("Are you sure you want to add this contact");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        ArrayList<ContentProviderOperation> ops =
                                new ArrayList<ContentProviderOperation>();

                        int rawContactID = ops.size();

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, txtName.getText().toString())
                                .build());

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, txtMobile.getText().toString())
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                .build());

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, txtOffice.getText().toString())
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                                .build());

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, txtEmail.getText().toString())
                                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                                .build());

                        try {
                            activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                            showToastMethod("Contact is successfully added");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (OperationApplicationException e) {
                            e.printStackTrace();
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
}
