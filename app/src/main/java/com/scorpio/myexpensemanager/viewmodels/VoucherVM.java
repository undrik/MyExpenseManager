package com.scorpio.myexpensemanager.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.IdTuple;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by hkundu on 13-03-2018.
 */

@SuppressLint("NewApi")
public class VoucherVM extends AndroidViewModel {
    private CompanyDb companyDb;
    private LiveData<List<VoucherWithEntries>> vouchersLd;

    public VoucherVM(@NonNull Application application) {
        super(application);
        companyDb = CompanyDb.getDatabase(application, Cache.getCompany().getDbName());
        vouchersLd = companyDb.voucherWithEntriesDao().findVoucherWithEntries();
    }

    public LiveData<List<VoucherWithEntries>> findVoucherWithEntries() {
        return vouchersLd;
    }

    public List<VoucherWithEntries> getVoucherByMinMaxDate(final Date voucherMinDate, final Date
            voucherMaxDate) {
        List<VoucherWithEntries> result = new ArrayList<>();

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<List<VoucherWithEntries>> future = taskExecutor.submit(() -> {
            Long minDate = Util.convertDateToLocalDate(voucherMinDate).toEpochDay();
            Long maxDate = Util.convertDateToLocalDate(voucherMaxDate).toEpochDay();
            return companyDb.voucherWithEntriesDao().findVoucherWithMinMaxDate(minDate, maxDate);
        });
        try {
            result = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }

        return result;
    }

    public IdTuple getVoucherIdByNumber(@NonNull String number) {
        IdTuple result = null;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<IdTuple> future = taskExecutor.submit(() ->
                companyDb.voucherWithEntriesDao().findVoucherIdByNumber(number)
        );

        try {
            result = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }
        return result;
    }

    public IdTuple getVoucherIdBySmdId(@NonNull String smsId) {
        IdTuple result = null;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<IdTuple> future = taskExecutor.submit(() ->
                companyDb.voucherWithEntriesDao().findVoucherIdBySmsId(smsId)
        );

        try {
            result = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }
        return result;
    }

    public Long addVoucher(@NonNull VoucherWithEntries voucher) {
        Long result = -1L;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<Long> future = taskExecutor.submit(() -> companyDb.voucherWithEntriesDao()
                .saveWithEntries(voucher));
        try {
            result = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }
        return result;
    }

    public Integer deleteVoucher(@NonNull VoucherWithEntries voucher) {
        Integer result = -1;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<Integer> future = taskExecutor.submit(() -> companyDb.voucherWithEntriesDao()
                .deleteWithEntry(voucher));
        try {
            result = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }
        return result;
    }

}
