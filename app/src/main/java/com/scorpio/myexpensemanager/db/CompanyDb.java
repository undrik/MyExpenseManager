package com.scorpio.myexpensemanager.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scorpio.myexpensemanager.db.Dao.AccountGroupDao;
import com.scorpio.myexpensemanager.db.Dao.CompanyDao;
import com.scorpio.myexpensemanager.db.Dao.LedgerDao;
import com.scorpio.myexpensemanager.db.Dao.VoucherDao;
import com.scorpio.myexpensemanager.db.Dao.VoucherEntryDao;
import com.scorpio.myexpensemanager.db.Dao.VoucherWithEntriesDao;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;

/**
 * Created by User on 06-02-2018.
 */

@Database(entities = {AccountGroup.class, Ledger.class, Voucher.class, VoucherEntry.class},
        version = 1)
public abstract class CompanyDb extends RoomDatabase {
    private static CompanyDb COMPANY_DB_INSTANCE;

    public abstract AccountGroupDao accountGroupDao();

    public abstract LedgerDao ledgerDao();

    public abstract VoucherDao voucherDao();

    public abstract VoucherEntryDao voucherEntryDao();

    public abstract VoucherWithEntriesDao voucherWithEntriesDao();

    public static CompanyDb getDatabase(Context context, String dbName) {
        if (null == COMPANY_DB_INSTANCE) {
            COMPANY_DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CompanyDb
                    .class, dbName).build();
        }
        return COMPANY_DB_INSTANCE;
    }
}
