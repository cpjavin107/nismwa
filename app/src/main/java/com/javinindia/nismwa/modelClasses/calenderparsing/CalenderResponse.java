package com.javinindia.nismwa.modelClasses.calenderparsing;

import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.dealparsing.DealDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 17-08-2017.
 */

public class CalenderResponse extends ApiBaseData {
    ArrayList<CalenderDetail> calenderDetailArrayList;
    private ArrayList<AdsDetail> adsDetailArrayList;

    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }


    public ArrayList<CalenderDetail> getCalenderDetailArrayList() {
        return calenderDetailArrayList;
    }

    public void setCalenderDetailArrayList(ArrayList<CalenderDetail> calenderDetailArrayList) {
        this.calenderDetailArrayList = calenderDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setCalenderDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
            if (jsonObject.has("ads") && jsonObject.optJSONArray("ads") != null)
                setAdsDetailArrayList(getAdsDetailMethod(jsonObject.optJSONArray("ads")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CalenderDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<CalenderDetail> details = new ArrayList<>();
        CalenderDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new CalenderDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("year"))
                tanentDetail.setYear(jsonObject.optString("year"));
            if (jsonObject.has("datee"))
                tanentDetail.setDate(jsonObject.optString("datee"));
            if (jsonObject.has("Day"))
                tanentDetail.setDay(jsonObject.optString("Day"));
            if (jsonObject.has("occasion"))
                tanentDetail.setOccasion(jsonObject.optString("occasion"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));

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
