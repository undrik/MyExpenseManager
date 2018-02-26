package com.scorpio.myexpensemanager.db.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.vo.AccountGroup;

import java.util.List;

/**
 * Created by User on 24-02-2018.
 */

@Dao
public interface AccountGroupDao {
    @Query("SELECT * FROM AccountGroup WHERE name = :name")
    LiveData<AccountGroup> findAccountGroupByName(@NonNull final String name);

    @Query("SELECT * FROM AccountGroup")
    LiveData<List<AccountGroup>> findAll();

    @Insert
    List<Long> save(@NonNull final List<AccountGroup> groups);

    @Insert
    List<Long> save(@NonNull final AccountGroup... groups);

    @Insert
    long save(@NonNull final AccountGroup company);

    @Update
    int update(@NonNull final AccountGroup company);

    @Delete
    int delete(@NonNull final AccountGroup company);
}