package com.scorpio.myexpensemanager.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.PopulateDefaults;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.AppDatabase;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.AccountGroup;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.viewmodels.AccountGroupViewModel;
import com.scorpio.myexpensemanager.viewmodels.CompanyViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class CreateUpdateCompany extends AppCompatActivity {

    Toolbar toolbar;
    TextInputLayout textInputName, textInputEmail;
    TextInputEditText inputName, inputFinYearStart, inputBookStart, inputAddress1, inputAddress2,
            inputState, inputCountry, inputPin, inputPhone, inputPan, inputTan, inputEmail;
    ConstraintLayout createCompanyLayout;
    Company updateCompany = null;
    int currentAction = Constants.CREATE_ACTION;

    private AppDatabase appDb;
    private CompanyViewModel companyViewModel;
    private List<Company> companyList;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        updateCompany = (Company) getIntent().getSerializableExtra(Constants.COMANY_OBJ);
        if (null != updateCompany) {
            currentAction = Constants.UPDATE_ACTION;
        }

        createCompanyLayout = findViewById(R.id.createCompanyLayout);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_create_company));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();

        appDb = AppDatabase.getDatabase(this.getApplication());

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);
        companyViewModel.fetchAllCompany().observe(CreateUpdateCompany.this, (companies -> {
            this.companyList = companies;
            if (null != updateCompany && null != companyList && companyList.size() > 0) {
                companyList = companyList.stream().filter(company -> !company.getName()
                        .equalsIgnoreCase(updateCompany.getName())).collect(Collectors.toList());
                populateFields();
            }
        }));
    }

    @SuppressLint("NewApi")
    private void initialize() {
        textInputName = findViewById(R.id.textInputName);
        inputName = findViewById(R.id.inputName);
        inputName.addTextChangedListener(new CreateCompanyTextWatcher(inputName));


        //
        Calendar calendar = Calendar.getInstance();
        inputFinYearStart = findViewById(R.id.inputFinYearStart);
        inputBookStart = findViewById(R.id.inputBookStart);
        //set the Financial Year start and Book start date as 1-Apr of financial year
        Calendar calendar1April = Calendar.getInstance();
        calendar1April.set(calendar.get(Calendar.YEAR), Calendar.APRIL, 1);
        String firstAprilStr;
        if (calendar.getTimeInMillis() > calendar1April.getTimeInMillis()) {
            firstAprilStr = Util.convertToDDMMMYYYY(calendar1April.getTimeInMillis());
        } else {
            calendar1April.set(calendar.get(Calendar.YEAR) - 1, Calendar.APRIL, 1);
            firstAprilStr = Util.convertToDDMMMYYYY(calendar1April.getTimeInMillis());
        }
        inputFinYearStart.setText(firstAprilStr);
        inputBookStart.setText(firstAprilStr);

        inputFinYearStart.setOnClickListener((view) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateUpdateCompany.this);
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.setOnDateSetListener((v, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                String dateText = Util.convertToDDMMMYYYY(calendar.getTimeInMillis());
                inputFinYearStart.setText(dateText);
                inputBookStart.setText(dateText);
            });

            datePickerDialog.show();
        });

        inputBookStart.setOnClickListener((bsv) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateUpdateCompany.this);
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.setOnDateSetListener((v, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                inputBookStart.setText(Util.convertToDDMMMYYYY(calendar.getTimeInMillis()));
            });
            datePickerDialog.show();
        });

        inputAddress1 = findViewById(R.id.inputAddress1);
        inputAddress2 = findViewById(R.id.inputAddress2);
        inputState = findViewById(R.id.inputState);
        inputCountry = findViewById(R.id.inputCountry);
        inputPin = findViewById(R.id.inputPin);
        inputPhone = findViewById(R.id.inputPhone);
        inputPan = findViewById(R.id.inputPan);
        inputTan = findViewById(R.id.inputTan);

        textInputEmail = findViewById(R.id.textInputEmail);
        inputEmail = findViewById(R.id.inputEmail);
        inputEmail.addTextChangedListener(new CreateCompanyTextWatcher(inputEmail));
    }

    private void populateFields() {
        inputName.setText(updateCompany.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionCheck) {
//            Snackbar.make(view, "Your code on action goes here", Snackbar.LENGTH_LONG).show();
            if (validateName()) {
                //This code should be changed later TODO
                Company company = new Company();
                company.setName(inputName.getText().toString());
                company.setFinYearStart(Util.convertToDateFromDDMMYYYY(inputFinYearStart.getText
                        ().toString().trim()));
                company.setBookStart(Util.convertToDateFromDDMMYYYY(inputBookStart.getText()
                        .toString().trim()));
                company.setDbName(Long.valueOf(Calendar.getInstance().getTimeInMillis()).toString
                        () + Constants.DB_EXTENSION);
                new CompanyTask(AppDatabase.getDatabase(this.getApplication())).execute(company);
//                companyViewModel.addCompany(company);
//                this.finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            textInputName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else if (isCompanyExist(inputName.getText().toString().trim())) {
            textInputName.setError(getString(R.string.err_msg_name_exists));
            requestFocus(inputName);
            return false;
        } else {
            textInputName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();
        if (!isValidEmail(email)) {
            textInputEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        }
        textInputEmail.setErrorEnabled(false);
        return true;

    }

    public boolean isCompanyExist(final String name) {
        if (null != companyList && companyList.size() != 0) {
            for (Company company : companyList) {
                if (company.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private class CreateCompanyTextWatcher implements TextWatcher {

        private View view;

        private CreateCompanyTextWatcher(View view) {
            this.view = view;
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
                case R.id.inputName:
                    validateName();
                    break;
                case R.id.inputEmail:
                    validateEmail();
                    break;
            }
        }
    }

    private class CompanyTask extends AsyncTask<Company, Void, Long> {
        private AppDatabase appDb;

        CompanyTask(final AppDatabase appDb) {
            this.appDb = appDb;
        }

        @Override
        protected Long doInBackground(Company... companies) {
            if (null == updateCompany) {
                Long result = appDb.companyDao().save(companies[0]);
//                CompanyDb companyDb = CompanyDb.getDatabase(getApplication(), companies[0]
//                        .getDbName());
                AccountGroupViewModel groupViewModel = new AccountGroupViewModel(getApplication()
                        , companies[0]);
                List<AccountGroup> groups = PopulateDefaults.predefinedGroups();
                groupViewModel.addAccountGroups(groups.toArray(new AccountGroup[groups.size()]));
                return result;
            } else {
                return Long.valueOf(appDb.companyDao().update(companies[0]));
            }
        }

        @Override
        protected void onPostExecute(Long aLong) {
//            super.onPostExecute(aLong);
            Snackbar snackbar;
            if (aLong > 0) {
                snackbar = Snackbar.make(createCompanyLayout, getString(R.string.success),
                        Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(createCompanyLayout, getString(R.string.failed),
                        Snackbar.LENGTH_LONG);
            }
            snackbar.show();
            if (aLong > 0) {
                CreateUpdateCompany.this.finish();
            }
        }
    }
}