package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.vo.Voucher;

import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

@Dao
public interface VoucherDao {
    @Query("SELECT * FROM Voucher WHERE id = :id")
    LiveData<Voucher> findVoucherById(@NonNull final Integer id);

    @Query("SELECT * FROM Voucher")
    LiveData<List<Voucher>> findAll();

    @Insert
    void save(@NonNull final List<Voucher> vouchers);

    @Insert
    long save(@NonNull final Voucher voucher);

    @Update
    int update(@NonNull final Voucher voucher);

    @Delete
    int delete(@NonNull final Voucher voucher);
}
