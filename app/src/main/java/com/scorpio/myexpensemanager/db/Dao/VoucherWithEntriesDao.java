package com.scorpio.myexpensemanager.db.Dao;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.content.Intent;
import android.database.Cursor;

import com.scorpio.myexpensemanager.db.vo.IdTuple;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherType;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import java.time.LocalDate;
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
    protected abstract List<Long> save(Voucher... vouchers);

    @Insert
    protected abstract Long save(Voucher voucher);

    @Insert
    protected abstract List<Long> save(VoucherEntry... voucherEntries);

    @Insert
    protected abstract List<Long> save(List<VoucherEntry> voucherEntries);

    @Update
    protected abstract int update(VoucherType voucherType);

    @Update
    protected abstract int update(Voucher voucher);

    @Delete
    protected abstract int delete(Voucher voucher);

    @Delete
    protected abstract int delete(List<VoucherEntry> voucherEntries);

    @SuppressLint("NewApi")
    @Transaction
    public Long saveWithEntries(VoucherWithEntries voucher) {
        voucher.setGuid(UUID.randomUUID().toString());
        Long voucherId = save(voucher);
        if (voucherId > 0) {
            List<VoucherEntry> voucherEntries = voucher.getVoucherEntries().stream().map
                    (voucherEntry -> {
                        voucherEntry.setVoucherId(voucherId);
                        voucherEntry.setLocalDate(voucher.getLocalDate());
                        return voucherEntry;
                    }).collect(Collectors.toList());
            save(voucherEntries);
            VoucherType voucherType = voucher.getVoucherType();
            voucherType.setCurrentVoucherNo(voucherType.getCurrentVoucherNo() + 1);
            update(voucherType);
        }
        return voucherId;
    }

    @Transaction
    public int deleteWithEntry(VoucherWithEntries voucherWithEntries) {
        Voucher voucher = voucherWithEntries;
        delete(voucherWithEntries.getVoucherEntries());
        return delete(voucher);
    }

    @Transaction
    @Query("SELECT * FROM Voucher ORDER BY localDate DESC")
    public abstract LiveData<List<VoucherWithEntries>> findVoucherWithEntries();

    @Query("SELECT id FROM Voucher WHERE number = :number")
    public abstract IdTuple findVoucherIdByNumber(String number);

    @Query("SELECT id FROM Voucher WHERE smsid = :smsId")
    public abstract IdTuple findVoucherIdBySmsId(String smsId);

    @Transaction
    @Query("SELECT * FROM Voucher WHERE localDate BETWEEN :minDate AND :maxDate ORDER BY " +
            "localDate DESC")
    public abstract List<VoucherWithEntries> findVoucherWithMinMaxDate(final Long minDate,
                                                                       final Long maxDate);

}
