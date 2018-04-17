package com.scorpio.myexpensemanager.commons.sms;

import android.support.annotation.NonNull;

public class SmsConfig {
    @NonNull
    private String address;
    @NonNull
    private String voucherType;
    @NonNull
    private String drLederName;
    @NonNull
    private String crLedgerName;
    @NonNull
    private String pattern;
    private int pareseFunction;

    public SmsConfig(String address, String voucherType, String drLederName, String crLedgerName) {
        this.address = address;
        this.voucherType = voucherType;
        this.drLederName = drLederName;
        this.crLedgerName = crLedgerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getDrLederName() {
        return drLederName;
    }

    public void setDrLederName(String drLederName) {
        this.drLederName = drLederName;
    }

    public String getCrLedgerName() {
        return crLedgerName;
    }

    public void setCrLedgerName(String crLedgerName) {
        this.crLedgerName = crLedgerName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getPareseFunction() {
        return pareseFunction;
    }

    public void setPareseFunction(int pareseFunction) {
        this.pareseFunction = pareseFunction;
    }

    @Override
    public String toString() {
        return "SmsConfig{" +
                "address='" + address + '\'' +
                ", voucherType='" + voucherType + '\'' +
                ", drLederName='" + drLederName + '\'' +
                ", crLedgerName='" + crLedgerName + '\'' +
                ", pattern=" + pattern +
                '}';
    }
}
