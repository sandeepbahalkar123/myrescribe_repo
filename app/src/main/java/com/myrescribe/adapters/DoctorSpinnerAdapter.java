package com.myrescribe.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.doctors.doctor_info.DoctorDetail;

import java.util.ArrayList;

public class DoctorSpinnerAdapter extends ArrayAdapter<DoctorDetail> {

    Context context;
    private ArrayList<DoctorDetail> items, tempItems, suggestions;

    public DoctorSpinnerAdapter(Context context, int resource, int textViewResourceId, ArrayList<DoctorDetail> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<DoctorDetail>(items); // this makes the difference.
        suggestions = new ArrayList<DoctorDetail>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_dropdown_layout, parent, false);
        }
        DoctorDetail doctor_details = items.get(position);
        if (doctor_details != null) {
            TextView lblName = (TextView) view.findViewById(R.id.doctorName);
            TextView doctorSpecialist = (TextView) view.findViewById(R.id.doctorSpecialist);
            TextView doctorAddress = (TextView) view.findViewById(R.id.doctorAddress);
            if (lblName != null)
                lblName.setText(doctor_details.getDoctorName());
            doctorSpecialist.setText(doctor_details.getSpecialization());
            doctorAddress.setText(doctor_details.getAddress());

        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((DoctorDetail) resultValue).getDoctorName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (DoctorDetail doctor_details : tempItems) {
                    if (doctor_details.getDoctorName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(doctor_details);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DoctorDetail> filterList = (ArrayList<DoctorDetail>) results.values;
            if (results.count > 0) {
                clear();
                for (DoctorDetail doctor_details : filterList) {
                    add(doctor_details);
                    notifyDataSetChanged();
                }
            }
        }
    };
}