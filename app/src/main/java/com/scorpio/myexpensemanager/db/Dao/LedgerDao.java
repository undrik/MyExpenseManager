package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;


import com.scorpio.myexpensemanager.db.vo.Ledger;

import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

@Dao
public interface LedgerDao {
    @Query("SELECT * FROM Ledger WHERE name = :name")
    LiveData<Ledger> findLedgerByName(@NonNull final String name);

    @Query("SELECT * FROM Ledger WHERE id = :id")
    LiveData<Ledger> findLedgerById(@NonNull final Integer id);

    @Query("SELECT * FROM Ledger")
    LiveData<List<Ledger>> findAll();

    @Insert
    List<Long> save(@NonNull final List<Ledger> ledgers);

    @Insert
    List<Long> save(@NonNull final Ledger... ledgers);

    @Insert
    Long save(@NonNull final Ledger ledger);

    @Update
    int update(final Ledger ledger);

    @Delete
    int delete(final Ledger ledger);
}
