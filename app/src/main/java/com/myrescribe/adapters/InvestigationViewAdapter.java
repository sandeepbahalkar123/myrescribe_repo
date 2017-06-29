package com.myrescribe.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.model.investigation.DataObject;
import com.myrescribe.util.CommonMethods;

import java.util.ArrayList;

public class InvestigationViewAdapter extends RecyclerView
        .Adapter<InvestigationViewAdapter
        .DataObjectHolder> {
    private static String TAG = "InvestigationViewAdapter";
    private ArrayList<DataObject> mDataset;
    private CheckedClickListener checkedClickListener;

    static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView title;
        CheckBox uploaded;

        DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            uploaded = (CheckBox) itemView.findViewById(R.id.selected);
            Log.i(TAG, "Adding Listener");
        }
    }

    public InvestigationViewAdapter(Context mContext, ArrayList<DataObject> myDataset) {
        mDataset = myDataset;

        try {
            this.checkedClickListener = ((InvestigationViewAdapter.CheckedClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CheckedClickListener.");
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.investigation_item, parent, false);

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.title.setText(mDataset.get(position).getTitle());
        holder.uploaded.setChecked(mDataset.get(position).isSelected());

        holder.uploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataset.get(position).isUploaded()){
                    mDataset.get(position).setSelected(true);
                    mDataset.get(position).setUploaded(true);
                    CommonMethods.showToast(holder.title.getContext(), "Already Uploaded");
                }else {
                    if (mDataset.get(position).isSelected()) {
                        mDataset.get(position).setSelected(false);
                    } else {
                        mDataset.get(position).setSelected(true);
                        checkedClickListener.onCheckedClick(position);
                    }
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface CheckedClickListener {
        void onCheckedClick(int position);
    }
}