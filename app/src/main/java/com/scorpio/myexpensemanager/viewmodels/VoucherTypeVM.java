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
import com.scorpio.myexpensemanager.db.vo.VoucherType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * View model for VoucherType
 * Created by User on 06-03-2018.
 */

public class VoucherTypeVM extends AndroidViewModel {
    private CompanyDb companyDb;
    private LiveData<List<VoucherType>> voucherTypeLiveData;

    public VoucherTypeVM(@NonNull Application application) {
        super(application);
        if (null != Cache.getCompany()) {
            companyDb = CompanyDb.getDatabase(getApplication(), Cache.getCompany().getDbName());
            voucherTypeLiveData = companyDb.voucherTypeDao().findAllLd();
        }
    }

    public LiveData<List<VoucherType>> fetchAllVoucherTypeLiveData() {
        return voucherTypeLiveData;
    }

    public List<VoucherType> fetchAllVoucherType() {
        List<VoucherType> result = new ArrayList<>();
        TaskExecutor taskExecutor = new TaskExecutor();
        Future<List<VoucherType>> future = taskExecutor.submit(() -> {
            return companyDb.voucherTypeDao().findAll();
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

    public VoucherType fetchCurrentVoucherNo(@NonNull String name) {
        VoucherType voucherType = null;

        TaskExecutor taskExecutor = new TaskExecutor();
        Future<VoucherType> future = taskExecutor.submit(() -> companyDb.voucherTypeDao()
                .getCurrentVoucherNo(name));

        try {
            voucherType = future.get();
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
        } finally {
            taskExecutor.shutdown();
        }

        return voucherType;
    }
}
