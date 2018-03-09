package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scorpio.myexpensemanager.db.vo.VoucherType;

import java.util.List;

/**
 * Dao to handle VoucherType entity
 * Created by User on 06-03-2018.
 */

@Dao
public interface VoucherTypeDao {
    @Insert
    List<Long> save(List<VoucherType> voucherTypes);

    @Insert
    Long save(VoucherType voucherType);

    @Update
    int update(VoucherType voucherType);

    @Delete
    int delete(VoucherType voucherType);

    @Query("SELECT * FROM VoucherType")
    LiveData<List<VoucherType>> findAll();
}
