package com.javinindia.nismwa.modelClasses.commentparsing;

import com.javinindia.nismwa.modelClasses.base.ApiBaseData;
import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish on 28-06-2017.
 */

public class CommentResponse extends ApiBaseData {
    private ArrayList<CommentDetail> detailsArrayList;
    private String commentCount;

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<CommentDetail> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void setDetailsArrayList(ArrayList<CommentDetail> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
    }

    public void responseParseMethod(Object response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            setMsg(jsonObject.optString("msg"));
            setStatus(jsonObject.optInt("status"));
            setCommentCount(jsonObject.optString("commentCount"));
            if (jsonObject.optInt("status") == 1 && jsonObject.has("data") && jsonObject.optJSONArray("data") != null)
                setDetailsArrayList(getDetailMethod(jsonObject.optJSONArray("data")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<CommentDetail> getDetailMethod(JSONArray jsonArray) {
        ArrayList<CommentDetail> details = new ArrayList<>();
        CommentDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new CommentDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("id"))
                tanentDetail.setId(jsonObject.optString("id"));
            if (jsonObject.has("postId"))
                tanentDetail.setPostId(jsonObject.optString("postId"));
            if (jsonObject.has("description"))
                tanentDetail.setDescription(jsonObject.optString("description"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("c_userid"))
                tanentDetail.setC_userid(jsonObject.optString("c_userid"));
            if (jsonObject.has("societyId"))
                tanentDetail.setSocietyId(jsonObject.optString("societyId"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updatedDate"))
                tanentDetail.setUpdatedDate(jsonObject.optString("updatedDate"));
            if (jsonObject.has("login_type"))
                tanentDetail.setLogin_type(jsonObject.optString("login_type"));
            if (jsonObject.has("commentCount"))
                tanentDetail.setCommentCount(jsonObject.optString("commentCount"));

            if (jsonObject.has("memberInformation") && jsonObject.optJSONArray("memberInformation") != null)
                tanentDetail.setMemberDetailArrayList(getMemberDetail(jsonObject.optJSONArray("memberInformation")));


            details.add(tanentDetail);
        }
        return details;
    }

    private ArrayList<MemberDetail> getMemberDetail(JSONArray jsonArray) {
        ArrayList<MemberDetail> details = new ArrayList<>();
        MemberDetail tanentDetail;
        for (int i = 0; i < jsonArray.length(); i++) {
            tanentDetail = new MemberDetail();
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            if (jsonObject.has("memberId"))
                tanentDetail.setMemberId(jsonObject.optString("memberId"));
            if (jsonObject.has("name"))
                tanentDetail.setName(jsonObject.optString("name"));
            if (jsonObject.has("password"))
                tanentDetail.setPassword(jsonObject.optString("password"));
            if (jsonObject.has("firm_no"))
                tanentDetail.setFirm_no(jsonObject.optString("firm_no"));
            if (jsonObject.has("mobileNumber"))
                tanentDetail.setMobileNumber(jsonObject.optString("mobileNumber"));
            if (jsonObject.has("landlineNumber"))
                tanentDetail.setLandlineNumber(jsonObject.optString("landlineNumber"));
            if (jsonObject.has("officeContactNumber"))
                tanentDetail.setOfficeContactNumber(jsonObject.optString("officeContactNumber"));
            if (jsonObject.has("email"))
                tanentDetail.setEmail(jsonObject.optString("email"));
            if (jsonObject.has("address"))
                tanentDetail.setAddress(jsonObject.optString("address"));
            if (jsonObject.has("imageUrl"))
                tanentDetail.setImageUrl(jsonObject.optString("imageUrl"));
            if (jsonObject.has("type"))
                tanentDetail.setType(jsonObject.optString("type"));
            if (jsonObject.has("deals_in_1"))
                tanentDetail.setDeals_in_1(jsonObject.optString("deals_in_1"));
            if (jsonObject.has("deals_in_2"))
                tanentDetail.setDeals_in_2(jsonObject.optString("deals_in_2"));
            if (jsonObject.has("status"))
                tanentDetail.setStatus(jsonObject.optString("status"));
            if (jsonObject.has("insertDate"))
                tanentDetail.setInsertDate(jsonObject.optString("insertDate"));
            if (jsonObject.has("updateDate"))
                tanentDetail.setUpdateDate(jsonObject.optString("updateDate"));
            if (jsonObject.has("residence"))
                tanentDetail.setResidence(jsonObject.optString("residence"));
            if (jsonObject.has("deviceToken"))
                tanentDetail.setDeviceToken(jsonObject.optString("deviceToken"));
            if (jsonObject.has("login_type"))
                tanentDetail.setLogin_type(jsonObject.optString("login_type"));
            if (jsonObject.has("type_name"))
                tanentDetail.setType_name(jsonObject.optString("type_name"));
            if (jsonObject.has("firm_name"))
                tanentDetail.setFirm_name(jsonObject.optString("firm_name"));

            details.add(tanentDetail);
        }
        return details;
    }
}
