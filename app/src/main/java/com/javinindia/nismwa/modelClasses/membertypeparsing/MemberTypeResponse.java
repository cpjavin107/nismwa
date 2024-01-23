package com.javinindia.nismwa.modelClasses.membertypeparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.eventnewsparsing.EventDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 06-07-2017.
 */

public class MemberTypeResponse extends ApiBaseData {
    ArrayList<TypeDetail> typeDetailArrayList;

    public ArrayList<TypeDetail> getTypeDetailArrayList() {
        return typeDetailArrayList;
    }

    public void setTypeDetailArrayList(ArrayList<TypeDetail> typeDetailArrayList) {
        this.typeDetailArrayList = typeDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setTypeDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<TypeDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<TypeDetail> details = new ArrayList<>();
        TypeDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new TypeDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("type_name"))
                tanentDetail.setType_name(jsonObject.optString("type_name"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insert_date"))
                tanentDetail.setInsert_date(jsonObject.optString("insert_date"));
            if (jsonObject.has("update_date"))
                tanentDetail.setUpdate_date(jsonObject.optString("update_date"));

            details.add(tanentDetail);
        }
        return details;
    }
}
