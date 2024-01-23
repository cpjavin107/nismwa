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

public class CityResponse extends ApiBaseData {

    private ArrayList<CityDetail> detailsArrayList;

    public ArrayList<CityDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<CityDetail> detailsArrayList) {
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

    private ArrayList<CityDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<CityDetail> details = new ArrayList<>();
        CityDetail quickJobDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            quickJobDetail = new CityDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("cityId"))
                quickJobDetail.setCityId(jsonObject.optString("cityId"));
            if (jsonObject.has("stateId"))
                quickJobDetail.setCountryId(jsonObject.optString("stateId"));
            if (jsonObject.has("countryId"))
                quickJobDetail.setCountryId(jsonObject.optString("countryId"));
            if (jsonObject.has("cityName"))
                quickJobDetail.setCityName(jsonObject.optString("cityName"));
            if (jsonObject.has("cDate"))
                quickJobDetail.setcDate(jsonObject.optString("cDate"));
            if (jsonObject.has("status"))
                quickJobDetail.setStatus(jsonObject.optString("status"));

            details.add(quickJobDetail);
        }
        return details;
    }
}
