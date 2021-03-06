package com.scorpio.myexpensemanager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.FileSelector;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.commons.sms.ProcessSms;
import com.scorpio.myexpensemanager.commons.tally.TallyFileHandler;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CompanyMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_CALLBACK = 1000;
    private static final int FILE_CHOOSER = 1010;

    CoordinatorLayout companyMainLayout;

    private TextView companyNameDrawerTv, companyFinRangeTv;
    private ImageButton navDatePicker;

    //    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 1000;
//    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1010;
    private static final int PERMISSION_READ_SMS = 1030;
    String[] permissionRequired = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateUpdateVoucher.class);
            startActivity(intent);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);
        companyNameDrawerTv = navHeaderView.findViewById(R.id.companyNameDrawerTv);
        companyNameDrawerTv.setText(Cache.getCompany().getName());
        companyFinRangeTv = navHeaderView.findViewById(R.id.companyFinRangeTv);
        navDatePicker = navHeaderView.findViewById(R.id.navDatePicker);
        navDatePicker.setOnClickListener((view) -> {
            showCalendarPickerView(CalendarPickerView.SelectionMode.RANGE);
        });
        refreshCompanyDateRange();

        companyMainLayout = findViewById(R.id.companyMainLayout);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.company_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.actionScanSms) {
            checkSmsPermission();
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleScanSms() {
        //create inbox uri
        Uri inboxUri = Uri.parse("content://sms/inbox");
        //columns
        String[] smsCol = new String[]{"_id", "address", "body", "date"};

        //get the content provider
        ContentResolver cr = getContentResolver();
        //Fetch the sms from the built in content provider
        Cursor cursor = cr.query(inboxUri, smsCol, null, null, null);
        if (cursor.moveToFirst()) {

            final int totalRecord = cursor.getCount();
            ProcessSms processSms = new ProcessSms(getApplication());
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.msg_process_sms));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(1);
            progressDialog.setMax(totalRecord);
            progressDialog.setCancelable(false);
            progressDialog.show();
            final Thread thread = new Thread() {
                @Override
                public void run() {
                    int increment = 1;
                    do {
                        String id = cursor.getString(cursor.getColumnIndex("_id"));
                        String from = cursor.getString(cursor.getColumnIndex("address"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        String receivedOn = cursor.getString(cursor.getColumnIndex("date"));
                        Log.i(Constants.APP_NAME, "id: " + id + " address: " + from + " body: " +
                                body +
                                " ReceivedOn: " + receivedOn);
                        final String DELIMETER = "-";
                        String[] fromArr = from.split(DELIMETER);
                        if (2 == fromArr.length) {
                            from = fromArr[1];
                            if (processSms.isSupportedSms(from)) {
                                processSms.process(from, receivedOn, body);
                            }
                        }
//                        try {
//                            sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        progressDialog.setProgress(increment++);
                    } while (cursor.moveToNext());
                    progressDialog.dismiss();
                }
            };
            thread.start();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            // Handle the account action
            Intent intent = new Intent(this, LedgerList.class);
            startActivity(intent);
        } else if (id == R.id.nav_group) {

        } else if (id == R.id.nav_voucher) {
            Intent intent = new Intent(this, VoucherList.class);
            startActivity(intent);

        } else if (id == R.id.nav_balance_sheet) {

        } else if (id == R.id.nav_income_expense) {

        } else if (id == R.id.nav_imp_account_group) {
            checkStoragePermission();
        } else if (id == R.id.nav_imp_voucher) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
            alBuilder.setTitle(Constants.WARNING_TITLE);
            alBuilder.setMessage(Constants.QUIT_COMPANY + Cache.getCompany().getName());
            alBuilder.setCancelable(false);
            alBuilder.setPositiveButton(Constants.YES, (dialog, which) -> {
                CompanyMain.this.finish();
            }).setNegativeButton(Constants.NO, (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = alBuilder.create();
            alBuilder.show();
        }
    }

    private void checkSmsPermission() {
        boolean result = false;

        if (ActivityCompat.checkSelfPermission(this, permissionRequired[2]) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_READ_SMS);
        } else {
            handleScanSms();
        }
    }

    private void checkStoragePermission() {
        boolean result = false;

        if (ActivityCompat.checkSelfPermission(this, permissionRequired[0]) != PackageManager
                .PERMISSION_GRANTED)
//            || ActivityCompat.checkSelfPermission(this,
//                permissionRequired[1]) != PackageManager.PERMISSION_GRANTED || ActivityCompat
//                .checkSelfPermission(this, permissionRequired[3]) != PackageManager
//                .PERMISSION_GRANTED)
        {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionRequired[0])
//                    || ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    permissionRequired[1]) || ActivityCompat.shouldShowRequestPermissionRationale
//                    (this, permissionRequired[2])) {
            ActivityCompat.requestPermissions(this, permissionRequired, PERMISSION_CALLBACK);
//            }
        } else {
            importAccountGroup();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                importAccountGroup();
            } else {
                Snackbar.make(companyMainLayout, getString(R.string.err_msg_file_permission),
                        Snackbar.LENGTH_LONG).show();
            }
        }
        if (requestCode == PERMISSION_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                handleScanSms();
            } else {
                Snackbar.make(companyMainLayout, getString(R.string.err_msg_sms_permission),
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void importAccountGroup() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, FILE_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FILE_CHOOSER:
                    Uri fileUri = data.getData();
                    String fileName = FileSelector.getFilePath(fileUri);
                    if (null != fileName) {
                        new TallyImportTask(CompanyMain.this).execute(fileName);
//                        importTallyFile(fileName);
                    } else {
                        Snackbar.make(companyMainLayout, getString(R.string.err_msg_wrong_file),
                                Snackbar.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void refreshCompanyDateRange() {
        if (null == Cache.getCompany().getFinYearEnd()) {
            Cache.getCompany().setFinYearEnd(new Date());
        }
        companyFinRangeTv.setText(Util.FormatDate(Constants.DATE_FORMAT_RANGE, Cache.getCompany()
                .getFinYearStart(), Cache.getCompany().getFinYearEnd()));
    }

    private void showSnackbarMessage(@NonNull String message) {
        Snackbar.make(companyMainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void showCalendarPickerView(CalendarPickerView.SelectionMode selectionMode) {
        CalendarPickerView calendarPickerView = (CalendarPickerView) getLayoutInflater().inflate
                (R.layout.dialog_calendar_picker_view, null, false);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string
                .title_select_range))
                .setView(calendarPickerView)
                .setPositiveButton(R.string
                        .done, (dialog13, which) -> {
                    //Handle the done button click
                    List<Date> dates = calendarPickerView.getSelectedDates();
                    if (dates.size() > 1) {
                        Cache.getCompany().setFinYearStart(dates.get(0));
                        Cache.getCompany().setFinYearEnd(dates.get(dates.size() - 1));
                        refreshCompanyDateRange();
                    }
                }).setNeutralButton(getString(R.string.cancel), (dialog12, which) ->
                        dialog12.dismiss())
                .create();
        dialog.setOnShowListener(dialog1 -> calendarPickerView.fixDialogDimens());
        Date minDate = new Date(0);
        Date maxDate = new Date(Calendar.getInstance().getTimeInMillis() + Constants.ONE_DAY);
        calendarPickerView.init(minDate, maxDate).inMode(selectionMode);

        dialog.show();

    }

    @SuppressLint("NewApi")
    private void importTallyFile(final String fileName) {
//        TaskExecutor taskExecutor = new TaskExecutor();
        ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
        taskExecutor.execute(() -> {
            TallyFileHandler tallyFileHandler = new TallyFileHandler(fileName);
            try {
                tallyFileHandler.parse();
//                    progressDialog.setMessage(companyMain.getString(R.string.msg_importing));
//                publishProgress(companyMain.getString(R.string.msg_importing));
//                AccountGroupVM groupViewModel = new AccountGroupVM(companyMain
//                        .getApplication(), Cache.getCompany());

                CompanyDb companyDb = CompanyDb.getDatabase(getApplication(),
                        Cache.getCompany().getDbName());
//                companyDb.accountGroupDao().findAllLd().observeForever(accountGroups -> {
                List<AccountGroup> accountGroups = companyDb.accountGroupDao().findAllGroups();
                Map<String, AccountGroup> groupMap = accountGroups.stream()
                        .collect(Collectors.toMap(AccountGroup::getName, group -> group));
                List<AccountGroup> groups = tallyFileHandler.getAccountGroups();
                groups = groups.stream().filter(accountGroup -> null == groupMap
                        .get(accountGroup.getName())).collect(Collectors.toList());
//                        List<Long> results = companyDb.accountGroupDao().save(groups);
                groups.forEach((group) -> {
                    Long result = companyDb.accountGroupDao().save(group);
                    if (result > 0) {
                        //Insert the ledgers for this group
                        final List<Ledger> ledgers = tallyFileHandler.getGroupLedgerMap()
                                .get(group);
                        if (null != ledgers) {
                            List<Ledger> ledgersInDb = companyDb.ledgerDao().findAllLedgers();
//                                    List<Ledger> ledgers = tallyFileHandler.getLedgers();
                            List<Ledger> ledgerList = ledgers;
                            if (null != ledgersInDb) {
                                Map<String, Ledger> ledgerMap = ledgersInDb.stream().collect
                                        (Collectors.toMap(Ledger::getName, ledger -> ledger));
                                ledgerList = ledgers.stream().filter(ledger ->
                                        null == ledgerMap.get(ledger.getName())).collect
                                        (Collectors.toList());
                            }
//                                    LedgerVM ledgerViewModel = new LedgerVM
//                                            (companyMain
//                                                    .getApplication(), Cache.getCompany());
                            companyDb.ledgerDao().save(ledgerList);
                        }
//                                    ledgerViewModel.addLedgers(ledgers.toArray(new Ledger[ledgers
//                                            .size()]));


//                        });
//                        groupViewModel.addAccountGroups(groups.toArray(new AccountGroup[groups
//                                .size()]));

//                            snackBarMsg = companyMain.getString(R.string.msg_success_import);
                    }
                });

            } catch (Exception e) {
                Log.e(Constants.APP_NAME, e.getMessage());
//                snackBarMsg = companyMain.getString(R.string.err_msg_failed_import);
            }
        });
        taskExecutor.shutdown();
        while (!taskExecutor.isTerminated()) {

        }
    }

    private static class TallyImportTask extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog;
        private CompanyMain companyMain;
        private String snackBarMsg;

        public TallyImportTask(CompanyMain companyMain) {
            this.companyMain = companyMain;
            progressDialog = new ProgressDialog(companyMain);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(companyMain.getString(R.string.msg_parsing));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(String... fileName) {
            if (fileName.length > 0) {
                TallyFileHandler tallyFileHandler = new TallyFileHandler(fileName[0]);
                try {
                    tallyFileHandler.parse();
                    publishProgress(companyMain.getString(R.string.msg_importing));
                    CompanyDb companyDb = CompanyDb.getDatabase(companyMain.getApplication(),
                            Cache.getCompany().getDbName());
                    List<AccountGroup> accountGroupsInDb = companyDb.accountGroupDao()
                            .findAllGroups();
                    Map<String, AccountGroup> groupMapInDb = accountGroupsInDb.stream()
                            .collect(Collectors.toMap(group -> group.getName(), group ->
                                    group));
                    List<AccountGroup> groups = tallyFileHandler.getAccountGroups();
                    if (null != groups) {

                        groups = groups.stream().filter(accountGroup -> null == groupMapInDb
                                .get(accountGroup.getName())).collect(Collectors.toList());
                        groups.forEach((group) -> {
                            Long result = companyDb.accountGroupDao().save(group);
                            if (result > 0) {
                                publishProgress("Imported group : " + group.getName());
                                group.setId(result);
                                groupMapInDb.put(group.getName(), null);
                            }
                        });
                    }

                    List<Ledger> ledgers = tallyFileHandler.getLedgers();
                    if (null != ledgers) {
                        List<Ledger> ledgersInDb = companyDb.ledgerDao().findAllLedgers();
                        Map<String, Ledger> ledgersMapInDb = ledgersInDb.stream().collect
                                (Collectors.toMap(ledger -> ledger.getName(), ledger -> ledger));
                        ledgers = ledgers.stream().filter(ledger -> null == ledgersMapInDb.get
                                (ledger.getName())).collect(Collectors.toList());
                        ledgers.forEach((ledger) -> {
                            if (null != groupMapInDb.get(ledger.getGroupName())) {
                                Long result = companyDb.ledgerDao().save(ledger);
                                if (result > 0) {
                                    publishProgress("Imported ledger : " + ledger.getName());
                                }
                            }
                        });

                    }
                    snackBarMsg = companyMain.getString(R.string.msg_success_import) + " " +
                            fileName[0];
                } catch (Exception e) {
                    Log.e(Constants.APP_NAME, e.getMessage());
                    snackBarMsg = companyMain.getString(R.string.err_msg_failed_import) + " " +
                            fileName[0];
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            companyMain.showSnackbarMessage(snackBarMsg);
        }


    }
}
