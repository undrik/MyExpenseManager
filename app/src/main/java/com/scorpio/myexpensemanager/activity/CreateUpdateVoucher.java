package com.scorpio.myexpensemanager.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.adapters.VoucherEntryRvAdapter;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.listeners.OnItemClickListner;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherType;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.fragments.VoucherDialog;
import com.scorpio.myexpensemanager.viewmodels.VoucherTypeVM;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressLint("NewApi")
public class CreateUpdateVoucher extends AppCompatActivity implements VoucherDialog
        .OnResultListner, ItemRvTouchHelper.RvItemSwipeListener, OnItemClickListner {

    Toolbar toolbar;
    private TextInputEditText inputVoucherNo, voucherDate;
    private TextView totalDebitTv, totalCreditTv;
    private Map<String, VoucherType> voucherTypeMap = new HashMap<>();
    private VoucherType voucherType;
    private VoucherWithEntries voucher = new VoucherWithEntries();
    private RecyclerView voucheEntryRv;
    private VoucherEntryRvAdapter veRvAdapter;
    private Integer voucherNo;
    VoucherVM voucherVM;
    VoucherTypeVM voucherTypeVM;
    private Double drTotal = 0.0, crTotal = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_update_voucher);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        voucherVM = ViewModelProviders.of(this).get(VoucherVM.class);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        voucherTypeVM = ViewModelProviders.of(this).get(VoucherTypeVM.class);

        List<VoucherType> voucherTypes = voucherTypeVM.fetchAllVoucherType();
        voucherTypeMap = voucherTypes.stream().collect(Collectors.toMap(VoucherType::getName,
                voucherType -> voucherType));

//        voucherTypeVM.fetchAllVoucherTypeLiveData().observe(this, (voucherTypes) -> {
//            voucherTypeMap = voucherTypes.stream().collect(Collectors.toMap(VoucherType::getName,
//                    voucherType -> voucherType));
//
////            refreshMenu();
////            onResume();
//        });
//        refreshMenu();
    }

    private void initialize() {
//        voucherNo = voucherVM.getVoucherSequence();
//        setToolbarTitle(getString(R.string.menu_payment) + getString(R.string.space) +
//                getString(R.string.entry));
        inputVoucherNo = findViewById(R.id.voucherNoTv);
        inputVoucherNo.setText(voucherNo.toString());
        voucherDate = findViewById(R.id.voucherDateTv);
        voucherDate.setText(Util.getTodayWithDay());
        voucher.setLocalDate(LocalDate.now());
        voucherDate.setOnClickListener((view) -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateUpdateVoucher.this);
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(Cache.getCompany().getBookStart().getTime
                    ());
            datePickerDialog.setOnDateSetListener((v, year, month, dayOfMonth) -> {
                LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
                String dateText = Util.convertToDDMMYYYEEE(localDate);
                voucherDate.setText(dateText);
                voucher.setLocalDate(localDate);
            });

            datePickerDialog.show();
        });
        voucheEntryRv = findViewById(R.id.voucherEntryRv);
        voucheEntryRv.setLayoutManager(new LinearLayoutManager(this));
        veRvAdapter = new VoucherEntryRvAdapter(new ArrayList<>());
        veRvAdapter.setOnItemClickListner(this);
        voucheEntryRv.setAdapter(veRvAdapter);

        //Add the item touch helperr
        // Only ItemTouchHelper.LEFT added to detect Right to Left Swipe
        ItemTouchHelper.SimpleCallback switeCallback = new ItemRvTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(switeCallback).attachToRecyclerView(voucheEntryRv);

        totalDebitTv = findViewById(R.id.totalDebitTv);
        totalCreditTv = findViewById(R.id.totalCreditTv);

        updateTotalText();
    }

    private void setToolbarTitle(@NonNull String title) {
        toolbar.setTitle(title);
    }

    private void setVoucherNo(String name) {
        voucherType = voucherTypeVM.fetchCurrentVoucherNo(name);
        if (null != voucherType) {
            inputVoucherNo.setText(voucherType.getCurrentVoucherNo().toString());
            setToolbarTitle(voucherType.getName() + getString(R.string.space) +
                    getString(R.string.entry));
            voucher.setNumber(voucherType.getCurrentVoucherNo().toString());
            voucher.setType(voucherType.getName());
            voucher.setVoucherType(voucherType);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voucher_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.actionPayment);
        onOptionsItemSelected(menuItem);
//        this.voucherScreenMenu = menu;

        return true;
    }

//    private void refreshMenu() {
//        if (null == voucherScreenMenu) {
//            invalidateOptionsMenu();
//        }
//        voucherTypeMap.forEach((key, value) -> {
//            voucherScreenMenu.add(0, value.getId().intValue(), 0, key);
//        });
//
//        if (voucherType == null) {
//            MenuItem menuItem = voucherScreenMenu.findItem(voucherTypeMap.get(Constants.PAYMENT)
//                    .getId().intValue());
//            onOptionsItemSelected(menuItem);
//        }
//    }

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
                handleActionCheck();
                return true;
            case R.id.actionPayment:
            case R.id.actionReceipt:
            case R.id.actionContra:
            case R.id.actionJournal:
            case R.id.actionPurchase:
            case R.id.actionSales:
                setVoucherNo(item.getTitle().toString());

//            default:
//                if (id <= voucherTypeMap.size()) {
//                    voucherType = voucherTypeMap.get(item.getTitle());
//                    setToolbarTitle(voucherType.getName() + getString(R.string.space) +
// getString(R
//                            .string.entry));
//                    setVoucherNo();
//                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleClickDR() {
        showVoucherDialog(Constants.DEBIT);
    }

    private void handleClickCR() {
        showVoucherDialog(Constants.CREDIT);
    }

    private void handleActionCheck() {
        if (drTotal.doubleValue() != 0.0 && drTotal.doubleValue() == crTotal.doubleValue()) {
            Long result = voucherVM.addVoucher(voucher);
            if (result > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.msg_success_create),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
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
        VoucherDialog newFragment = VoucherDialog.newInstance(Constants.PAYMENT,
                debitOrCredit);
        newFragment.setOnResultListner(this);
        newFragment.show(ft, "dialog");
    }

    //Handle the retrun value from the Voucher Dialog
    @Override
    public void onResult(VoucherEntry voucherEntry) {
        voucher.getVoucherEntries().add(voucherEntry);
        veRvAdapter.addItem(voucherEntry);
        calculateTotal();
    }

    //handle the click on the card view of voucher entry
    @Override
    public void onItemClick(View view) {

    }

    //handle swipe on the card view of voucher entry
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof VoucherEntryRvAdapter.VoucherEntryViewHolder) {
            final VoucherEntry voucherEntry = veRvAdapter.getItems().get(position);
            if (direction == ItemTouchHelper.LEFT) {
                //remove the item from the recycler view
                veRvAdapter.removeItem(position);

                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setTitle(Constants.WARNING_TITLE);
                alBuilder.setMessage(getString(R.string.msg_sure_delete));
                alBuilder.setCancelable(false);
                alBuilder.setPositiveButton(Constants.YES, (dialog, which) -> {

                }).setNegativeButton(Constants.NO, (dialog, which) -> {
                    dialog.cancel();
                    veRvAdapter.restoreItem(position, voucherEntry);
                });
                AlertDialog alertDialog = alBuilder.create();
                alBuilder.show();


//                // showing Snackbar with UNDO option
//                Snackbar snackbar = Snackbar.make(expenseManagerLayout, companyName + " deleted
// " +
//                        "from " +
//
//                        "list!", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", (view) -> companyRvAdapter.restoreItem(company,
//                        position));
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();
            } else {
//                Intent intent = new Intent(this, CreateUpdateCompany.class);
//                intent.putExtra(Constants.COMANY_OBJ, company);
//                startActivity(intent);
            }
        }
    }

    //Calculate the total of debit and credit voucher entry
    private void calculateTotal() {
        drTotal = crTotal = 0.0;
        for (VoucherEntry entry : voucher.getVoucherEntries()) {
            if (entry.getDebitOrCredit() == Constants.DEBIT) {
                drTotal += entry.getAmount();
            } else {
                crTotal += entry.getAmount();
            }
        }
        updateTotalText();
    }

    //Update the Debit and Credit Total test field
    private void updateTotalText() {
        totalDebitTv.setText(Util.convertAmount(drTotal));
        totalCreditTv.setText(Util.convertAmount(crTotal));
    }
}
