package com.scorpio.myexpensemanager.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.scorpio.myexpensemanager.db.Dao.CompanyDao;
import com.scorpio.myexpensemanager.db.vo.Company;

import junit.extensions.TestDecorator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by User on 07-02-2018.
 */
@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private CompanyDao companyDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void testFindCompany() {
        Company company = new Company();
        company.setName("MyCompany");

        mDb.companyDao().save(company);

        Company result = mDb.companyDao().findCompanyByName("MyCompany");

        assertEquals(result.getName(), company.getName());
    }


}