package com.scorpio.myexpensemanager.db.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Ledger implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Long id = null;
    private String name = null;
    //    private Long groupId = null;
    private String groupName = null;
    //    @TypeConverters(NatureOfGroupEnumConverter.class)
//    private NatureOfGroup groupType = null;
    private String barcode = null;
    private Double openingBalance = null;
    private Long openingBalanceAsOn = null;
    private Double currentBalance = null;
    private Long currentBalanceAsOn = null;
    private Boolean active;
    private Long attachmentId = null;
    private Long addressId = null;
    private Long bankAccountId = null;
    private Long taxId = null;
//    private Attachment attachment = null;

    public Ledger() {
        super();
    }

    public Ledger(@NonNull final String name, @NonNull final String gName, @NonNull final Double
            OpenBalance, @NonNull final Long openningBalanceAsOn) {
        this.name = name;
        this.groupName = gName;
        this.openingBalance = OpenBalance;
        this.openingBalanceAsOn = openningBalanceAsOn;
        this.currentBalance = OpenBalance;
        this.currentBalanceAsOn = openningBalanceAsOn;
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

//    public Long getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(Long groupId) {
//        this.groupId = groupId;
//    }

    public Double getOpeningBalance() {
        return openingBalance;
    }


    public Long getOpeningBalanceAsOn() {
        return openingBalanceAsOn;
    }

    public void setOpeningBalanceAsOn(Long openingBalanceAsOn) {
        this.openingBalanceAsOn = openingBalanceAsOn;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Long getCurrentBalanceAsOn() {
        return currentBalanceAsOn;
    }

    public void setCurrentBalanceAsOn(Long currentBalanceAsOn) {
        this.currentBalanceAsOn = currentBalanceAsOn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

//    public NatureOfGroup getGroupType() {
//        return groupType;
//    }
//
//    public void setGroupType(NatureOfGroup groupType) {
//        this.groupType = groupType;
//    }

    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

//    public Attachment getAttachment() {
//        return attachment;
//    }
//
//    public void setAttachment(Attachment attachment) {
//        this.attachment = attachment;
//    }

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

}

