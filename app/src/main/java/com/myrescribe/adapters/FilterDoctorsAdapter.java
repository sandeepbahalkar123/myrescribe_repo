package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDoctorsAdapter extends RecyclerView.Adapter<FilterDoctorsAdapter.FileViewHolder> {

    private final ArrayList<DoctorDetail> doctorDetailArrayList;
    private final Context context;

    public FilterDoctorsAdapter(Context context, ArrayList<DoctorDetail> doctorDetailArrayList) {
        this.context = context;
        this.doctorDetailArrayList = doctorDetailArrayList;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.selected_image_item_layout, parent, false);
        return new FilterDoctorsAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FilterDoctorsAdapter.FileViewHolder holder, final int position) {

        holder.drName.setText(doctorDetailArrayList.get(position).getDoctorName());

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorDetailArrayList.get(position).setDoctorSelected(holder.selectCheckbox.isSelected());
                notifyItemChanged(position);
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
        ImageView selectCheckbox;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
