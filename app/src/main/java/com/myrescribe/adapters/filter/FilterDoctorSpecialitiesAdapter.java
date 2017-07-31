package com.myrescribe.adapters.filter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;
import com.myrescribe.model.filter.DoctorSpecialityData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDoctorSpecialitiesAdapter extends RecyclerView.Adapter<FilterDoctorSpecialitiesAdapter.FileViewHolder> {

    private final ArrayList<DoctorSpecialityData> doctorSpecialityDatas;
    private final Context context;
    private FilterDoctorSpecialitiesAdapter.ItemClickListener itemClickListener;

    public FilterDoctorSpecialitiesAdapter(Context context, ArrayList<DoctorSpecialityData> doctorSpecialityDatas) {
        this.context = context;
        this.doctorSpecialityDatas = doctorSpecialityDatas;

        try {
            this.itemClickListener = ((FilterDoctorSpecialitiesAdapter.ItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemClickListener.");
        }
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.doctor_list_row, parent, false);
        return new FilterDoctorSpecialitiesAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterDoctorSpecialitiesAdapter.FileViewHolder holder, final int position) {

        holder.drName.setText(doctorSpecialityDatas.get(position).getSpeciality());

        holder.selectCheckbox.setChecked(doctorSpecialityDatas.get(position).isSelected());
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorSpecialityDatas.get(position).isSelected())
                    doctorSpecialityDatas.get(position).setSelected(false);
                else doctorSpecialityDatas.get(position).setSelected(true);

                notifyItemChanged(position);

                itemClickListener.onDoctorSpecialityClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorSpecialityDatas.size();
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
        void onDoctorSpecialityClick();
    }
}
