package com.javinindia.nismwa.modelClasses.eventnewsparsing;

import java.util.ArrayList;

/**
 * Created by Ashish on 06-07-2017.
 */

public class EventDetail {
    private String id;
    private String title;
    private String date;
    private String time;
    private String description;
    private String address;
    private String type;
    private ArrayList<ImageDetail> imageDetailArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ImageDetail> getImageDetailArrayList() {
        return imageDetailArrayList;
    }

    public void setImageDetailArrayList(ArrayList<ImageDetail> imageDetailArrayList) {
        this.imageDetailArrayList = imageDetailArrayList;
    }
}
