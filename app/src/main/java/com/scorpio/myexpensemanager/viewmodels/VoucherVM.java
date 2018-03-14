package com.scorpio.myexpensemanager.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by hkundu on 13-03-2018.
 */

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

    public Long addVoucher(@NonNull Voucher voucher) {
        Long result = -1L;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<Long> future = taskExecutor.submit(() -> {
            return companyDb.voucherWithEntriesDao().save(voucher);
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
}
