package com.rescribe.adapters.vital_graph;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VitalGraphAdapter extends RecyclerView.Adapter<VitalGraphAdapter.FileViewHolder> {

    private final ArrayList<VitalGraphData> vitalGraphs;
    private final Context context;
    private ItemClickListener itemClickListener;

    public VitalGraphAdapter(Context context, ArrayList<VitalGraphData> vitalGraphs) {
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

        final VitalGraphData vitalGraph = vitalGraphs.get(position);

        holder.nameText.setText(vitalGraph.getVitalName());
        holder.weightText.setText(vitalGraph.getVitalValue());
        holder.dateText.setText(String.valueOf(vitalGraph.getVitalDate()));

        if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
            holder.dateText.setTextColor(ContextCompat.getColor(context, R.color.range_green));
        } else if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
            holder.dateText.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
        } else if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
            holder.dateText.setTextColor(ContextCompat.getColor(context, R.color.Red));
        } else {
            holder.dateText.setTextColor(ContextCompat.getColor(context, R.color.Gray));
        }

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
        void onVitalClick(VitalGraphData vitalList);

        void onAddTrackerClick();
    }
}
