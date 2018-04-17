package com.scorpio.myexpensemanager.db.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.TypeConverters;

import com.scorpio.myexpensemanager.db.converters.LocalDateEpochConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

@TypeConverters(LocalDateEpochConverter.class)
public class VoucherWithEntries extends Voucher implements Serializable {

    @Relation(parentColumn = "id", entityColumn = "voucherId", entity = VoucherEntry.class)
    public List<VoucherEntry> voucherEntries = new ArrayList<>();

    public List<VoucherEntry> getVoucherEntries() {
        return voucherEntries;
    }

    public void setVoucherEntries(List<VoucherEntry> voucherEntries) {
        this.voucherEntries = voucherEntries;
    }
}
