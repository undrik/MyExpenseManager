package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scorpio.myexpensemanager.db.vo.Voucher;

import java.util.List;

/**
 * Dao to handle Voucher entity
 * Created by User on 24-02-2018.
 */

@Dao
public interface VoucherDao {

    @Query("SELECT * FROM Voucher WHERE id = :id")
    LiveData<Voucher> findVoucherById(final Integer id);

    @Query("SELECT * FROM Voucher")
    LiveData<List<Voucher>> findAll();

    @Insert
    List<Long> save(final List<Voucher> vouchers);

    @Insert
    List<Long> save(final Voucher... vouchers);

    @Insert
    Long save(final Voucher voucher);

    @Update
    int update(final Voucher voucher);

    @Delete
    int delete(final Voucher voucher);
}
