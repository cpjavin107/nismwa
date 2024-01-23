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

public class CountryResponse extends ApiBaseData {

    private ArrayList<CountryDetail> detailsArrayList;

    public ArrayList<CountryDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<CountryDetail> detailsArrayList) {
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

    private ArrayList<CountryDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<CountryDetail> details = new ArrayList<>();
        CountryDetail quickJobDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            quickJobDetail = new CountryDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("countryId"))
                quickJobDetail.setCountryId(jsonObject.optString("countryId"));
            if (jsonObject.has("countryName"))
                quickJobDetail.setCountryName(jsonObject.optString("countryName"));
            if (jsonObject.has("cDate"))
                quickJobDetail.setcDate(jsonObject.optString("cDate"));
            if (jsonObject.has("status"))
                quickJobDetail.setStatus(jsonObject.optString("status"));

            details.add(quickJobDetail);
        }
        return details;
    }
}
