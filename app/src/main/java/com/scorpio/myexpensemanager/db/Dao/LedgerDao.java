package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scorpio.myexpensemanager.db.vo.IdTuple;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.Name;

import java.util.List;

/**
 * Dao class to handle Ledger Entity
 * Created by User on 24-02-2018.
 */

@Dao
public interface LedgerDao {
    @Query("SELECT * FROM Ledger WHERE name = :name")
    LiveData<Ledger> findLedgerByNameLD(final String name);

    @Query("SELECT * FROM Ledger WHERE name = :name")
    Ledger findLedgerByName(final String name);

    @Query("SELECT * FROM Ledger WHERE id = :id")
    LiveData<Ledger> findLedgerByIdLD(final Long id);

    @Query("SELECT * FROM Ledger WHERE id = :id")
    Ledger findLedgerById(final Long id);

    @Query("SELECT * FROM Ledger ORDER BY name")
    List<Ledger> findAllLedgers();

    @Query("SELECT name FROM Ledger")
    List<Name> findAllLedgerNames();

    @Query("SELECT * FROM Ledger ORDER BY name")
    LiveData<List<Ledger>> findAll();

    @Insert
    List<Long> save(final List<Ledger> ledgers);

    @Insert
    List<Long> save(final Ledger... ledgers);

    @Insert
    Long save(final Ledger ledger);

    @Update
    int update(final Ledger ledger);

    @Delete
    int delete(final Ledger ledger);
}
