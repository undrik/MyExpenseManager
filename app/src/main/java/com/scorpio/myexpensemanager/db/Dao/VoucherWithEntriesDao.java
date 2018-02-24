package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

/**
 * Created by User on 24-02-2018.
 */

@Dao
public interface VoucherWithEntriesDao {
    @Transaction
    @Query("SELECT * FROM Voucher")
    LiveData<VoucherWithEntries> findVoucherWithEntries();
}
