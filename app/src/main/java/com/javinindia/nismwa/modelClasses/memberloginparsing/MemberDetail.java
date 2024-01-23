package com.javinindia.nismwa.modelClasses.memberloginparsing;

import java.util.ArrayList;

/**
 * Created by Ashish on 05-07-2017.
 */

public class MemberDetail {
    private String memberId;
    private String name;
    private String password;
    private String firm_no;
    private String mobileNumber;
    private String landlineNumber;
    private String officeContactNumber;
    private String email;
    private String address;
    private String imageUrl;
    private String type;
    private String deals_in_1;
    private String deals_in_2;
    private String deals_in_3;
    private String deals_in_4;
    private String status;
    private String insertDate;
    private String updateDate;
    private String residence;
    private String deviceToken;
    private String website;
    private String login_type;
    private String type_name;
    private String firm_name;
    private ArrayList<MemberDetail> memberDetailArrayList;

    public ArrayList<MemberDetail> getMemberDetailArrayList() {
        return memberDetailArrayList;
    }

    public void setMemberDetailArrayList(ArrayList<MemberDetail> memberDetailArrayList) {
        this.memberDetailArrayList = memberDetailArrayList;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDeals_in_3() {
        return deals_in_3;
    }

    public void setDeals_in_3(String deals_in_3) {
        this.deals_in_3 = deals_in_3;
    }

    public String getDeals_in_4() {
        return deals_in_4;
    }

    public void setDeals_in_4(String deals_in_4) {
        this.deals_in_4 = deals_in_4;
    }

    public String getDeals_in_1() {
        return deals_in_1;
    }

    public void setDeals_in_1(String deals_in_1) {
        this.deals_in_1 = deals_in_1;
    }

    public String getDeals_in_2() {
        return deals_in_2;
    }

    public void setDeals_in_2(String deals_in_2) {
        this.deals_in_2 = deals_in_2;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirm_no() {
        return firm_no;
    }

    public void setFirm_no(String firm_no) {
        this.firm_no = firm_no;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLandlineNumber() {
        return landlineNumber;
    }

    public void setLandlineNumber(String landlineNumber) {
        this.landlineNumber = landlineNumber;
    }

    public String getOfficeContactNumber() {
        return officeContactNumber;
    }

    public void setOfficeContactNumber(String officeContactNumber) {
        this.officeContactNumber = officeContactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getFirm_name() {
        return firm_name;
    }

    public void setFirm_name(String firm_name) {
        this.firm_name = firm_name;
    }
}
