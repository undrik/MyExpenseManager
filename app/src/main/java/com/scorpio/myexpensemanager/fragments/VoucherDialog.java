package com.scorpio.myexpensemanager.fragments;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.activity.CreateUpdateAccount;
import com.scorpio.myexpensemanager.commons.Cache;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.TaskExecutor;
import com.scorpio.myexpensemanager.db.CompanyDb;
import com.scorpio.myexpensemanager.db.vo.Ledger;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by User on 06-03-2018.
 */

@SuppressLint("NewApi")
public class VoucherDialog extends DialogFragment {
    private static final String VOUCHERTYPE = "voucherType";
    private static final String DEBIT_OR_CREDIT = "debitOrCredit";
    private static final String VOUCHER_OBJ = "param3";

    private String voucherType;
    private int debitOrCredit;

    private List<String> ledgerNamesInDb;
    private Map<String, Ledger> ledgerMapInDb;
    private AutoCompleteTextView inputLedgerAcTv;
    private TextInputLayout textInputLedger;

    private OnResultListner onResultListner;

    public interface OnResultListner {
        void onResult(VoucherEntry voucherEntry);

    }

    public void setOnResultListner(@NonNull OnResultListner listner) {
        onResultListner = listner;
    }

    public static VoucherDialog newInstance(String voucherType, int debitOrCredit) {
        VoucherDialog fragment = new VoucherDialog();
        Bundle args = new Bundle();
        args.putString(VOUCHERTYPE, voucherType);
        args.putInt(DEBIT_OR_CREDIT, debitOrCredit);
        fragment.setArguments(args);

        return fragment;
    }

    public VoucherDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            voucherType = getArguments().getString(VOUCHERTYPE);
            debitOrCredit = getArguments().getInt(DEBIT_OR_CREDIT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_voucher_create_update, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TaskExecutor taskExecutor = new TaskExecutor();
        Future<List<String>> future = taskExecutor.submit(() -> getLedgerNamesInDb());
        try {
            ledgerNamesInDb = future.get();
            progressBar.setVisibility(View.INVISIBLE);
            initialize(view);
        } catch (InterruptedException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
            dismiss();
        } catch (ExecutionException e) {
            Log.v(Constants.APP_NAME, e.getMessage());
            dismiss();
        }

        return view;
    }

    private void initialize(View view) {
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        if (debitOrCredit == Constants.DEBIT) {
            dialogTitle.setText(getString(R.string.debit));
        } else {
            dialogTitle.setText(getString(R.string.credit));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity()
                .getApplicationContext(), android.R.layout.simple_dropdown_item_1line,
                ledgerNamesInDb);
        textInputLedger = view.findViewById(R.id.textInputLedger);
        inputLedgerAcTv = view.findViewById(R.id.inputLedgerAcTv);
        inputLedgerAcTv.setAdapter(adapter);

        ImageButton dropDownBtn = view.findViewById(R.id.dropDownBtn);
        dropDownBtn.setOnClickListener((dropDownView) -> {
            inputLedgerAcTv.showDropDown();
        });


        ImageButton addAccountBtn = view.findViewById(R.id.addAccountBtn);
        addAccountBtn.setOnClickListener((addAccountView) -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CreateUpdateAccount
                    .class);
            startActivity(intent);
        });

        Button okBtn = view.findViewById(R.id.okBtn);
        okBtn.setOnClickListener((btnView) -> {
            handleOk();
        });
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener((cancelView) -> dismiss());
    }

    private void handleOk() {

    }


    private List<String> getLedgerNamesInDb() {
        CompanyDb companyDb = CompanyDb.getDatabase(getActivity().getApplication(), Cache
                .getCompany().getDbName());
        List<Ledger> ledgers = companyDb.ledgerDao().findAllLedgers();
        if (null != ledgers) {
            ledgerMapInDb = ledgers.stream().collect(Collectors.toMap(ledger -> ledger.getName(),
                    ledger -> ledger));
            return ledgers.stream().map(ledger -> ledger.getName()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
