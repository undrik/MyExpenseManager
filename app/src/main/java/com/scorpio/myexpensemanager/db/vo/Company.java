package com.scorpio.myexpensemanager.db.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.converters.TimestampConverter;

import java.io.Serializable;
import java.util.Date;


@Entity
@TypeConverters(TimestampConverter.class)
public class Company implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    @ColumnInfo(index = true)
    private String name;
    private String address1;
    private String address2;
    private String state;
    private String country;
    private String pincode;
    private String email;
    private String phone;
    private String tan;
    private String pan;
    private String aadhar;
    private Date finYearStart;
    private Date finYearEnd;
    private Date bookStart;
    private String currencyId;
    private boolean isPasswordProtected;
    private String password;
    private String dbName;
    private Date modifiedDate;

    public Company() {

        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFinYearStart() {
        return finYearStart;
    }

    public void setFinYearStart(Date finYearStart) {
        this.finYearStart = finYearStart;
    }

    public Date getFinYearEnd() {
        return finYearEnd;
    }

    public void setFinYearEnd(Date finYearEnd) {
        this.finYearEnd = finYearEnd;
    }

    public Date getBookStart() {
        return bookStart;
    }

    public void setBookStart(Date bookStart) {
        this.bookStart = bookStart;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTan() {
        return tan;
    }

    public void setTan(String tan) {
        this.tan = tan;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.isPasswordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

