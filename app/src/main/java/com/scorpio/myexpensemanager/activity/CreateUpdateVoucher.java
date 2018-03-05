package com.scorpio.myexpensemanager.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Util;

public class CreateUpdateVoucher extends AppCompatActivity {

    private TextInputEditText voucherDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_voucher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        toolbar.setTitle(getString(R.string.payment) + getString(R.string.entry));
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialize();
    }

    private void initialize() {
        voucherDate = findViewById(R.id.voucherDate);
        voucherDate.setText(Util.getTodayWithDay());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
