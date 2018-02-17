package com.scorpio.myexpensemanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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
import com.scorpio.myexpensemanager.db.AppDatabase;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.viewmodels.CompanyViewModel;

public class CreateCompany extends AppCompatActivity {

    Toolbar toolbar;
    View view;
    TextInputLayout textInputName, textInputEmail;
    TextInputEditText inputName, inputEmail;
    private AppDatabase appDb;
    private CompanyViewModel companyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);

        view = findViewById(R.id.createCompanyLayout);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_create_company));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textInputName = findViewById(R.id.textInputName);
        inputName = findViewById(R.id.inputName);
        inputName.addTextChangedListener(new CreateCompanyTextWatcher(inputName));

        textInputEmail = findViewById(R.id.textInputEmail);
        inputEmail = findViewById(R.id.inputEmail);
        inputEmail.addTextChangedListener(new CreateCompanyTextWatcher(inputEmail));

        appDb = AppDatabase.getDatabase(this.getApplication());

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);
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
                companyViewModel.addCompany(company);
                finishActivity(0);
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
}
