package com.rescribe.adapters.vital_graph;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.vital_graph.vital_all_list.VitalGraphData;
import com.rescribe.model.vital_graph.vital_tracker.VitalGraphTracker;
import com.rescribe.ui.activities.vital_graph.AddTrackerActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTrackerAdapter extends RecyclerView.Adapter<AddTrackerAdapter.FileViewHolder> implements Filterable {

    private ArrayList<VitalGraphTracker> vitalGraphsTrackerList;
    private Context context;
    private ArrayList<VitalGraphTracker> mArrayList;
    private ItemClickListener itemClickListener;
    private String searchString;

    public AddTrackerAdapter(Context context, ArrayList<VitalGraphTracker> vitalGraphs) {
        this.context = context;
        this.vitalGraphsTrackerList = vitalGraphs;
        this.mArrayList = vitalGraphs;

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

        final VitalGraphTracker vitalGraphTracker = vitalGraphsTrackerList.get(position);

        SpannableString spannableStringSearch = null;
        if ((searchString != null) && (!searchString.isEmpty())) {

            spannableStringSearch = new SpannableString(vitalGraphTracker.getVitalName());
            Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(vitalGraphTracker.getVitalName());
            while (matcher.find()) {
                spannableStringSearch.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(context, R.color.tagColor)),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (spannableStringSearch != null) {
            holder.nameText.setText(spannableStringSearch);
        } else {
            holder.nameText.setText(vitalGraphTracker.getVitalName());
        }

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onTrackerClick(vitalGraphTracker);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vitalGraphsTrackerList.size();
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
        void onTrackerClick(VitalGraphTracker vitalList);
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                searchString = charString;

                if (charString.isEmpty()) {
                    vitalGraphsTrackerList = mArrayList;
                } else {

                    ArrayList<VitalGraphTracker> filteredList = new ArrayList<>();

                    for (VitalGraphTracker trackerModelObject : mArrayList) {

                        if (trackerModelObject.getVitalName().toLowerCase().contains(charString.toLowerCase())) {

                            filteredList.add(trackerModelObject);
                        }
                    }
                    vitalGraphsTrackerList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = vitalGraphsTrackerList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                vitalGraphsTrackerList = (ArrayList<VitalGraphTracker>) filterResults.values;
                if (vitalGraphsTrackerList.size() == 0) {
                    AddTrackerActivity temp = (AddTrackerActivity) context;
                    temp.isDataListViewVisible(false);
                } else {
                    AddTrackerActivity temp = (AddTrackerActivity) context;
                    temp.isDataListViewVisible(true);
                }
                notifyDataSetChanged();
            }
        };
    }
}
