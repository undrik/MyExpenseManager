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

import java.util.List;

/**
 * Created by hkundu on 16-02-2018.
 */

public class CompanyRvAdapter extends RecyclerView.Adapter<CompanyRvAdapter.CompanyViewHolder>
        implements View.OnClickListener {
    private List<Company> companyList;
    private OnItemClickListner itemClickListner = null;

    public CompanyRvAdapter(List<Company> companyList) {
        this.companyList = companyList;
    }

    public void setItemClickListner(OnItemClickListner listner) {
        this.itemClickListner = listner;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout
                .company_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CompanyViewHolder holder, int position) {
        if (companyList.size() > 0) {
            Company company = companyList.get(position);
            holder.companyNameTv.setText(company.getName());
            holder.companyLastUpdateTv.setText("");
            holder.commonCv.setTag(company);
            if (null != itemClickListner) {
                holder.commonCv.setOnClickListener(this);
            }
        }
    }

    public void addItms(List<Company> companyList) {
        this.companyList = companyList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        companyList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Company company, int position) {
        companyList.add(position, company);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public List<Company> getItems() {
        return companyList;
    }

    @Override
    public void onClick(View view) {
        if (null != itemClickListner) {
            itemClickListner.onItemClick(view);
        }
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        private TextView companyNameTv;
        private TextView companyLastUpdateTv;
        public CardView commonCv;
        public ConstraintLayout editBackgroud, deleteBackground;


        public CompanyViewHolder(View itemView) {
            super(itemView);
            companyNameTv = itemView.findViewById(R.id.companyNameDrawerTv);
            companyLastUpdateTv = itemView.findViewById(R.id.companyLastUpdateTv);
            commonCv = itemView.findViewById(R.id.commonCv);
            editBackgroud = itemView.findViewById(R.id.editBackground);
            deleteBackground = itemView.findViewById(R.id.deleteBackground);

        }

        public CardView getCard(){
            return commonCv;
        }
    }


}
