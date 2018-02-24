package com.scorpio.myexpensemanager.db.vo;

/**
 * Created by hkundu on 01-08-2016.
 * for My Expense Manager
 */
public class Tax {
    private Long id;
    private String pan;
    private String tan;
    private String vat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }
}
