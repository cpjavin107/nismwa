package com.javinindia.nismwa.modelClasses.notificationparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.usefullInfoparsing.UsefullMasterDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashis on 9/23/2017.
 */

public class NotificationResponse extends ApiBaseData {
    ArrayList<NotificationDetail> notificationDetails = new ArrayList();

    public ArrayList<NotificationDetail> getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(ArrayList<NotificationDetail> notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setNotificationDetails(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<NotificationDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<NotificationDetail> details = new ArrayList<>();
        NotificationDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new NotificationDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("title"))
                tanentDetail.setTitle(jsonObject.optString("title"));
            if (jsonObject.has("description"))
                tanentDetail.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));

            details.add(tanentDetail);
        }
        return details;
    }
}
