package com.javinindia.nismwa.modelClasses.countryStateCityparsing;

/**
 * Created by Ashish on 27-04-2017.
 */

public class CountryDetail {
    private String countryId;
    private String countryName;
    private String cDate;
    private String status;

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
