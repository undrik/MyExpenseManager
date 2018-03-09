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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.viewmodels.AccountGroupVM;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.viewmodels.LedgerViewModel;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@SuppressLint("NewApi")
public class CreateUpdateAccount extends AppCompatActivity {
    private TextInputLayout textInputAccountName;
    private TextInputEditText inputbalanceAsOn, inputAccountName;
    private List<String> accountGroups;
    private AutoCompleteTextView groupNameAcTv;
    private List<String> ledgersInDb;
    private ProgressBar accountProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_account);
        Toolbar toolbar = findViewById(R.id.main_toolbar);

        toolbar.setTitle(getString(R.string.title_create_account));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        accountProgressBar = findViewById(R.id.accountProgressBar);
//        accountProgressBar.setIndeterminate(true);
        initialize();

        AccountGroupVM accountGroupVM = new AccountGroupVM(getApplication(), Cache.getCompany());
        accountGroupVM.fetchAllAccountGroup().observe(this, groups -> {
            accountGroups = groups.stream().map(group -> group.getName()).collect(Collectors
                    .toList());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout
                    .simple_dropdown_item_1line, accountGroups);
            groupNameAcTv = findViewById(R.id.groupNameAcTv);
            groupNameAcTv.setAdapter(adapter);
            ImageButton dropDownImgBtn = findViewById(R.id.dropDownImgBtn);
            dropDownImgBtn.setOnClickListener((view) -> {
                groupNameAcTv.showDropDown();
            });

        });
        LedgerViewModel ledgerViewModel = new LedgerViewModel(getApplication(), Cache.getCompany());
        ledgerViewModel.fetchAllLedgers().observe(this, ledgers -> {
            ledgersInDb = ledgers.stream().map(ledger -> ledger.getName()).collect(Collectors
                    .toList());
            inputAccountName.addTextChangedListener(new AccountTextWatcher(inputAccountName,
                    ledgersInDb));
        });


    }

    private void initialize() {
        textInputAccountName = findViewById(R.id.textInputAccountName);
        inputAccountName = findViewById(R.id.inputAccountName);

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
        }

        return super.onOptionsItemSelected(item);
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
                            textInputAccountName.setErrorEnabled(false);
                    }
                    break;
                case R.id.groupNameAcTv:
                    if (Util.validateName(groupNameAcTv.getText().toString().trim()) == Constants
                            .ERROR_CODE_EMPTY) {
                        groupNameAcTv.setError(getString(R.string.err_msg_name));
                        requestFocus(view);
                    } else {
                        groupNameAcTv.setError(null);
                    }
                    break;
            }
        }
    }


}
