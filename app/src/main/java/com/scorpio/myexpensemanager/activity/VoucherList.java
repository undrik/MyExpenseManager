package com.scorpio.myexpensemanager.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.adapters.VoucherRvAdapter;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.tally.TallyVoucher;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.viewmodels.LedgerVM;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VoucherList extends AppCompatActivity implements ItemRvTouchHelper
        .RvItemSwipeListener, SearchView.OnQueryTextListener {

    private VoucherRvAdapter voucherRvAdapter;
    private Toolbar toolbar;
    private Date voucherStartDate, voucherEndDate;
    String[] permissionRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK = 1020;
    private static final int DIRECTORY_CHOOSER = 1030;
    private ConstraintLayout voucherListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_list);
        toolbar = (Toolbar) findViewById(R.id.voucherListToolBar);

//        toolbar.setTitle(getString(R.string.title_activity_voucher_list));

        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        voucherListLayout = findViewById(R.id.voucherListLayout);

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

        LedgerVM ledgerVM = new LedgerVM(getApplication());
        List<String> ledersInDb = ledgerVM.getAllLedgers();
        if (null != ledersInDb && ledersInDb.size() > 0) {

            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint(getString(R.string.search_account));
            searchView.setOnQueryTextListener(this);
            searchView.setIconified(false);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout
                    .auto_complete_item, ledersInDb);
            final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById
                    (android.support.v7.appcompat.R.id.search_src_text);
            searchAutoComplete.setAdapter(adapter);
            searchAutoComplete.setThreshold(1);
            searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
                String sStr = (String) parent.getItemAtPosition(position);
                searchAutoComplete.setText(sStr);
                searchView.setIconified(true);
//                searchView.onActionViewCollapsed();
                toolbar.collapseActionView();
            });
        } else {
            searchItem.setVisible(false);
        }
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
            case R.id.actionExport:
                checkWritePermission();
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
//                Intent intent = new Intent(this, CreateUpdateAccount.class);
//                intent.putExtra(Constants.VOUCHER_OBJ, voucher);
//                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        voucherRvAdapter.filterByAccountName(newText);
        return true;
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
                    if (dates.size() == 1) {
                        voucherEndDate = voucherStartDate = dates.get(0);
                    } else {
                        voucherStartDate = dates.get(0);
                        voucherEndDate = dates.get(1);
                    }

                }).setNeutralButton(getString(R.string.cancel), (dialog12, which) ->
                        dialog12.dismiss())
                .create();
        dialog.setOnShowListener(dialog1 -> calendarPickerView.fixDialogDimens());
        Date minDate = Cache.getCompany().getBookStart();
        Date maxDate = new Date(Calendar.getInstance().getTimeInMillis() + Constants.ONE_DAY);
        calendarPickerView.init(minDate, maxDate).inMode(selectionMode);
        dialog.show();

    }

    private void checkWritePermission() {
        if (ActivityCompat.checkSelfPermission(this, permissionRequired[0]) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionRequired, PERMISSION_CALLBACK);
        } else {
            showDirectoryChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDirectoryChooser();
            } else {
                Snackbar.make(voucherListLayout, getString(R.string.err_msg_write_file_permission),
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == DIRECTORY_CHOOSER) {
//            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
//                String directory = data.getStringExtra(DirectoryChooserActivity
//                        .RESULT_SELECTED_DIR);
//                Log.v(Constants.APP_NAME, "Directory Selected : " + directory);
//            } else {
//                // Nothing selected
//            }
//        }
//    }

    private void showDirectoryChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
////        intent.setType("vnd.android.cursor.dir");
//        startActivityForResult(intent, DIRECTORY_CHOOSER);
//        final Intent chooserIntent = new Intent(this, DirectoryChooserActivity.class);
//
//        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
//                .newDirectoryName("DirChooserSample")
////                .allowReadOnlyDirectory(true)
////                .allowNewDirectoryNameModification(true)
//                .build();
//
//        chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
//        startActivityForResult(chooserIntent, DIRECTORY_CHOOSER);

        Log.v(Constants.APP_NAME, "getFilesDir : " + getFilesDir());
        Log.v(Constants.APP_NAME, "getCacheDir : " + getCacheDir());
        Log.v(Constants.APP_NAME, "getExternalFilesDir" + getExternalFilesDir(null));
        TallyVoucher tallyVoucher = new TallyVoucher(getExternalFilesDir(null).getPath());
        tallyVoucher.export(voucherRvAdapter.getItems());
    }

    private void exportVouchers() {
    }
}
