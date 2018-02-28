package com.scorpio.myexpensemanager.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.listeners.OnResultListener;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Company;

import java.util.ArrayList;
import java.util.List;

/**
 * View model for AccountGroup
 * Created by User on 24-02-2018.
 */

public class AccountGroupViewModel extends AndroidViewModel {
    private CompanyDb companyDb;
    private LiveData<List<AccountGroup>> groupLiveData;
    private OnResultListener onResultListener;

    public AccountGroupViewModel(@NonNull Application application, @NonNull final Company company) {
        super(application);
        if (null != company.getDbName()) {
            companyDb = CompanyDb.getDatabase(getApplication(), company.getDbName());
            groupLiveData = companyDb.accountGroupDao().findAll();
        }
    }

    public void setOnResultListener(@NonNull final OnResultListener listener) {
        this.onResultListener = listener;
    }

    public LiveData<List<AccountGroup>> fetchAllAccountGroup() {
        return groupLiveData;
    }

    public void addAccountGroups(@NonNull AccountGroup... groups) {
        if (null != companyDb) {
            new AccountGroupTask(companyDb).execute(groups);
        }
    }

    public void addAccountGroup(@NonNull AccountGroup group) {
        if (null != companyDb) {
            new AccountGroupTask(companyDb).execute(group);
        }
    }

    private class AccountGroupTask extends AsyncTask<AccountGroup, Void, List<Long>> {
        private CompanyDb companyDb;

        AccountGroupTask(@NonNull final CompanyDb companyDb) {
            this.companyDb = companyDb;
        }

        @Override
        protected List<Long> doInBackground(AccountGroup... accountGroups) {
            if (accountGroups.length > 0 && null != companyDb) {
                return companyDb.accountGroupDao().save(accountGroups);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(@NonNull List<Long> results) {
            if (null != onResultListener && results.size() > 0) {
                onResultListener.onResult((Long[]) results.toArray());
            }
        }
    }
}
