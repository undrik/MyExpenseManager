package com.scorpio.myexpensemanager.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.db.listeners.OnItemClickListner;
import com.scorpio.myexpensemanager.db.vo.Voucher;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;

import java.util.List;

/**
 * Created by hkundu on 14-03-2018.
 */

public class VoucherEntryRvAdapter extends RecyclerView.Adapter<VoucherEntryRvAdapter
        .VoucherEntryViewHolder> implements View.OnClickListener {
    private List<VoucherEntry> voucherEntries;
    private OnItemClickListner onItemClickListner;

    public VoucherEntryRvAdapter(@NonNull List<VoucherEntry> voucherEntries) {
        this.voucherEntries = voucherEntries;
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public VoucherEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherEntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .voucher_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherEntryViewHolder holder, int position) {
        VoucherEntry voucherEntry = voucherEntries.get(position);
        holder.veDrCrTv.setText(voucherEntry.getDebitOrCredit() == Constants.DEBIT ? Constants.DR
                : Constants.CR);
        holder.veParticularTv.setText(voucherEntry.getLedgerName());
        if (voucherEntry.getDebitOrCredit() == Constants.DEBIT) {
            holder.veCreditTv.setVisibility(View.INVISIBLE);
            holder.veDebitTv.setVisibility(View.VISIBLE);
            holder.veDebitTv.setText(voucherEntry.getAmount().toString());
        } else {
            holder.veDebitTv.setVisibility(View.INVISIBLE);
            holder.veCreditTv.setVisibility(View.VISIBLE);
            holder.veCreditTv.setText(voucherEntry.getAmount().toString());
        }
        holder.commonCv.setTag(voucherEntry);
        if (null != onItemClickListner) {
            holder.commonCv.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return voucherEntries.size();
    }

    public void addItems(@NonNull List<VoucherEntry> voucherEntryList) {
        this.voucherEntries = voucherEntryList;
        notifyDataSetChanged();
    }

    public List<VoucherEntry> getItems() {
        return voucherEntries;
    }

    public void addItem(@NonNull VoucherEntry voucherEntry) {
        int position = voucherEntries.size();
        voucherEntries.add(voucherEntry);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        voucherEntries.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(int position, VoucherEntry voucherEntry) {
        voucherEntries.add(position, voucherEntry);
        notifyItemInserted(position);
    }

    @Override
    public void onClick(View v) {
        if (null != onItemClickListner) {
            onItemClickListner.onItemClick(v);
        }
    }

    public static class VoucherEntryViewHolder extends RecyclerView.ViewHolder {
        TextView veDrCrTv, veParticularTv, veDebitTv, veCreditTv;
        CardView commonCv;
        ConstraintLayout editBackgroud, deleteBackground;

        public VoucherEntryViewHolder(View itemView) {
            super(itemView);
            editBackgroud = itemView.findViewById(R.id.editBackground);
            deleteBackground = itemView.findViewById(R.id.deleteBackground);
            commonCv = itemView.findViewById(R.id.commonCv);
            veDrCrTv = itemView.findViewById(R.id.veDrCrTv);
            veParticularTv = itemView.findViewById(R.id.veParticularTv);
            veDebitTv = itemView.findViewById(R.id.veDebitTv);
            veCreditTv = itemView.findViewById(R.id.veCreditTv);
        }
    }
}
