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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDoctorSpecialitiesAdapter extends RecyclerView.Adapter<FilterDoctorSpecialitiesAdapter.FileViewHolder> {

    private final ArrayList<DoctorDetail> doctorDetailArrayList;
    private final Context context;
    private FilterDoctorSpecialitiesAdapter.ItemClickListener itemClickListener;

    public FilterDoctorSpecialitiesAdapter(Context context, ArrayList<DoctorDetail> doctorDetailArrayList) {
        this.context = context;
        this.doctorDetailArrayList = doctorDetailArrayList;

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

        holder.drName.setText(doctorDetailArrayList.get(position).getSpecialization());

        holder.selectCheckbox.setChecked(doctorDetailArrayList.get(position).isDoctorSpecialitySelected());
        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorDetailArrayList.get(position).isDoctorSpecialitySelected())
                    doctorDetailArrayList.get(position).setDoctorSpecialitySelected(false);
                else doctorDetailArrayList.get(position).setDoctorSpecialitySelected(true);

                notifyItemChanged(position);

                itemClickListener.onDoctorSpecialityClick();
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
        void onDoctorSpecialityClick();
    }
}
