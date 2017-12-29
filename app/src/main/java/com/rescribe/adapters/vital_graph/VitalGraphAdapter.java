package com.rescribe.adapters.vital_graph;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        holder.weightText.setText(vitalGraph.getVitalValue() + " " + vitalGraph.getVitalUnit());

        if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
            holder.weightText.setTextColor(ContextCompat.getColor(context, R.color.range_green));
        } else if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
            holder.weightText.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
        } else if (vitalGraph.getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
            holder.weightText.setTextColor(ContextCompat.getColor(context, R.color.Red));
        } else {
            holder.weightText.setTextColor(ContextCompat.getColor(context, R.color.Gray));
        }

        if ((getItemCount() - 1) == position) {
            holder.layout1.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
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

        if (vitalGraph.getVitalDate() != null) {
            Date timeStamp = CommonMethods.convertStringToDate(vitalGraph.getVitalDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeStamp);
            String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup>" + " "+ new SimpleDateFormat("MMM yy").format(cal.getTime());
            //------
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.dateText.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.dateText.setText(Html.fromHtml(toDisplay));
            }
        }else{
            holder.dateText.setText("");
        }

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
