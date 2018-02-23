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

@Database(entities = {Company.class}, version = 1)
public abstract class CompanyDb extends RoomDatabase {
    private static CompanyDb COMPANY_DB_INSTANCE;

    public abstract CompanyDao companyDao();

    public static CompanyDb getDatabase(Context context, String dbName) {
        if (null == COMPANY_DB_INSTANCE) {
            COMPANY_DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CompanyDb
                    .class, dbName).build();
        }
        return COMPANY_DB_INSTANCE;
    }
}
