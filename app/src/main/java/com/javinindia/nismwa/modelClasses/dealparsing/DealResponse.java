package com.javinindia.nismwa.modelClasses.dealparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.membertypeparsing.TypeDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 12-07-2017.
 */

public class DealResponse extends ApiBaseData {
    ArrayList<DealDetail> dealDetailArrayList;

    public ArrayList<DealDetail> getDealDetailArrayList() {
        return dealDetailArrayList;
    }

    public void setDealDetailArrayList(ArrayList<DealDetail> dealDetailArrayList) {
        this.dealDetailArrayList = dealDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setDealDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DealDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<DealDetail> details = new ArrayList<>();
        DealDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new DealDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("dealsId"))
                tanentDetail.setDealsId(jsonObject.optString("dealsId"));
            if (jsonObject.has("dealsName"))
                tanentDetail.setDealsName(jsonObject.optString("dealsName"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("cDate"))
                tanentDetail.setcDate(jsonObject.optString("cDate"));

            details.add(tanentDetail);
        }
        return details;
    }
}
