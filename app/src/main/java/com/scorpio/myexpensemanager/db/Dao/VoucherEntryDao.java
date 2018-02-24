package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;


import com.scorpio.myexpensemanager.db.vo.VoucherEntry;

import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

@Dao
public interface VoucherEntryDao {

    @Query("SELECT * FROM VoucherEntry")
    LiveData<List<VoucherEntry>> findAll();

    @Insert
    void save(@NonNull final List<VoucherEntry> voucherEntries);

    @Insert
    long save(@NonNull final VoucherEntry voucherEntry);

    @Update
    int update(@NonNull final VoucherEntry voucherEntry);

    @Delete
    int delete(@NonNull final VoucherEntry company);
}
