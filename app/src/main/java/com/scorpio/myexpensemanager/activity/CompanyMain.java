package com.scorpio.myexpensemanager.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.FileSelector;
import com.scorpio.myexpensemanager.commons.tally.TallyFileHandler;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Ledger;

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

    //    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 1000;
//    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 1010;
//    private static final int PERMISSION_READ_SMS = 1030;
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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        }

        return super.onOptionsItemSelected(item);
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
            checkPermission();
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

    private void checkPermission() {
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

    private void showSnackbarMessage(@NonNull String message) {
        Snackbar.make(companyMainLayout, message, Snackbar.LENGTH_LONG).show();
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
//                companyDb.accountGroupDao().findAll().observeForever(accountGroups -> {
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
//                                    LedgerViewModel ledgerViewModel = new LedgerViewModel
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
