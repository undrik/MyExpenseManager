package com.scorpio.myexpensemanager.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.db.listeners.OnItemClickListner;
import com.scorpio.myexpensemanager.db.vo.Company;
import com.scorpio.myexpensemanager.db.vo.Ledger;

import java.util.List;

/**
 * Created by hkundu on 16-02-2018.
 */

public class LedgerRvAdapter extends RecyclerView.Adapter<LedgerRvAdapter.LedgerViewHolder>
        implements View.OnClickListener {
    private List<Ledger> ledgerList;
    private OnItemClickListner itemClickListner = null;

    public LedgerRvAdapter(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
    }

    public void setItemClickListner(OnItemClickListner listner) {
        this.itemClickListner = listner;
    }

    @Override
    public LedgerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LedgerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .ledger_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final LedgerViewHolder holder, int position) {
        if (ledgerList.size() > 0) {
            Ledger ledger = ledgerList.get(position);
            holder.ledgerNameTv.setText(ledger.getName());
            holder.ledgerGroupTv.setText(ledger.getGroupName());
            holder.ledgerCurrentBalanceTv.setText(ledger.getCurrentBalance().toString());
            holder.commonCv.setTag(ledger);
            if (null != itemClickListner) {
                holder.commonCv.setOnClickListener(this);
            }
        }
    }

    public void addItms(List<Ledger> ledgerList) {
        this.ledgerList = ledgerList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        ledgerList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Ledger ledger, int position) {
        ledgerList.add(position, ledger);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return ledgerList.size();
    }

    public List<Ledger> getItems() {
        return ledgerList;
    }

    @Override
    public void onClick(View view) {
        if (null != itemClickListner) {
            itemClickListner.onItemClick(view);
        }
    }

    public static class LedgerViewHolder extends RecyclerView.ViewHolder {
        private TextView ledgerNameTv, ledgerGroupTv, ledgerCurrentBalanceTv, debitCreditTv;
        public CardView commonCv;
        public ConstraintLayout editBackgroud, deleteBackground;


        public LedgerViewHolder(View itemView) {
            super(itemView);
            ledgerNameTv = itemView.findViewById(R.id.ledgerNameTv);
            ledgerGroupTv = itemView.findViewById(R.id.ledgerGroupTv);
            ledgerCurrentBalanceTv = itemView.findViewById(R.id.ledgerCurrentBalanceTv);
            debitCreditTv = itemView.findViewById(R.id.debitCreditTv);
            commonCv = itemView.findViewById(R.id.commonCv);
            editBackgroud = itemView.findViewById(R.id.editBackground);
            deleteBackground = itemView.findViewById(R.id.deleteBackground);
        }
    }
}
