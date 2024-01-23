package com.javinindia.nismwa.modelClasses.suggestionparsing;

import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashis on 11/21/2017.
 */

public class SuggestionResponse extends ApiBaseData {
    private ArrayList<SuggestionDetail> suggestionDetailArrayList;
    private ArrayList<AdsDetail> adsDetailArrayList;

    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }

    public ArrayList<SuggestionDetail> getSuggestionDetailArrayList() {
        return suggestionDetailArrayList;
    }

    public void setSuggestionDetailArrayList(ArrayList<SuggestionDetail> suggestionDetailArrayList) {
        this.suggestionDetailArrayList = suggestionDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setSuggestionDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
            if (jsonObject.has("ads") && jsonObject.optJSONArray("ads") != null)
                setAdsDetailArrayList(getAdsDetailMethod(jsonObject.optJSONArray("ads")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SuggestionDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<SuggestionDetail> details = new ArrayList<>();
        SuggestionDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new SuggestionDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("c_title"))
                tanentDetail.setC_title(jsonObject.optString("c_title"));
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("complain"))
                tanentDetail.setComplain(jsonObject.optString("complain"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));

            if (jsonObject.has("parkingInformation") && jsonObject.optJSONArray("parkingInformation") != null)
                tanentDetail.setSuggestionReplyArrayList(getReplyMethod(jsonObject.optJSONArray("parkingInformation")));

            details.add(tanentDetail);
        }
        return details;
    }

    private ArrayList<SuggestionReply> getReplyMethod(JSONArray jsonArray) {
        ArrayList<SuggestionReply> details = new ArrayList<>();
        SuggestionReply tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new SuggestionReply();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("replyId"))
                tanentDetail.setReplyId(jsonObject.optString("replyId"));
            if (jsonObject.has("complainId"))
                tanentDetail.setComplainId(jsonObject.optString("complainId"));
            if (jsonObject.has("replyDescription"))
                tanentDetail.setReplyDescription(jsonObject.optString("replyDescription"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
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
