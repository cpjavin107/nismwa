package com.javinindia.nismwa.modelClasses.memberloginparsing;


import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 05-07-2017.
 */

public class MemberLoginResponse extends ApiBaseData {

    private ArrayList<MemberDetail> detailsArrayList;
    private ArrayList<AdsDetail> adsDetailArrayList;

    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }

    public ArrayList<MemberDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<MemberDetail> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
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

    private ArrayList<MemberDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<MemberDetail> details = new ArrayList<>();
        MemberDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new MemberDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("name"))
                tanentDetail.setName(jsonObject.optString("name"));
            if (jsonObject.has("password"))
                tanentDetail.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("firm_no"))
                tanentDetail.setFirm_no(jsonObject.optString("firm_no"));
            if (jsonObject.has("mobileNumber"))
                tanentDetail.setMobileNumber(jsonObject.optString("mobileNumber"));
            if (jsonObject.has("landlineNumber"))
                tanentDetail.setLandlineNumber(jsonObject.optString("landlineNumber"));
            if (jsonObject.has("officeContactNumber"))
                tanentDetail.setOfficeContactNumber(jsonObject.optString("officeContactNumber"));
            if (jsonObject.has("email"))
                tanentDetail.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("imageUrl"))
                tanentDetail.setImageUrl(jsonObject.optString("imageUrl"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("deals_in_1"))
                tanentDetail.setDeals_in_1(jsonObject.optString("deals_in_1"));
            if (jsonObject.has("deals_in_2"))
                tanentDetail.setDeals_in_2(jsonObject.optString("deals_in_2"));
            if (jsonObject.has("deals_in_3"))
                tanentDetail.setDeals_in_3(jsonObject.optString("deals_in_3"));
            if (jsonObject.has("deals_in_4"))
                tanentDetail.setDeals_in_4(jsonObject.optString("deals_in_4"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("residence"))
                tanentDetail.setResidence(jsonObject.optString("residence"));
            if (jsonObject.has("deviceToken"))
                tanentDetail.setDeviceToken(jsonObject.optString("deviceToken"));
            if (jsonObject.has("website"))
                tanentDetail.setWebsite(jsonObject.optString("website"));
            if (jsonObject.has("login_type"))
                tanentDetail.setLogin_type(jsonObject.optString("login_type"));
            if (jsonObject.has("type_name"))
                tanentDetail.setType_name(jsonObject.optString("type_name"));
            if (jsonObject.has("firm_name"))
                tanentDetail.setFirm_name(jsonObject.optString("firm_name"));
            if (jsonObject.has("memberInformation") && jsonObject.optJSONArray("memberInformation") != null)
                tanentDetail.setMemberDetailArrayList(getPartnerDetailMethod(jsonObject.optJSONArray("memberInformation")));
            details.add(tanentDetail);
        }
        return details;
    }

    private ArrayList<MemberDetail> getPartnerDetailMethod(JSONArray jsonArray) {
        ArrayList<MemberDetail> details = new ArrayList<>();
        MemberDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new MemberDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("name"))
                tanentDetail.setName(jsonObject.optString("name"));
            if (jsonObject.has("password"))
                tanentDetail.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("firm_no"))
                tanentDetail.setFirm_no(jsonObject.optString("firm_no"));
            if (jsonObject.has("mobileNumber"))
                tanentDetail.setMobileNumber(jsonObject.optString("mobileNumber"));
            if (jsonObject.has("landlineNumber"))
                tanentDetail.setLandlineNumber(jsonObject.optString("landlineNumber"));
            if (jsonObject.has("officeContactNumber"))
                tanentDetail.setOfficeContactNumber(jsonObject.optString("officeContactNumber"));
            if (jsonObject.has("email"))
                tanentDetail.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("imageUrl"))
                tanentDetail.setImageUrl(jsonObject.optString("imageUrl"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("deals_in_1"))
                tanentDetail.setDeals_in_1(jsonObject.optString("deals_in_1"));
            if (jsonObject.has("deals_in_2"))
                tanentDetail.setDeals_in_2(jsonObject.optString("deals_in_2"));
            if (jsonObject.has("deals_in_3"))
                tanentDetail.setDeals_in_3(jsonObject.optString("deals_in_3"));
            if (jsonObject.has("deals_in_4"))
                tanentDetail.setDeals_in_4(jsonObject.optString("deals_in_4"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("residence"))
                tanentDetail.setResidence(jsonObject.optString("residence"));
            if (jsonObject.has("deviceToken"))
                tanentDetail.setDeviceToken(jsonObject.optString("deviceToken"));
            if (jsonObject.has("website"))
                tanentDetail.setWebsite(jsonObject.optString("website"));
            if (jsonObject.has("login_type"))
                tanentDetail.setLogin_type(jsonObject.optString("login_type"));
            if (jsonObject.has("type_name"))
                tanentDetail.setType_name(jsonObject.optString("type_name"));
            if (jsonObject.has("firm_name"))
                tanentDetail.setFirm_name(jsonObject.optString("firm_name"));
            details.add(tanentDetail);
        }
        return details;
    }
}
