package com.scorpio.myexpensemanager.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.db.vo.VoucherType;

import java.util.List;

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
            voucherTypeLiveData = companyDb.voucherTypeDao().findAll();
        }
    }

    public LiveData<List<VoucherType>> fetchAllVoucherType() {
        return voucherTypeLiveData;
    }
}
