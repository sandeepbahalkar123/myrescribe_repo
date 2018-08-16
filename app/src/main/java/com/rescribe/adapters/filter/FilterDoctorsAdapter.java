package com.rescribe.adapters.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.filter.DoctorData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDoctorsAdapter extends RecyclerView.Adapter<FilterDoctorsAdapter.FileViewHolder> {

    private final ArrayList<DoctorData> doctorDetailArrayList;
    private final Context context;
    private ItemClickListener itemClickListener;

    public FilterDoctorsAdapter(Context context, ArrayList<DoctorData> doctorDetailArrayList) {
        this.context = context;
        this.doctorDetailArrayList = doctorDetailArrayList;

        try {
            this.itemClickListener = ((ItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemClickListener.");
        }
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.doctor_list_row, parent, false);
        return new FilterDoctorsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterDoctorsAdapter.FileViewHolder holder, final int position) {
        String doctorName;
        if(doctorDetailArrayList.get(position).getDoctorName().contains("Dr.")) {
            doctorName = doctorDetailArrayList.get(position).getDoctorName();
        }else{
            doctorName = "Dr. " + doctorDetailArrayList.get(position).getDoctorName();
        }
        holder.drName.setText(doctorName);

        holder.selectCheckbox.setChecked(doctorDetailArrayList.get(position).isSelected());
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorDetailArrayList.get(position).isSelected())
                    doctorDetailArrayList.get(position).setSelected(false);
                else doctorDetailArrayList.get(position).setSelected(true);

                notifyItemChanged(position);

                itemClickListener.onDoctorClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorDetailArrayList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.drNameTextView)
        TextView drName;

        @BindView(R.id.selectCheckbox)
        CheckBox selectCheckbox;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface ItemClickListener{
        void onDoctorClick();
    }
}
