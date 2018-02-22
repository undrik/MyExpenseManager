package com.scorpio.myexpensemanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.scorpio.myexpensemanager.activity.CreateCompany;
import com.scorpio.myexpensemanager.adapters.CompanyRvAdapter;
import com.scorpio.myexpensemanager.adapters.CompanyRvTouchHelper;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.viewmodels.CompanyViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyExpenseManager extends AppCompatActivity implements CompanyRvTouchHelper
        .CompanyRvItemSwipeListener {

    private CompanyViewModel companyViewModel;

    private CoordinatorLayout expenseManagerLayout;
    private RecyclerView companyRv;
    private CompanyRvAdapter companyRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expense_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateCompany.class);
            startActivity(intent);
        });
        expenseManagerLayout = findViewById(R.id.expenseManagerLayout);
        companyRv = findViewById(R.id.companyRv);
        companyRv.setLayoutManager(new LinearLayoutManager(this));
        companyRvAdapter = new CompanyRvAdapter(new ArrayList<Company>());
        companyRv.setAdapter(companyRvAdapter);

        //Add the item touch helperr
        // Only ItemTouchHelper.LEFT added to detect Right to Left Swipe
        ItemTouchHelper.SimpleCallback companySwiteCallback = new CompanyRvTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(companySwiteCallback).attachToRecyclerView(companyRv);

        companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);
        companyViewModel.fetchAllCompany().observe(MyExpenseManager.this, (companyList -> {
            companyRvAdapter.addItms(companyList);
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_expense_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CompanyRvAdapter.CompanyViewHolder) {
            final String companyName = companyRvAdapter.getItems().get(position).getName();
            final Company company = companyRvAdapter.getItems().get(position);
            if (direction == ItemTouchHelper.LEFT) {
                //remove the item from the recycler view
                companyRvAdapter.removeItem(position);

                // showing Snackbar with UNDO option
                Snackbar snackbar = Snackbar.make(expenseManagerLayout, companyName + " deleted " +
                        "from " +

                        "list!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", (view) -> {
                    companyRvAdapter.restoreItem(company, position);
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            } else {
                Intent intent = new Intent(this, CreateCompany.class);
                startActivity(intent);
            }
        }
    }
}
