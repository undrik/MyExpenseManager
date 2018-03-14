package com.scorpio.myexpensemanager.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.viewmodels.LedgerVM;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@SuppressLint("NewApi")
public class CreateUpdateAccount extends AppCompatActivity {
    private TextInputLayout textInputAccountName;
    private TextInputEditText inputbalanceAsOn, inputAccountName, inputOpenningBalance;
    private Switch inputDebitCredit;
    private List<String> accountGroups;
    private Map<String, AccountGroup> groupMap;
    private AutoCompleteTextView groupNameAcTv;
    private List<String> ledgersInDb;
    private ProgressBar accountProgressBar;
    private String ledgerName, groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_account);
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        toolbar.setTitle(getString(R.string.title_create_account));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        accountProgressBar = findViewById(R.id.accountProgressBar);
        accountProgressBar.setIndeterminate(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        TaskExecutor taskExecutor = new TaskExecutor();
        Future future = taskExecutor.submit(() -> {
            initializeLedgersGroups();
        });

        try {
            future.get();
            initialize();
            accountProgressBar.setVisibility(View.INVISIBLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.v(Constants.APP_NAME, e.getMessage());
            finish();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.v(Constants.APP_NAME, e.getMessage());
            finish();
        }
//        AccountGroupVM accountGroupVM = new AccountGroupVM(getApplication(), Cache.getCompany());
//        accountGroupVM.fetchAllAccountGroup().observe(this, groups -> {
//            accountGroups = groups.stream().map(group -> group.getName()).collect(Collectors
//                    .toList());
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout
//                    .simple_dropdown_item_1line, accountGroups);
//            groupNameAcTv = findViewById(R.id.groupNameAcTv);
//            groupNameAcTv.setAdapter(adapter);
//            ImageButton dropDownImgBtn = findViewById(R.id.dropDownImgBtn);
//            dropDownImgBtn.setOnClickListener((view) -> {
//                groupNameAcTv.showDropDown();
//            });
//
//        });
//        LedgerVM ledgerViewModel = new LedgerVM(getApplication(), Cache
// .getCompany());
//        ledgerViewModel.fetchAllLedgers().observe(this, ledgers -> {
//            ledgersInDb = ledgers.stream().map(ledger -> ledger.getName()).collect(Collectors
//                    .toList());
//            inputAccountName.addTextChangedListener(new AccountTextWatcher(inputAccountName,
//                    ledgersInDb));
//        });
    }

    private void initialize() {
        textInputAccountName = findViewById(R.id.textInputAccountName);
        inputAccountName = findViewById(R.id.inputAccountName);
        inputAccountName.addTextChangedListener(new AccountTextWatcher(inputAccountName,
                ledgersInDb));

        inputbalanceAsOn = findViewById(R.id.inputBalanceAsOn);
        inputbalanceAsOn.setText(Util.getToday());
        inputbalanceAsOn.setOnClickListener((view) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateUpdateAccount.this);
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(Cache.getCompany().getBookStart().getTime
                    ());
            datePickerDialog.setOnDateSetListener((v, year, month, dayOfMonth) -> {
                LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                String dateText = Util.convertToDDMMYYY(localDate);
                inputbalanceAsOn.setText(dateText);
            });

            datePickerDialog.show();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout
                .simple_dropdown_item_1line, accountGroups);
        groupNameAcTv = findViewById(R.id.groupNameAcTv);
        groupNameAcTv.setAdapter(adapter);
        groupNameAcTv.addTextChangedListener(new AccountTextWatcher(groupNameAcTv));
        ImageButton dropDownImgBtn = findViewById(R.id.dropDownImgBtn);
        dropDownImgBtn.setOnClickListener((view) -> groupNameAcTv.showDropDown());

        inputOpenningBalance = findViewById(R.id.inputOpenningBalance);
        inputOpenningBalance.setText("0.0");
        inputDebitCredit = findViewById(R.id.inputDebitCredit);
    }

    private synchronized void initializeLedgersGroups() {
        CompanyDb companyDb = CompanyDb.getDatabase(getApplication(), Cache.getCompany()
                .getDbName());
        if (null != companyDb) {
            List<AccountGroup> groups = companyDb.accountGroupDao().findAllGroups();
            groupMap = groups.stream().collect(Collectors.toMap(group -> group.getName(), group
                    -> group));
            accountGroups = companyDb.accountGroupDao().findAllGroupNames().stream().map(name ->
                    name.getName()).collect(Collectors.toList());
            ledgersInDb = companyDb.ledgerDao().findAllLedgerNames().stream().map(name -> name
                    .getName()).collect(Collectors.toList());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actionCheck:
                saveLedger();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveLedger() {
        if (null != ledgerName || null != groupName) {
            Ledger ledger = new Ledger();
            ledger.setActive(true);
            ledger.setName(ledgerName);
            ledger.setGroupName(groupName);
            String balanceText = inputOpenningBalance.getText().toString().trim();
            if (null != balanceText && !balanceText.isEmpty()) {
                ledger.setOpeningBalance(Double.valueOf(balanceText));
            }
            AccountGroup group = groupMap.get(groupName);
            if (group.isDeemedPositive() && !inputDebitCredit.isChecked()) {
                ledger.setOpeningBalance(-1 * ledger.getOpeningBalance());
            }
            ledger.setCurrentBalance(ledger.getOpeningBalance());
            ledger.setOpeningBalanceAsOn(Util.convertToTimeFromddMMMyyyy(inputOpenningBalance
                    .getText().toString().trim()));

            LedgerVM ledgerVM = new LedgerVM(getApplication(), Cache.getCompany());
            Long result = ledgerVM.addLedger(ledger);
            if (result > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string
                        .msg_success_create) + " " + ledger.getName(), Toast.LENGTH_LONG)
                        .show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string
                        .msg_failed_create) + " " + ledger.getName(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class AccountTextWatcher implements TextWatcher {

        private View view;
        private List<String> names;

        private AccountTextWatcher(View view) {
            this.view = view;
        }

        private AccountTextWatcher(View view, List<String> names) {
            this.view = view;
            this.names = names;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputAccountName:
                    switch (Util.validateName(inputAccountName.getText().toString().trim(),
                            ledgersInDb)) {
                        case Constants.ERROR_CODE_EMPTY:
                            textInputAccountName.setError(getString(R.string.err_msg_name));
                            requestFocus(view);
                            break;
                        case Constants.ERROR_CODE_EXISTS:
                            textInputAccountName.setError(getString(R.string.err_msg_name_exists));
                            requestFocus(view);
                            break;
                        case Constants.SUCCESS_CODE:
                            ledgerName = inputAccountName.getText().toString().trim();
                            textInputAccountName.setErrorEnabled(false);
                    }
                    break;
                case R.id.groupNameAcTv:
                    if (Util.validateName(groupNameAcTv.getText().toString().trim()) == Constants
                            .ERROR_CODE_EMPTY) {
                        groupNameAcTv.setError(getString(R.string.err_msg_name));
                        requestFocus(view);
                    } else {
                        groupName = groupNameAcTv.getText().toString().trim();
                        groupNameAcTv.setError(null);
                    }
                    break;
            }
        }
    }


}
