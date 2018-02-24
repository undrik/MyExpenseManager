package com.scorpio.myexpensemanager.db.vo;

/**
 * Created by hkundu on 01-08-2016.
 * for My Expense Manager
 */
public class BankAccount {
    private Long id;
    private String accountNo;
    private String branchName;
    private String bsrCode;
    private String contactPerson;
    private String micrCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBsrCode() {
        return bsrCode;
    }

    public void setBsrCode(String bsrCode) {
        this.bsrCode = bsrCode;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getMicrCode() {
        return micrCode;
    }

    public void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }
}
