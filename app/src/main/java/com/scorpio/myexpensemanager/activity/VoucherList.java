package com.scorpio.myexpensemanager.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.adapters.LedgerRvAdapter;
import com.scorpio.myexpensemanager.adapters.VoucherRvAdapter;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;
import com.scorpio.myexpensemanager.viewmodels.VoucherVM;

import java.util.ArrayList;

public class VoucherList extends AppCompatActivity implements ItemRvTouchHelper
        .RvItemSwipeListener {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
}
