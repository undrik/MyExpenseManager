package com.scorpio.myexpensemanager.db.Dao;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.content.Intent;
import android.database.Cursor;

import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Dao to handle VoucherWithEntries entity
 * Created by User on 24-02-2018.
 */

@Dao
public abstract class VoucherWithEntriesDao {
    @Insert
    public abstract List<Long> save(Voucher... vouchers);

    @Insert
    public abstract Long save(Voucher voucher);

    @Insert
    public abstract List<Long> save(VoucherEntry... voucherEntries);

    @Insert
    public abstract List<Long> save(List<VoucherEntry> voucherEntries);

    @SuppressLint("NewApi")
    @Transaction
    public Long saveWithEntries(Voucher voucher) {
        voucher.setGuid(UUID.randomUUID().toString());
        Long voucherId = save(voucher);
        List<VoucherEntry> voucherEntries = voucher.getVoucherEntryList().stream().map
                (voucherEntry -> {
                    voucherEntry.setVoucherId(voucherId);
                    return voucherEntry;
                }).collect(Collectors.toList());
        save(voucherEntries);
        return voucherId;
    }

    @Transaction
    @Query("SELECT * FROM Voucher")
    public abstract LiveData<List<VoucherWithEntries>> findVoucherWithEntries();

    @Query("SELECT seq FROM sqlite_sequence WHERE name = 'Voucher'")
    public abstract Cursor findVoucherSequence();

    public Integer fetchNextVoucherSequence() {
        Integer result = 1;
        Cursor cursor = findVoucherSequence();
        if (null != cursor && cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex("sequence"));
        }
        return result;
    }
}
