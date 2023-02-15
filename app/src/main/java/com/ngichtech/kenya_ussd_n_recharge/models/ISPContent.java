package com.ngichtech.kenya_ussd_n_recharge.models;

public class ISPContent {
    String ussdCodeName;
    String ussdCode;

    public ISPContent(String ussdCodeName, String ussdCode) {
        this.ussdCodeName = ussdCodeName;
        this.ussdCode = ussdCode;
    }

    public String getUssdCodeName() {
        return ussdCodeName;
    }

    public String getUssdCode() {
        return ussdCode;
    }
}
