package com.rescribe.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.my_records.SpinnerDoctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpinnerAdapter extends ArrayAdapter<SpinnerDoctor> {

    Context context;
    private List<SpinnerDoctor> items;
    private ArrayList<SpinnerDoctor> tempItems;
    private ArrayList<SpinnerDoctor> suggestions;
    private TextEnterListener textEnterListener;

    public DoctorSpinnerAdapter(Context context, int resource, int textViewResourceId, ArrayList<SpinnerDoctor> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
        tempItems = new ArrayList<SpinnerDoctor>(items); // this makes the difference.
        suggestions = new ArrayList<SpinnerDoctor>();

        try {
            this.textEnterListener = ((TextEnterListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement TextEnterListener.");
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_dropdown_layout, parent, false);
        }

        final SpinnerDoctor doctor_details = items.get(position);
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
            return ((SpinnerDoctor) resultValue).getDoctorName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (SpinnerDoctor doctor_details : tempItems) {
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
            ArrayList<SpinnerDoctor> filterList = (ArrayList<SpinnerDoctor>) results.values;
            if (results.count > 0) {
                clear();
                for (SpinnerDoctor doctor_details : filterList) {
                    add(doctor_details);
                    notifyDataSetChanged();
                }
            }
            textEnterListener.onTextEnter(constraint != null && constraint.length() > 0);
        }
    };


    public SpinnerDoctor getDoctor(int position) {
        return suggestions.get(position);
    }

    public interface TextEnterListener {
        void onTextEnter(boolean isEntered);
    }
}