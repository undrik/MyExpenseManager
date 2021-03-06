package com.scorpio.myexpensemanager.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.listeners.OnResultListener;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.Name;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * View model for Ledger
 * Created by User on 24-02-2018.
 */
@SuppressLint("NewApi")
public class LedgerVM extends AndroidViewModel {
    private CompanyDb companyDb;
    private LiveData<List<Ledger>> ledgerLiveData;
    private static OnResultListener onResultListener;

    public LedgerVM(@NonNull Application application) {
        super(application);
        if (null != Cache.getCompany().getDbName()) {
            companyDb = CompanyDb.getDatabase(this.getApplication(), Cache.getCompany().getDbName
                    ());
            ledgerLiveData = companyDb.ledgerDao().findAll();
        }
    }

    public void setOnResultListener(@NonNull final OnResultListener listener) {
        this.onResultListener = listener;
    }

    public LiveData<List<Ledger>> getAllLedgersLD() {
        return ledgerLiveData;
    }

    public List<String> getAllLedgers() {
        List<String> result = new ArrayList<>();

        if (null != companyDb) {
            TaskExecutor taskExecutor = new TaskExecutor();
            Future<List<String>> future = (Future<List<String>>) taskExecutor.submit(() -> {
                List<Name> names = companyDb.ledgerDao().findAllLedgerNames();
                List<String> nameList = names.stream().map(name -> name.getName()).collect
                        (Collectors.toList());
                return nameList;
            });

            try {
                taskExecutor.shutdown();
                result = future.get();
            } catch (InterruptedException e) {
                Log.v(Constants.APP_NAME, e.getMessage());
            } catch (ExecutionException e) {
                Log.v(Constants.APP_NAME, e.getMessage());
            } finally {
                taskExecutor.shutdown();
            }
        }
        return result;
    }


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

    public Long addLedger(@NonNull Ledger ledger) {
//        if (null != companyDb) {
//            new AddLedgerTask(companyDb).execute(ledger);
//        }
        Long result = -1L;
        if (null != companyDb) {
            if (null == ledger.getCurrentBalance()) {
                ledger.setCurrentBalance(ledger.getOpeningBalance());
            }
            TaskExecutor taskExecutor = new TaskExecutor();

            Future<Long> future = taskExecutor.submit(() -> companyDb.ledgerDao().save(ledger));
            try {
                taskExecutor.shutdown();
                result = future.get();
            } catch (InterruptedException e) {
                Log.v(Constants.APP_NAME, e.getMessage());
            } catch (ExecutionException e) {
                Log.v(Constants.APP_NAME, e.getMessage());
            } finally {
                taskExecutor.shutdown();
            }
        }
        return result;
    }

    public Ledger getCreateLedger(@NonNull String name, @NonNull String gName) {
        Ledger result = null;

        if (null != companyDb) {
            TaskExecutor taskExecutor = new TaskExecutor();
            Future<Ledger> future = taskExecutor.submit(() -> {
                Ledger ledger = companyDb.ledgerDao().findLedgerByName(name);
                if (null == ledger) {
                    Long r = companyDb.ledgerDao().save(new Ledger(name, gName, 0.0, Calendar
                            .getInstance
                                    ().getTimeInMillis()));
                    if (r > 0) {
                        ledger = companyDb.ledgerDao().findLedgerById(r);
                    }
                }
                return ledger;
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
        }
        return result;
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
