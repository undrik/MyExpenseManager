package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scorpio.myexpensemanager.db.vo.VoucherEntry;

import java.util.List;

/**
 * Dao to handle VoucherEntry entity
 * Created by User on 24-02-2018.
 */

@Dao
public interface VoucherEntryDao {

    @Query("SELECT * FROM VoucherEntry")
    LiveData<List<VoucherEntry>> findAll();

    @Insert
    List<Long> save(final List<VoucherEntry> voucherEntries);

    @Insert
    List<Long> save(final VoucherEntry... voucherEntries);

    @Insert
    Long save(final VoucherEntry voucherEntry);

    @Update
    int update(final VoucherEntry voucherEntry);

    @Delete
    int delete(final VoucherEntry company);
}
