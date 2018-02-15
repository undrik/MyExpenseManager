package com.scorpio.myexpensemanager.db.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.content.Intent;

import com.scorpio.myexpensemanager.db.vo.Company;

/**
 * Created by User on 06-02-2018.
 */

@Dao
public interface CompanyDao {
    @Query("SELECT * FROM Company WHERE name = :name")
    Company findCompanyByName(final String name);

    @Insert
    long insert(final Company company);

    @Update
    int update(final Company company);

    @Delete
    int delete(final Company company);


}
