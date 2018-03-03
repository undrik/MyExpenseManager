package com.scorpio.myexpensemanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scorpio.myexpensemanager.activity.CompanyMain;
import com.scorpio.myexpensemanager.activity.CreateUpdateCompany;
import com.scorpio.myexpensemanager.adapters.CompanyRvAdapter;
import com.scorpio.myexpensemanager.adapters.ItemRvTouchHelper;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.listeners.OnItemClickListner;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.viewmodels.CompanyViewModel;

import java.util.ArrayList;

public class MyExpenseManager extends AppCompatActivity implements ItemRvTouchHelper
        .RvItemSwipeListener, OnItemClickListner {
    private static final int CREATE_COMPANY = 10;

    private CoordinatorLayout expenseManagerLayout;
    private CompanyRvAdapter companyRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expense_manager);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateUpdateCompany.class);
            startActivityForResult(intent, CREATE_COMPANY);
        });
        expenseManagerLayout = findViewById(R.id.expenseManagerLayout);
        RecyclerView companyRv = findViewById(R.id.companyRv);
        companyRv.setLayoutManager(new LinearLayoutManager(this));
        companyRvAdapter = new CompanyRvAdapter(new ArrayList<>());
        companyRvAdapter.setItemClickListner(this);
        companyRv.setAdapter(companyRvAdapter);


        //Add the item touch helperr
        // Only ItemTouchHelper.LEFT added to detect Right to Left Swipe
        ItemTouchHelper.SimpleCallback companySwiteCallback = new ItemRvTouchHelper(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(companySwiteCallback).attachToRecyclerView(companyRv);

        CompanyViewModel companyViewModel = ViewModelProviders.of(this).get(CompanyViewModel.class);
        companyViewModel.fetchAllCompany().observe(MyExpenseManager.this, (companyList ->
                companyRvAdapter.addItms(companyList)));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_COMPANY) {
            String message = new String();
            if (resultCode == RESULT_OK) {
                Company company = (Company) data.getSerializableExtra(Constants.COMANY_OBJ);
                if (null != company.getId()) {
                    message = getString(R.string.msg_success_company_create) + " " + company
                            .getName();
                }
            } else {
                message = getString(R.string.msg_failed_company_create);
            }
            Snackbar.make(expenseManagerLayout, message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CompanyRvAdapter.CompanyViewHolder) {
            final String companyName = companyRvAdapter.getItems().get(position).getName();
            final Company company = companyRvAdapter.getItems().get(position);
            if (direction == ItemTouchHelper.LEFT) {
                //remove the item from the recycler view
                companyRvAdapter.removeItem(position);

                AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
                alBuilder.setTitle(Constants.WARNING_TITLE);
                alBuilder.setMessage(getString(R.string.msg_sure_delete) + companyName);
                alBuilder.setCancelable(false);
                alBuilder.setPositiveButton(Constants.YES, (dialog, which) -> {
                    CompanyViewModel companyViewModel = new CompanyViewModel(getApplication());
                    companyViewModel.deleteCompany(company);
                }).setNegativeButton(Constants.NO, (dialog, which) -> {
                    dialog.cancel();
                    companyRvAdapter.restoreItem(company, position);
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
                Intent intent = new Intent(this, CreateUpdateCompany.class);
                intent.putExtra(Constants.COMANY_OBJ, company);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onItemClick(View view) {
        Company company = (Company) view.getTag();
        Log.v(Constants.APP_NAME, company.getName());
//        Cache.getInstance();
        Cache.setCompany(company);
        Intent intent = new Intent(this, CompanyMain.class);
        startActivity(intent);
    }
}
