package com.scorpio.myexpensemanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scorpio.myexpensemanager.db.Dao.CompanyDao;
import com.scorpio.myexpensemanager.db.vo.Company;

/**
 * Created by User on 06-02-2018.
 */

@Database(entities = {Company.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase EXPENSE_DB_INSTANCE;

    public abstract CompanyDao companyDao();

    public static AppDatabase getDatabase(Context context) {
        if (null == EXPENSE_DB_INSTANCE) {
            String DB_NAME = "myexpensemanager.db";
            EXPENSE_DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).build();
        }
        return EXPENSE_DB_INSTANCE;
    }
}
