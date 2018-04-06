package com.scorpio.myexpensemanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.adapters.LedgerRvAdapter;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.viewmodels.LedgerVM;

import java.util.ArrayList;

public class LedgerList extends AppCompatActivity implements ItemRvTouchHelper.RvItemSwipeListener {
    private CoordinatorLayout ledgerListLayout;
    private LedgerRvAdapter ledgerRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_list);
        ledgerListLayout = findViewById(R.id.ledgerListLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateUpdateAccount.class);
            startActivity(intent);
        });

        RecyclerView ledgerRv = findViewById(R.id.ledgerRv);
        ledgerRv.setLayoutManager(new LinearLayoutManager(this));
        ledgerRvAdapter = new LedgerRvAdapter(new ArrayList<>());
        ledgerRv.setAdapter(ledgerRvAdapter);

        ItemTouchHelper.SimpleCallback ledgerSwiteCallback = new ItemRvTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(ledgerSwiteCallback).attachToRecyclerView(ledgerRv);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading Ledgers...");
        progressDialog.show();

        LedgerVM ledgerViewModel = new LedgerVM(this.getApplication());
        ledgerViewModel.fetchAllLedgers().observe(this, (ledgers -> {
            progressDialog.dismiss();
            ledgerRvAdapter.addItms(ledgers);
        }));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof LedgerRvAdapter.LedgerViewHolder) {
            final String ledgerName = ledgerRvAdapter.getItems().get(position).getName();
            final Ledger ledger = ledgerRvAdapter.getItems().get(position);
            if (direction == ItemTouchHelper.LEFT) {
                //remove the item from the recycler view
                ledgerRvAdapter.removeItem(position);

                // showing Snackbar with UNDO option
                Snackbar snackbar = Snackbar.make(ledgerListLayout, ledgerName + " deleted " +
                        "from list!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", (view) -> ledgerRvAdapter.restoreItem(ledger,
                        position));
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                Intent intent = new Intent(this, CreateUpdateAccount.class);
                intent.putExtra(Constants.LEDGER_OBJ, ledger);
                startActivity(intent);
            }
        }
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
