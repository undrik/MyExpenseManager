package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scorpio.myexpensemanager.db.vo.AccountGroup;

import java.util.List;

/**
 * Dao to handle AccountGroup entity
 * Created by User on 24-02-2018.
 */

@Dao
public interface AccountGroupDao {
    @Query("SELECT * FROM AccountGroup WHERE name = :name")
    LiveData<AccountGroup> findAccountGroupByName(final String name);

    @Query("SELECT * FROM AccountGroup")
    LiveData<List<AccountGroup>> findAll();

    @Query("SELECT * FROM AccountGroup")
    List<AccountGroup> findAllGroups();

    @Query("SELECT name FROM AccountGroup")
    List<String> findAllGroupNames();

    @Insert
    List<Long> save(final List<AccountGroup> groups);

    @Insert
    List<Long> save(final AccountGroup... groups);

    @Insert
    Long save(final AccountGroup company);

    @Update
    int update(final AccountGroup company);

    @Delete
    int delete(final AccountGroup company);
}
