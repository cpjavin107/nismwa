package com.javinindia.nismwa.modelClasses.commentparsing;

import com.javinindia.nismwa.modelClasses.memberloginparsing.MemberDetail;

import java.util.ArrayList;

/**
 * Created by Ashish on 28-06-2017.
 */

public class CommentDetail {
    private String id;
    private String postId;
    private String description;
    private String type;
    private String insertDate;
    private String updatedDate;
    private String c_userid;
    private String login_type;
    private String societyId;
    private String status;
    private String memberId;
    private String commentCount;
    private ArrayList<MemberDetail> memberDetailArrayList;

    public ArrayList<MemberDetail> getMemberDetailArrayList() {
        return memberDetailArrayList;
    }

    public void setMemberDetailArrayList(ArrayList<MemberDetail> memberDetailArrayList) {
        this.memberDetailArrayList = memberDetailArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getC_userid() {
        return c_userid;
    }

    public void setC_userid(String c_userid) {
        this.c_userid = c_userid;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getSocietyId() {
        return societyId;
    }

    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
