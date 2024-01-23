package com.javinindia.nismwa.modelClasses.usefullInfoparsing;

import com.javinindia.nismwa.modelClasses.adsparsing.AdsDetail;
import com.javinindia.nismwa.modelClasses.base.ApiBaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 17-07-2017.
 */

public class UsefullListResponse extends ApiBaseData {
    private ArrayList<UsefullDetail> usefullDetailArrayList;
    private ArrayList<AdsDetail> adsDetailArrayList;

    public ArrayList<AdsDetail> getAdsDetailArrayList() {
        return adsDetailArrayList;
    }

    public void setAdsDetailArrayList(ArrayList<AdsDetail> adsDetailArrayList) {
        this.adsDetailArrayList = adsDetailArrayList;
    }


    public ArrayList<UsefullDetail> getUsefullDetailArrayList() {
        return usefullDetailArrayList;
    }

    public void setUsefullDetailArrayList(ArrayList<UsefullDetail> usefullDetailArrayList) {
        this.usefullDetailArrayList = usefullDetailArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setUsefullDetailArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
            if (jsonObject.has("ads") && jsonObject.optJSONArray("ads") != null)
                setAdsDetailArrayList(getAdsDetailMethod(jsonObject.optJSONArray("ads")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<UsefullDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<UsefullDetail> details = new ArrayList<>();
        UsefullDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new UsefullDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("informationId"))
                tanentDetail.setInformationId(jsonObject.optString("informationId"));
            if (jsonObject.has("usefullId"))
                tanentDetail.setUsefullId(jsonObject.optString("usefullId"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("contactNumber"))
                tanentDetail.setContactNumber(jsonObject.optString("contactNumber"));

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
