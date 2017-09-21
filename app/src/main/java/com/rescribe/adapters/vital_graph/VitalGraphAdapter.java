package com.rescribe.adapters.vital_graph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.VitalList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalGraphAdapter extends RecyclerView.Adapter<VitalGraphAdapter.FileViewHolder> {

    private final ArrayList<VitalList> vitalGraphs;
    private final Context context;
    private ItemClickListener itemClickListener;

    public VitalGraphAdapter(Context context, ArrayList<VitalList> vitalGraphs) {
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
                .inflate(R.layout.vitalgraph_list_row, parent, false);

        return new VitalGraphAdapter.FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VitalGraphAdapter.FileViewHolder holder, final int position) {

        final VitalList vitalGraph = vitalGraphs.get(position);

        holder.nameText.setText(vitalGraph.getVitalName());
        holder.weightText.setText(vitalGraph.getVitalWeight());
        holder.dateText.setText(vitalGraph.getVitalDate());

        if ((getItemCount() - 1) == position) {
            holder.layout1.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.layout1.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }

        holder.layout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onVitalClick(vitalGraph);
            }
        });

        holder.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onAddTrackerClick();
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
        @BindView(R.id.weight)
        TextView weightText;
        @BindView(R.id.date)
        TextView dateText;

        @BindView(R.id.layout0)
        LinearLayout layout0;

        @BindView(R.id.divider)
        View divider;

        @BindView(R.id.layout1)
        RelativeLayout layout1;

        View rowView;

        FileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView = itemView;
        }
    }

    public interface ItemClickListener {
        void onVitalClick(VitalList vitalList);
        void onAddTrackerClick();
    }
}
