package com.javinindia.nismwa.modelClasses.eventnewsparsing;

import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 06-07-2017.
 */

public class EventNewsRespons extends ApiBaseData {
    ArrayList<EventDetail> eventDetailArrayList;
    private ArrayList<AdsDetail> adsDetailArrayList;

    public ArrayList<EventDetail> getEventDetailArrayList() {
        return eventDetailArrayList;
    }

    public void setEventDetailArrayList(ArrayList<EventDetail> eventDetailArrayList) {
        this.eventDetailArrayList = eventDetailArrayList;
    }

    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setEventDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
            if (jsonObject.has("ads") && jsonObject.optJSONArray("ads") != null)
                setAdsDetailArrayList(getAdsDetailMethod(jsonObject.optJSONArray("ads")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<EventDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<EventDetail> details = new ArrayList<>();
        EventDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new EventDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("title"))
                tanentDetail.setTitle(jsonObject.optString("title"));
            if (jsonObject.has("date"))
                tanentDetail.setDate(jsonObject.optString("date"));
            if (jsonObject.has("time"))
                tanentDetail.setTime(jsonObject.optString("time"));
            if (jsonObject.has("description"))
                tanentDetail.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));

            if (jsonObject.has("images") && jsonObject.optJSONArray("images") != null)
                tanentDetail.setImageDetailArrayList(getImageDetailMethod(jsonObject.optJSONArray("images")));

            details.add(tanentDetail);
        }
        return details;
    }

    private ArrayList<ImageDetail> getImageDetailMethod(JSONArray jsonArray) {
        ArrayList<ImageDetail> details = new ArrayList<>();
        ImageDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new ImageDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("event_id"))
                tanentDetail.setEvent_id(jsonObject.optString("event_id"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("image"))
                tanentDetail.setImage(jsonObject.optString("image"));
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
