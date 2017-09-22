package com.rescribe.adapters.vital_graph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.VitalList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTrackerAdapter extends RecyclerView.Adapter<AddTrackerAdapter.FileViewHolder> {

    private final ArrayList<VitalList> vitalGraphs;
    private final Context context;
    private ItemClickListener itemClickListener;

    public AddTrackerAdapter(Context context, ArrayList<VitalList> vitalGraphs) {
        this.context = context;
        this.vitalGraphs = vitalGraphs;

        try {
            this.itemClickListener = ((ItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ItemClickListener.");
        }
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.tracker_list_row, parent, false);

        return new AddTrackerAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AddTrackerAdapter.FileViewHolder holder, final int position) {

        final VitalList vitalGraph = vitalGraphs.get(position);

        holder.nameText.setText(vitalGraph.getVitalName());

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onTrackerClick(vitalGraph);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vitalGraphs.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView nameText;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface ItemClickListener {
        void onTrackerClick(VitalList vitalList);
    }
}
