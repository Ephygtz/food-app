package com.wrx.quickeats.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mobulous55 on 23/1/18.
 */

public class MPesaResponse
{
    @SerializedName("sid")
    String sid="";

    @SerializedName("oid")
    String oid="";

    @SerializedName("amount")
    String amt="";

    @SerializedName("account")
    String account="";

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
