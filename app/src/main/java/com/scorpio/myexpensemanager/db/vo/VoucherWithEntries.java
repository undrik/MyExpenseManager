package com.scorpio.myexpensemanager.db.vo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

public class VoucherWithEntries {
    @Embedded
    public Voucher voucher;
    @Relation(parentColumn = "id", entityColumn = "voucherId", entity = VoucherEntry.class)
    public List<VoucherEntry> voucherEntries;
}
