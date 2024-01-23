package com.javinindia.nismwa.modelClasses.countryStateCityparsing;

import android.util.Log;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 27-04-2017.
 */

public class StateResponse extends ApiBaseData {

    private ArrayList<StateDetail> detailsArrayList;

    public ArrayList<StateDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<StateDetail> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("data")));

            Log.d("Response", this.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<StateDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<StateDetail> details = new ArrayList<>();
        StateDetail quickJobDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            quickJobDetail = new StateDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("stateId"))
                quickJobDetail.setStateId(jsonObject.optString("stateId"));
            if (jsonObject.has("countryId"))
                quickJobDetail.setCountryId(jsonObject.optString("countryId"));
            if (jsonObject.has("stateName"))
                quickJobDetail.setStateName(jsonObject.optString("stateName"));
            if (jsonObject.has("cDate"))
                quickJobDetail.setcDate(jsonObject.optString("cDate"));
            if (jsonObject.has("status"))
                quickJobDetail.setStatus(jsonObject.optString("status"));

            details.add(quickJobDetail);
        }
        return details;
    }
}
