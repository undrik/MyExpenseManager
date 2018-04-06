package com.scorpio.myexpensemanager.db.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class VoucherType {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String name;
    public Long currentVoucherNo;
    public String prefix;
    public String postfix;
    private String type;
    private String delimiter;

    public VoucherType() {
        super();
    }

    @Ignore
    public VoucherType(String name, Long currentVoucherNo) {
        this.name = name;
        this.currentVoucherNo = currentVoucherNo;
    }

    @Ignore
    public VoucherType(String name, Long currentVoucherNo, String delimiter) {
        this.name = name;
        this.currentVoucherNo = currentVoucherNo;
        this.delimiter = delimiter;
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

    public Long getCurrentVoucherNo() {
        return currentVoucherNo;
    }

    public void setCurrentVoucherNo(Long currentVoucherNo) {
        this.currentVoucherNo = currentVoucherNo;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimeter) {
        this.delimiter = delimeter;
    }

    @Override
    public String toString() {
        return "VoucherType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentVoucherNo=" + currentVoucherNo +
                ", prefix='" + prefix + '\'' +
                ", postfix='" + postfix + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

