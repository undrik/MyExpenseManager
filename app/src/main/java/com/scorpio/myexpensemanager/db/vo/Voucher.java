package com.scorpio.myexpensemanager.db.vo;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * <!-- begin-user-doc -->
 * <!--  end-user-doc  -->
 *
 * @generated
 */

public class Voucher implements Parcelable {
    private Long id;
    private String number;
    private Long typeId;
    private Long dateLong;
    private String narration;
    private String guid;
    private ArrayList<VoucherEntry> voucherEntryList = new ArrayList<>();

    public Voucher() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getDateLong() {
        return dateLong;
    }

    public void setDate(long date) {
        this.dateLong = date;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public ArrayList<VoucherEntry> getVoucherEntryList() {
        return voucherEntryList;
    }

    public void setVoucherEntryList(ArrayList<VoucherEntry> voucherEntryList) {
        this.voucherEntryList = voucherEntryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id != null) {
            dest.writeLong(id);
        }
        if (number != null) {
            dest.writeString(number);
        }
        if (typeId != null) {
            dest.writeLong(typeId);
        }
        if (null != dateLong)
            dest.writeLong(dateLong);
        if (narration != null) {
            dest.writeString(narration);
        }

        // dest.writeTypedList(voucherEntryList);

        //dest.writeParcelableArray((Parcelable[]) getVoucherEntryList().toArray(), 0);
    }

    public static final Creator<Voucher> CREATOR
            = new Creator<Voucher>() {
        public Voucher createFromParcel(Parcel in) {
            return new Voucher(in);
        }

        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };

    private Voucher(Parcel in) {
        id = in.readLong();
        number = in.readString();
        typeId = in.readLong();
        dateLong = in.readLong();
        narration = in.readString();

        //in.readTypedList(voucherEntryList, VoucherEntry.CREATOR);
    }
}

