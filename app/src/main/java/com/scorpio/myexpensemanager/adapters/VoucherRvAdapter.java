package com.scorpio.myexpensemanager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.commons.Constants;
import com.scorpio.myexpensemanager.commons.Util;
import com.scorpio.myexpensemanager.db.listeners.OnItemClickListner;
import com.scorpio.myexpensemanager.db.vo.VoucherEntry;
import com.scorpio.myexpensemanager.db.vo.VoucherWithEntries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoucherRvAdapter extends RecyclerView.Adapter<VoucherRvAdapter.VoucherViewHolder>
        implements View.OnClickListener {
    private List<VoucherWithEntries> vouchers;
    private List<VoucherWithEntries> copyVouchers;
    private OnItemClickListner itemClickListner = null;
    private Context context;

    public VoucherRvAdapter(@NonNull Context context, @NonNull List<VoucherWithEntries> vouchers) {
        this.context = context;
        this.vouchers = vouchers;
        copyVouchers = new ArrayList<>();
    }

    public void setItemClickListner(OnItemClickListner listner) {
        this.itemClickListner = listner;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VoucherRvAdapter.VoucherViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.voucher_list_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        if (vouchers.size() > 0) {
            VoucherWithEntries voucher = vouchers.get(position);
            holder.voucherDateTv.setText(Util.convertToDDMMYYY(voucher.getLocalDate()));
            holder.voucherNoTv.setText(voucher.getNumber());
            holder.voucherTypeTv.setText(voucher.getType().toString());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            holder.voucherItemLayout.removeAllViews();
            for (VoucherEntry entry : voucher.getVoucherEntries()) {
                View voucherLineItem = inflater.inflate(R.layout.voucher_rv_line, null);
                TextView veDrCrTv = voucherLineItem.findViewById(R.id.veDrCrTv);
                TextView veParticularTv = voucherLineItem.findViewById(R.id.veParticularTv);
                veParticularTv.setText(entry.getLedgerName());
                TextView veDebitTv = voucherLineItem.findViewById(R.id.veDebitTv);
                TextView veCreditTv = voucherLineItem.findViewById(R.id.veCreditTv);
                if (entry.getDebitOrCredit().intValue() == Constants.DEBIT) {
                    veDrCrTv.setText(context.getString(R.string.dr));
                    veDebitTv.setText(Util.convertAmountWithSign(entry.getAmount()));
                    veCreditTv.setVisibility(View.INVISIBLE);
                } else {
                    veDrCrTv.setText(context.getString(R.string.cr));
                    veCreditTv.setText(Util.convertAmountWithSign(entry.getAmount()));
                    veDebitTv.setVisibility(View.INVISIBLE);
                }
                holder.voucherItemLayout.addView(voucherLineItem);
            }
            holder.commonCv.setTag(voucher);
        }
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public void addItems(@NonNull List<VoucherWithEntries> vouchers) {
        this.vouchers = vouchers;
        copyVouchers.clear();
        copyVouchers.addAll(vouchers);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        vouchers.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(VoucherWithEntries voucher, int position) {
        vouchers.add(position, voucher);
        notifyItemInserted(position);
    }

    public List<VoucherWithEntries> getItems() {
        return this.vouchers;
    }

    @SuppressLint("NewApi")
    public void filterByAccountName(String accountName) {
        if (!accountName.isEmpty()) {
            vouchers.clear();
            copyVouchers.forEach(voucher -> {
                for (VoucherEntry entry : voucher.getVoucherEntries()) {
                    if (entry.getLedgerName().equalsIgnoreCase(accountName)) {
                        vouchers.add(voucher);
                    }
                }
            });
        }
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        private TextView voucherDateTv, voucherNoTv, voucherTypeTv;
        CardView commonCv;
        ConstraintLayout editBackgroud, deleteBackground;
        LinearLayout voucherItemLayout;

        VoucherViewHolder(View itemView) {
            super(itemView);
            voucherDateTv = itemView.findViewById(R.id.voucherDateTv);
            voucherNoTv = itemView.findViewById(R.id.voucherNoTv);
            voucherTypeTv = itemView.findViewById(R.id.voucherTypeTv);
            commonCv = itemView.findViewById(R.id.commonCv);
            editBackgroud = itemView.findViewById(R.id.editBackground);
            deleteBackground = itemView.findViewById(R.id.deleteBackground);
            voucherItemLayout = itemView.findViewById(R.id.voucherItemLayout);
        }
    }
}
