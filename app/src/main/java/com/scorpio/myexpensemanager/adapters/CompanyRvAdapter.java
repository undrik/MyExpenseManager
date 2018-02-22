package com.scorpio.myexpensemanager.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorpio.myexpensemanager.R;
import com.scorpio.myexpensemanager.db.vo.Company;

import java.util.List;

/**
 * Created by hkundu on 16-02-2018.
 */

public class CompanyRvAdapter extends RecyclerView.Adapter<CompanyRvAdapter.CompanyViewHolder> {
    private List<Company> companyList;

    public CompanyRvAdapter(List<Company> companyList) {
        this.companyList = companyList;
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
            holder.itemView.setTag(company);
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

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        private TextView companyNameTv;
        private TextView companyLastUpdateTv;
        public CardView companyCv;
        public RelativeLayout editBackgroud, deleteBackground;


        public CompanyViewHolder(View itemView) {
            super(itemView);
            companyNameTv = itemView.findViewById(R.id.companyNameTv);
            companyLastUpdateTv = itemView.findViewById(R.id.companyLastUpdateTv);
            companyCv = itemView.findViewById(R.id.companyCv);
            editBackgroud = itemView.findViewById(R.id.editBackground);
            deleteBackground = itemView.findViewById(R.id.deleteBackground);

        }
    }
}
