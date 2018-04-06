package com.scorpio.myexpensemanager.db.vo;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.converters.LocalDateEpochConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Voucher {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String number;
    private String type;
    @TypeConverters(LocalDateEpochConverter.class)
    private LocalDate localDate;
    private String narration;
    private String guid;
    private String smsid;
    @Ignore
    @NonNull
    private VoucherType voucherType;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
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

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }

    public String getSmsid() {
        return smsid;
    }

    public void setSmsid(String smsid) {
        this.smsid = smsid;
    }

    @Override
    public String toString() {
        return "{'id':" + this.id.toString() +
                ",'localDate':" + localDate.toString() +
                "}";
    }

    //    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        if (id != null) {
//            dest.writeLong(id);
//        }
//        if (number != null) {
//            dest.writeString(number);
//        }
//        if (typeId != null) {
//            dest.writeLong(typeId);
//        }
//        if (null != localDate)
//            dest.writeLong(localDate);
//        if (narration != null) {
//            dest.writeString(narration);
//        }
//
//        // dest.writeTypedList(voucherEntryList);
//
//        //dest.writeParcelableArray((Parcelable[]) getVoucherEntryList().toArray(), 0);
//    }

//    public static final Creator<Voucher> CREATOR
//            = new Creator<Voucher>() {
//        public Voucher createFromParcel(Parcel in) {
//            return new Voucher(in);
//        }
//
//        public Voucher[] newArray(int size) {
//            return new Voucher[size];
//        }
//    };
//
//    private Voucher(Parcel in) {
//        id = in.readLong();
//        number = in.readString();
//        typeId = in.readLong();
//        localDate = in.readLong();
//        narration = in.readString();
//
//        //in.readTypedList(voucherEntryList, VoucherEntry.CREATOR);
//    }
}

