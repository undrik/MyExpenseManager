package com.scorpio.myexpensemanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.adapters.LedgerRvAdapter;
import com.scorpio.myexpensemanager.adapters.VoucherRvAdapter;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VoucherList extends AppCompatActivity implements ItemRvTouchHelper
        .RvItemSwipeListener, SearchView.OnQueryTextListener {

    private VoucherRvAdapter voucherRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        toolbar.setTitle(getString(R.string.title_activity_voucher_list));
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateUpdateVoucher.class);
            startActivity(intent);
        });

        RecyclerView voucherRv = findViewById(R.id.voucherRv);
        voucherRv.setLayoutManager(new LinearLayoutManager(this));
        voucherRvAdapter = new VoucherRvAdapter(getApplicationContext(), new ArrayList<>());
        voucherRv.setAdapter(voucherRvAdapter);

        ItemTouchHelper.SimpleCallback voucherSwiteCallback = new ItemRvTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(voucherSwiteCallback).attachToRecyclerView(voucherRv);

        VoucherVM voucherVM = ViewModelProviders.of(this).get(VoucherVM.class);
        voucherVM.findVoucherWithEntries().observe(this, (voucherWithEntries ->
                voucherRvAdapter.addItems(voucherWithEntries)
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voucher_list, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_account));
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.actionDate:
                showCalendarPickerView(CalendarPickerView.SelectionMode.SINGLE);
                return true;
            case R.id.actionRange:
                showCalendarPickerView(CalendarPickerView.SelectionMode.RANGE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof VoucherRvAdapter.VoucherViewHolder) {
//            final String ledgerName = voucherRvAdapter.getItems().get(position).getName();
            final VoucherWithEntries voucher = voucherRvAdapter.getItems().get(position);
            if (direction == ItemTouchHelper.LEFT) {
                //remove the item from the recycler view
                voucherRvAdapter.removeItem(position);

                // showing Snackbar with UNDO option
//                Snackbar snackbar = Snackbar.make(ledgerListLayout, ledgerName + " deleted " +
//                        "from list!", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", (view) -> ledgerRvAdapter.restoreItem(ledger,
//                        position));
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();
            } else {
                Intent intent = new Intent(this, CreateUpdateAccount.class);
                intent.putExtra(Constants.VOUCHER_OBJ, voucher);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        voucherRvAdapter.filterByAccountName(newText);
        return true;
    }

    private void showCalendarPickerView(CalendarPickerView.SelectionMode selectionMode) {
        CalendarPickerView calendarPickerView = (CalendarPickerView) getLayoutInflater().inflate
                (R.layout.dialog_calendar_picker_view, null, false);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string
                .title_select_range)).setView(calendarPickerView).setPositiveButton(R.string
                .done, new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                calendarPickerView.fixDialogDimens();
            }
        });
        Date minDate = Cache.getCompany().getFinYearStart();
        Date maxDate = new Date(Calendar.getInstance().getTimeInMillis() + Constants.ONE_DAY);
        calendarPickerView.init(minDate, maxDate).inMode(selectionMode);
        dialog.show();

    }
}
