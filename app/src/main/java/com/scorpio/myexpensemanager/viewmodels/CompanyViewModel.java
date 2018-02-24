package com.scorpio.myexpensemanager.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.db.AppDatabase;
import com.scorpio.myexpensemanager.db.vo.Company;

import java.util.List;

/**
 * Created by hkundu on 17-02-2018.
 */

public class CompanyViewModel extends AndroidViewModel {

    private LiveData<List<Company>> companyLiveData;
    private AppDatabase appDb;

    public CompanyViewModel(@NonNull Application application) {
        super(application);

        appDb = AppDatabase.getDatabase(this.getApplication());
        companyLiveData = appDb.companyDao().findAll();
    }

    public LiveData<List<Company>> fetchAllCompany() {
        return companyLiveData;
    }

    public Company fetchCompanyByName(@NonNull final String name) {
        return appDb.companyDao().findCompanyByName(name);
    }

    public void addCompany(@NonNull final Company company) {
        if (null != company && null != company.getName()) {
            new AddCompanyTask(appDb).execute(company);
        }
    }

    public boolean isCompanyExists(@NonNull final String name) {
//        Company company = appDb.companyDao().findCompanyByName(name);
        return false;
    }


    private static class AddCompanyTask extends AsyncTask<Company, Void, Long> {
        private AppDatabase appDb;

        public AddCompanyTask(final AppDatabase appDb) {
            this.appDb = appDb;
        }

        @Override
        protected Long doInBackground(@NonNull Company... companies) {
            return appDb.companyDao().save(companies[0]);
        }
    }

    private static class DeleteCompanyTask extends AsyncTask<Company, Void, Integer> {

        private AppDatabase appDb;

        public DeleteCompanyTask(final AppDatabase appDb) {
            this.appDb = appDb;
        }

        @Override
        protected Integer doInBackground(@NonNull Company... companies) {
            return appDb.companyDao().delete(companies[0]);
        }
    }
}
