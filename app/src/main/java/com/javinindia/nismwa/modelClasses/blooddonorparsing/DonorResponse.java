package com.javinindia.nismwa.modelClasses.blooddonorparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DonorResponse extends ApiBaseData {
    private ArrayList<DonorDetail> donorDetailArrayList;

    public ArrayList<DonorDetail> getDonorDetailArrayList() {
        return donorDetailArrayList;
    }

    public void setDonorDetailArrayList(ArrayList<DonorDetail> donorDetailArrayList) {
        this.donorDetailArrayList = donorDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setDonorDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DonorDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<DonorDetail> details = new ArrayList<>();
        DonorDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new DonorDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("group_name"))
                tanentDetail.setGroup_name(jsonObject.optString("group_name"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("userid"))
                tanentDetail.setUserid(jsonObject.optString("userid"));

            if (jsonObject.has("name"))
                tanentDetail.setName(jsonObject.optString("name"));
            if (jsonObject.has("age"))
                tanentDetail.setAge(jsonObject.optString("age"));
            if (jsonObject.has("mobile"))
                tanentDetail.setMobile(jsonObject.optString("mobile"));
            details.add(tanentDetail);
        }
        return details;
    }
}
