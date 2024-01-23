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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import com.javinindia.nismwa.modelClasses.bloodparsing.BloodGroupDetail;
import com.javinindia.nismwa.modelClasses.bloodparsing.BloodGroupResponse;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsfullMasterResponse;
import com.javinindia.nismwa.preference.SharedPreferencesManager;
import com.javinindia.nismwa.recyclerView.BloodGroupAdopter;
import com.javinindia.nismwa.recyclerView.ComplaintCategoryAdopter;
import com.javinindia.nismwa.recyclerView.UsefullMasterAdopter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashis on 3/23/2018.
 */

public class DonateBloodFragment extends BaseFragment implements View.OnClickListener, BloodGroupAdopter.MyClickListener {
    private RequestQueue requestQueue;
    AppCompatTextView txtName, txtAge, txtMobile, txtSubmit, txtAddress, txtGroup, txtSelectGroup;
    AppCompatEditText etName, etAge, etMobile, etAddress;

    ProgressBar progressBar;
    Dialog dialog;
    int width;
    int height;
    BloodGroupAdopter usefullAdopter;
    String bGroupId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        clickMethod(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    private void clickMethod(View view) {
        txtSubmit.setOnClickListener(this);
    }

    private void initialize(View view) {
        txtName =  view.findViewById(R.id.txtName);
        txtName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtAge =   view.findViewById(R.id.txtAge);
        txtAge.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobile =   view.findViewById(R.id.txtMobile);
        txtMobile.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSubmit =   view.findViewById(R.id.txtSubmit);
        txtSubmit.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
//,,,
        txtAddress =   view.findViewById(R.id.txtAddress);
        txtAddress.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGroup =   view.findViewById(R.id.txtGroup);
        txtGroup.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtSelectGroup =   view.findViewById(R.id.txtSelectGroup);
        txtSelectGroup.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        etName =   view.findViewById(R.id.etName);
        etName.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAge =   view.findViewById(R.id.etAge);
        etAge.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etMobile =   view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress =   view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtSelectGroup.setOnClickListener(this);
        etName.setText(SharedPreferencesManager.getUsername(activity));
        etMobile.setText(SharedPreferencesManager.getMobile(activity));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSubmit:
                methodSubmit();
                break;
            case R.id.txtSelectGroup:
                // methodBlockPopup();
                dialogMethod("useful");
                break;
            default:
                break;
        }
    }

    private void methodBlockPopup() {
        final String TypeArray[] = {"Any", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(TypeArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                txtSelectGroup.setText(TypeArray[item].trim());
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void methodSubmit() {
        String title = etName.getText().toString().trim();
        String desc = etAge.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String group = txtSelectGroup.getText().toString().trim();
        if (validation(title, desc, mobile, address, group)) {
            sendDataOnFeedBackApi(title, desc, mobile, address, group);
        }
    }

    private boolean validation(String title, String desc, String mobile, String address, String group) {
        if (TextUtils.isEmpty(title)) {
            showToastMethod("You have not entered name.");
            return false;
        } else if (TextUtils.isEmpty(desc)) {
            showToastMethod("You have not entered age.");
            return false;
        } else if (TextUtils.isEmpty(mobile)) {
            showToastMethod("You have not entered mobile no.");
            return false;
        } else if (mobile.length() != 10) {
            showToastMethod("Invalid mobile no.");
            return false;
        } else if (TextUtils.isEmpty(address)) {
            showToastMethod("You have not entered address.");
            return false;
        } else if (TextUtils.isEmpty(group)) {
            showToastMethod("You have not selected blood group.");
            return false;
        } else {
            return true;
        }
    }

    private void sendDataOnFeedBackApi(final String title, final String desc, final String mobile, final String address, final String group) {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DONATE_BLOOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.e("addd bbbb", response);
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
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("address", address);
                params.put("name", title);
                params.put("age", desc);
                params.put("mobile", mobile);
                params.put("blood_group", bGroupId);
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void responseImplement(String response) {
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
                    etName.setText("");
                    etAge.setText("");
                    etMobile.setText("");
                    etAddress.setText("");
                    txtSelectGroup.setText("");
                    showToastMethod("Successfully submitted");
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

    @Override
    protected int getFragmentLayout() {
        return R.layout.donate_blood_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

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

        AppCompatTextView txtTitle =   dialog.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontRobotoCondensedBoldSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatEditText etDialog =   dialog.findViewById(R.id.etDialog);
        etDialog.setTypeface(FontRobotoCondensedRegularSingleTonClass.getInstance(activity).getTypeFace());

        if (tt.equalsIgnoreCase("useful")) {
            txtTitle.setText("Select Blood Group");
            etDialog.setVisibility(View.GONE);
            usefullAdopter = new BloodGroupAdopter(activity);
            LinearLayoutManager layoutMangerDestination
                    = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            recyclerview.setLayoutManager(layoutMangerDestination);
            recyclerview.setAdapter(usefullAdopter);
            usefullAdopter.setMyClickListener(this);
            sendRequestUsefullListFeed(test);
        }
        dialog.show();
    }

    private void sendRequestUsefullListFeed(String test) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BLOOD_MASTER_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        BloodGroupResponse responseparsing = new BloodGroupResponse();
                        responseparsing.responseParseMethod(response);
                        if (response.length() != 0) {
                            int status = responseparsing.getStatus();
                            if (status == 1) {
                                ArrayList arrayList = responseparsing.getBloodGroupDetailArrayList();
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
                params.put("action", "api");
                params.put("page", "1");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemUseClick(int position, BloodGroupDetail model) {
        txtSelectGroup.setText(model.getGroup_name().trim());
        bGroupId = model.getId().trim();
        dialog.dismiss();
    }
}
