package com.rescribe.adapters.find_doctors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.complaints.ComplaintList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 18/5/17.
 */

public class ShowDoctorComplaints extends ArrayAdapter<ComplaintList> {

    Context context;
    int resource, textViewResourceId;
    List<ComplaintList> items, tempItems, suggestions;

    public ShowDoctorComplaints(Context context, int resource, int textViewResourceId, List<ComplaintList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<ComplaintList>(items); // this makes the difference.
        suggestions = new ArrayList<ComplaintList>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner_layout, parent, false);
        }
        ComplaintList names = items.get(position);
        if (names != null) {
            TextView lblName = (TextView) view.findViewById(R.id.complaints);
            if (lblName != null)
                lblName.setText(names.getComplaint());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ComplaintList) resultValue).getComplaint();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ComplaintList names : tempItems) {
                    if (names.getComplaint().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(names);
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
           try {
               List<ComplaintList> filterList = (ArrayList<ComplaintList>) results.values;
               if (results != null && results.count > 0) {
                   clear();
                   for (ComplaintList names : filterList) {
                       add(names);
                       notifyDataSetChanged();
                   }
               }
           }
           catch (Exception e){
              e.printStackTrace();
           }

        }
    };
}



