package com.scorpio.myexpensemanager.commons.sms;

import java.util.ArrayList;
import java.util.List;

public class SmsConfig {
    private String address;
    private String voucherType;
    private String drLederName;
    private String crLedgerName;
    private List<String> patterns = new ArrayList<>();

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

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        return "SmsConfig{" +
                "address='" + address + '\'' +
                ", voucherType='" + voucherType + '\'' +
                ", drLederName='" + drLederName + '\'' +
                ", crLedgerName='" + crLedgerName + '\'' +
                ", patterns=" + patterns +
                '}';
    }
}
