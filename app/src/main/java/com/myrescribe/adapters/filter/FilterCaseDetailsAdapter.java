package com.myrescribe.adapters.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.filter.CaseDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterCaseDetailsAdapter extends RecyclerView.Adapter<FilterCaseDetailsAdapter.FileViewHolder> {

    private final ArrayList<CaseDetails> caseDetailsList;
    private final Context context;

    public FilterCaseDetailsAdapter(Context context, ArrayList<CaseDetails> caseDetailsList) {
        this.context = context;
        this.caseDetailsList = caseDetailsList;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.case_details_row, parent, false);
        return new FilterCaseDetailsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterCaseDetailsAdapter.FileViewHolder holder, final int position) {

        holder.caseTextView.setText(caseDetailsList.get(position).getCaseDetails());
        holder.caseIcon.setImageResource(caseDetailsList.get(position).getImageId());

        holder.selectCheckbox.setChecked(caseDetailsList.get(position).isSelected());
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caseDetailsList.get(position).isSelected())
                    caseDetailsList.get(position).setSelected(false);
                else caseDetailsList.get(position).setSelected(true);

                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return caseDetailsList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.caseTextView)
        TextView caseTextView;

        @BindView(R.id.caseIcon)
        ImageView caseIcon;

        @BindView(R.id.selectCheckbox)
        CheckBox selectCheckbox;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }
}
