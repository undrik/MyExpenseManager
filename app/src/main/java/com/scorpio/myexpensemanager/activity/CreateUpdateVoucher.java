package com.scorpio.myexpensemanager.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.vo.VoucherType;
import com.scorpio.myexpensemanager.fragments.VoucherDialog;
import com.scorpio.myexpensemanager.viewmodels.VoucherTypeVM;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressLint("NewApi")
public class CreateUpdateVoucher extends AppCompatActivity {

    Toolbar toolbar;
    private TextInputEditText voucherNo, voucherDate;
    private Map<String, VoucherType> voucherTypeMap = new HashMap<>();
    private VoucherType voucherType;
    private Menu voucherScreenMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_voucher);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VoucherTypeVM voucherTypeVM = ViewModelProviders.of(this).get(VoucherTypeVM.class);

        voucherTypeVM.fetchAllVoucherType().observe(this, (voucherTypes) -> {
            voucherTypeMap = voucherTypes.stream().collect(Collectors.toMap(VoucherType::getName,
                    voucherType -> voucherType));
            initialize();
//            refreshMenu();
//            onResume();
        });
//        refreshMenu();
    }

    private void initialize() {
        setToolbarTitle(getString(R.string.payment) + getString(R.string.space) + getString(R
                .string.entry));
        voucherNo = findViewById(R.id.voucherNo);
        voucherDate = findViewById(R.id.voucherDate);
        voucherDate.setText(Util.getTodayWithDay());
        voucherDate.setOnClickListener((view) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateUpdateVoucher.this);
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(Cache.getCompany().getBookStart().getTime
                    ());
            datePickerDialog.setOnDateSetListener((v, year, month, dayOfMonth) -> {
                LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                String dateText = Util.convertToDDMMYYYEEE(localDate);
                voucherDate.setText(dateText);
            });

            datePickerDialog.show();
        });
    }

    private void setToolbarTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    private void setVoucherNo() {
        voucherNo.setText(voucherType.getCurrentVoucherNo().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voucher_toolbar, menu);
        this.voucherScreenMenu = menu;

        return true;
    }

    private void refreshMenu() {
        if (null == voucherScreenMenu) {
            invalidateOptionsMenu();
        }
        voucherTypeMap.forEach((key, value) -> {
            voucherScreenMenu.add(0, value.getId().intValue(), 0, key);
        });

        if (voucherType == null) {
            MenuItem menuItem = voucherScreenMenu.findItem(voucherTypeMap.get(Constants.PAYMENT)
                    .getId().intValue());
            onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actionDr:
                handleClickDR();
                return true;
            case R.id.actionCr:
                handleClickCR();
                return true;
            case R.id.actionCheck:
                return true;
            default:
                if (id <= voucherTypeMap.size()) {
                    voucherType = voucherTypeMap.get(item.getTitle());
                    setToolbarTitle(voucherType.getName() + getString(R.string.space) + getString(R
                            .string.entry));
                    setVoucherNo();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleClickDR() {
        showVoucherDialog(Constants.DEBIT);
    }

    private void handleClickCR() {
        showVoucherDialog(Constants.CREDIT);
    }

    void showVoucherDialog(int debitOrCredit) {
//        mStackLevel++;

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = VoucherDialog.newInstance(Constants.PAYMENT,
                debitOrCredit);
        newFragment.show(ft, "dialog");
    }
}
