package com.javinindia.nismwa.modelClasses.managementlistparsing;

import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 06-07-2017.
 */

public class ManagementListResponse extends ApiBaseData {
    private ArrayList<AdsDetail> adsDetailArrayList;
    public ArrayList<ManagementDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<ManagementDetail> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    private ArrayList<ManagementDetail> detailsArrayList;
    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }


    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
            if (jsonObject.has("ads") && jsonObject.optJSONArray("ads") != null)
                setAdsDetailArrayList(getAdsDetailMethod(jsonObject.optJSONArray("ads")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ManagementDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<ManagementDetail> details = new ArrayList<>();
        ManagementDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new ManagementDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("image"))
                tanentDetail.setImage(jsonObject.optString("image"));
            if (jsonObject.has("designationName"))
                tanentDetail.setDesignationName(jsonObject.optString("designationName"));
            if (jsonObject.has("mc_id"))
                tanentDetail.setMc_id(jsonObject.optString("mc_id"));
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("mc_joining_date"))
                tanentDetail.setMc_joining_date(jsonObject.optString("mc_joining_date"));
            if (jsonObject.has("mc_ending_date"))
                tanentDetail.setMc_ending_date(jsonObject.optString("mc_ending_date"));
            if (jsonObject.has("added_date"))
                tanentDetail.setAdded_date(jsonObject.optString("added_date"));
            if (jsonObject.has("mc_name"))
                tanentDetail.setMc_name(jsonObject.optString("mc_name"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("mobileNumber"))
                tanentDetail.setMobileNumber(jsonObject.optString("mobileNumber"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("deals_in"))
                tanentDetail.setDeals_in(jsonObject.optString("deals_in"));
            if (jsonObject.has("landlineNumber"))
                tanentDetail.setLandlineNumber(jsonObject.optString("landlineNumber"));
            if (jsonObject.has("officeContactNumber"))
                tanentDetail.setOfficeContactNumber(jsonObject.optString("officeContactNumber"));
            if (jsonObject.has("email"))
                tanentDetail.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("residence"))
                tanentDetail.setResidence(jsonObject.optString("residence"));
            if (jsonObject.has("type_name"))
                tanentDetail.setType_name(jsonObject.optString("type_name"));
            if (jsonObject.has("firm_name"))
                tanentDetail.setFirm_name(jsonObject.optString("firm_name"));

            details.add(tanentDetail);
        }
        return details;
    }

    private ArrayList<AdsDetail> getAdsDetailMethod(JSONArray jsonArray) {
        ArrayList<AdsDetail> details = new ArrayList<>();
        AdsDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new AdsDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("title"))
                tanentDetail.setTitle(jsonObject.optString("title"));
            if (jsonObject.has("imageUrl"))
                tanentDetail.setImageUrl(jsonObject.optString("imageUrl"));
            if (jsonObject.has("imageUrlSmall"))
                tanentDetail.setImageUrlSmall(jsonObject.optString("imageUrlSmall"));
            if (jsonObject.has("period"))
                tanentDetail.setPeriod(jsonObject.optString("period"));
            if (jsonObject.has("rate"))
                tanentDetail.setRate(jsonObject.optString("rate"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insert_date"))
                tanentDetail.setInsert_date(jsonObject.optString("insert_date"));
            if (jsonObject.has("update_date"))
                tanentDetail.setUpdate_date(jsonObject.optString("update_date"));
            if (jsonObject.has("big_image"))
                tanentDetail.setBig_image(jsonObject.optString("big_image"));
            if (jsonObject.has("small_image"))
                tanentDetail.setSmall_image(jsonObject.optString("small_image"));
            if (jsonObject.has("t"))
                tanentDetail.setT(jsonObject.optString("t"));
            details.add(tanentDetail);
        }
        return details;
    }
}
