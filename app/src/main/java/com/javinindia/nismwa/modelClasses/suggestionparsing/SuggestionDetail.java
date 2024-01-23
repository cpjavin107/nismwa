package com.javinindia.nismwa.modelClasses.suggestionparsing;

import java.util.ArrayList;

/**
 * Created by ashis on 11/21/2017.
 */

public class SuggestionDetail {
    private String id;
    private String c_title;
    private String memberId;
    private String complain;
    private String insertDate;
    private String updateDate;
    private String status;
    private ArrayList<SuggestionReply> suggestionReplyArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getC_title() {
        return c_title;
    }

    public void setC_title(String c_title) {
        this.c_title = c_title;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getComplain() {
        return complain;
    }

    public void setComplain(String complain) {
        this.complain = complain;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<SuggestionReply> getSuggestionReplyArrayList() {
        return suggestionReplyArrayList;
    }

    public void setSuggestionReplyArrayList(ArrayList<SuggestionReply> suggestionReplyArrayList) {
        this.suggestionReplyArrayList = suggestionReplyArrayList;
    }
}
