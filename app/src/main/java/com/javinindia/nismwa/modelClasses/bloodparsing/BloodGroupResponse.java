package com.javinindia.nismwa.modelClasses.bloodparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashis on 3/27/2018.
 */

public class BloodGroupResponse extends ApiBaseData {
    private ArrayList<BloodGroupDetail> bloodGroupDetailArrayList;

    public ArrayList<BloodGroupDetail> getBloodGroupDetailArrayList() {
        return bloodGroupDetailArrayList;
    }

    public void setBloodGroupDetailArrayList(ArrayList<BloodGroupDetail> bloodGroupDetailArrayList) {
        this.bloodGroupDetailArrayList = bloodGroupDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setBloodGroupDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<BloodGroupDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<BloodGroupDetail> details = new ArrayList<>();
        BloodGroupDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new BloodGroupDetail();
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
            if (jsonObject.has("remark"))
                tanentDetail.setRemark(jsonObject.optString("remark"));
            details.add(tanentDetail);
        }
        return details;
    }
}
