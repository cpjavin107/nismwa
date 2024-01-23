package com.javinindia.nismwa.modelClasses.usefullInfoparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 17-07-2017.
 */

public class UsfullMasterResponse extends ApiBaseData {
    private ArrayList<UsefullMasterDetail> usefullMasterDetailArrayList;

    public ArrayList<UsefullMasterDetail> getUsefullMasterDetailArrayList() {
        return usefullMasterDetailArrayList;
    }

    public void setUsefullMasterDetailArrayList(ArrayList<UsefullMasterDetail> usefullMasterDetailArrayList) {
        this.usefullMasterDetailArrayList = usefullMasterDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setUsefullMasterDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<UsefullMasterDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<UsefullMasterDetail> details = new ArrayList<>();
        UsefullMasterDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new UsefullMasterDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("uName"))
                tanentDetail.setuName(jsonObject.optString("uName"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("cDate"))
                tanentDetail.setcDate(jsonObject.optString("cDate"));

            details.add(tanentDetail);
        }
        return details;
    }
}
