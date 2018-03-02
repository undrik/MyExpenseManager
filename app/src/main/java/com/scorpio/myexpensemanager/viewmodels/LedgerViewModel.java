package com.scorpio.myexpensemanager.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.listeners.OnResultListener;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.db.vo.Ledger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * View model for Ledger
 * Created by User on 24-02-2018.
 */

public class LedgerViewModel extends AndroidViewModel {
    private CompanyDb companyDb;
    private LiveData<List<Ledger>> ledgerLiveData;
    private static OnResultListener onResultListener;

    public LedgerViewModel(@NonNull Application application, @NonNull final Company company) {
        super(application);
        if (null != company.getDbName()) {
            companyDb = CompanyDb.getDatabase(this.getApplication(), company.getDbName());
            ledgerLiveData = companyDb.ledgerDao().findAll();
        }
    }

    public void setOnResultListener(@NonNull final OnResultListener listener) {
        this.onResultListener = listener;
    }

    public LiveData<List<Ledger>> fetchAllLedgers() {
        return ledgerLiveData;
    }

    @SuppressLint("NewApi")
    public void addLedgers(@NonNull Ledger... ledgers) {
        if (null != companyDb) {
            List<Ledger> ledgerList = Arrays.stream(ledgers).map(ledger -> {
                if (null == ledger.getCurrentBalance()) {
                    ledger.setCurrentBalance(ledger.getOpeningBalance());

                }
                return ledger;
            }).collect(Collectors.toList());
            ledgers = ledgerList.toArray(new Ledger[ledgerList.size()]);
            new AddLedgerTask(companyDb).execute(ledgers);
        }
    }

    public void addLedger(@NonNull Ledger ledger) {
//        if (null != companyDb) {
//            new AddLedgerTask(companyDb).execute(ledger);
//        }
        if (null != companyDb) {
            if (null == ledger.getCurrentBalance()) {
                ledger.setCurrentBalance(ledger.getOpeningBalance());
            }
            new TaskExecutor().execute(() -> {
                Long result = companyDb.ledgerDao().save(ledger);
                if (result > 0 && null != onResultListener) {
                    onResultListener.onResult(result);
                }
            });
        }
    }

    private static class AddLedgerTask extends AsyncTask<Ledger, Void, List<Long>> {
        private CompanyDb companyDb;

        AddLedgerTask(@NonNull final CompanyDb companyDb) {
            this.companyDb = companyDb;
        }

        @Override
        protected List<Long> doInBackground(Ledger... ledgers) {
            if (ledgers.length > 0 && null != companyDb) {
                return companyDb.ledgerDao().save(ledgers);
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
