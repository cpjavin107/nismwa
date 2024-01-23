package com.javinindia.nismwa.modelClasses.dealparsing;

/**
 * Created by Ashish on 12-07-2017.
 */

public class DealDetail {
    private String dealsId;
    private String dealsName;
    private String cDate;
    private String status;

    public String getDealsId() {
        return dealsId;
    }

    public void setDealsId(String dealsId) {
        this.dealsId = dealsId;
    }

    public String getDealsName() {
        return dealsName;
    }

    public void setDealsName(String dealsName) {
        this.dealsName = dealsName;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
