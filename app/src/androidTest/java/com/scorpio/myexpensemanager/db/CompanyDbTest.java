package com.scorpio.myexpensemanager.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;

/**
 * Created by User on 28-02-2018.
 */
@RunWith(AndroidJUnit4.class)
public class CompanyDbTest {
    private CompanyDb companyDb;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        companyDb = Room.inMemoryDatabaseBuilder(context, CompanyDb.class).build();
    }

    @After
    public void tearDown() throws Exception {
        companyDb.close();
    }

    @Test
    public void saveWithVoucherEntryTest() throws Exception {
        Ledger ledger = new Ledger();
        ledger.setId(1L);
        companyDb.ledgerDao().save(ledger);

        ledger = new Ledger();
        ledger.setId(2L);
        companyDb.ledgerDao().save(ledger);

        Voucher voucher = new Voucher();
        voucher.setNumber("1");
        voucher.setNarration("Test");
        voucher.setLocalDate(LocalDate.now());
        Long voucherId = 1L;
//        companyDb.voucherWithEntriesDao().save(voucher);

        VoucherEntry voucherEntry = new VoucherEntry();
//        voucherEntry.setVoucherId(voucherId);
        voucherEntry.setLedgerId(1L);
        voucherEntry.setDebitOrCredit(0);
        voucherEntry.setAmount(-1000D);
        voucherEntry.setLocalDate(LocalDate.now());
        voucher.getVoucherEntryList().add(voucherEntry);

        voucherEntry = new VoucherEntry();
//        voucherEntry.setVoucherId(voucherId);
        voucherEntry.setLedgerId(2L);
        voucherEntry.setDebitOrCredit(1);
        voucherEntry.setAmount(1000D);
        voucherEntry.setLocalDate(LocalDate.now());
        voucher.getVoucherEntryList().add(voucherEntry);
        companyDb.voucherWithEntriesDao().saveWithEntries(voucher);

        companyDb.voucherWithEntriesDao().findVoucherWithEntries().observeForever(
                (voucherWithEntries) -> {
                    voucherWithEntries.forEach((v) -> {
                        System.out.println(v);
                        v.getVoucherEntryList().forEach(System.out::println);
                    });
                }
        );
    }
}