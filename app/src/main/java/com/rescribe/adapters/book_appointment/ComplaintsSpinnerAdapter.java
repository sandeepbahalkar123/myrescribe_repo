package com.rescribe.adapters.book_appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.complaints.ComplaintList;

import java.util.ArrayList;


/**
 * Created by root on 22/6/16.
 */
public class ComplaintsSpinnerAdapter extends BaseAdapter {
    Context mContext;
    private ArrayList<ComplaintList> mComplaintLists;


    public ComplaintsSpinnerAdapter(Context context, ArrayList<ComplaintList> items) {
        this.mContext = context;
        this.mComplaintLists = items;
    }

    @Override
    public int getCount() {
        return mComplaintLists.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ComplaintList complaintList = mComplaintLists.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.layout_custom_spinner_layout, null);
        }

        TextView complaints = (TextView) view.findViewById(R.id.complaints);
        complaints.setText(complaintList.getComplaint());

        return view;
    }

}
