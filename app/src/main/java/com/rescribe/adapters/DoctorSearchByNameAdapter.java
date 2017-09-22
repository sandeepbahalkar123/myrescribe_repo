package com.rescribe.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.R;
import com.rescribe.model.doctor_connect.ChatDoctor;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.activities.DoctorConnectActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 8/9/17.
 */

public class DoctorSearchByNameAdapter extends RecyclerView.Adapter<DoctorSearchByNameAdapter.ListViewHolder> implements Filterable {
    private ColorGenerator mColorGenerator;
    private Context mContext;
    private ArrayList<ChatDoctor> appointmentsList;
    private ArrayList<ChatDoctor> mArrayList;
    String searchString = "";
    private String mIdle, mOnline, mOffline;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.onlineStatusTextView)
        TextView onlineStatusTextView;
        @BindView(R.id.paidStatusTextView)
        TextView paidStatusTextView;
        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public DoctorSearchByNameAdapter(Context mContext, ArrayList<ChatDoctor> appointmentsList) {
        this.appointmentsList = appointmentsList;
        mArrayList = appointmentsList;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
        mOnline = mContext.getString(R.string.online);
        mOffline = mContext.getString(R.string.offline);
        mIdle = mContext.getString(R.string.idle);
    }

    @Override
    public DoctorSearchByNameAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_chats_row_item, parent, false);

        return new DoctorSearchByNameAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DoctorSearchByNameAdapter.ListViewHolder holder, int position) {
        final ChatDoctor chatDoctor = appointmentsList.get(position);
        holder.doctorType.setText(chatDoctor.getSpecialization());
        //-----------
        if (chatDoctor.getOnlineStatus().equalsIgnoreCase(mOnline)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else if (chatDoctor.getOnlineStatus().equalsIgnoreCase(mIdle)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        } else if (chatDoctor.getOnlineStatus().equalsIgnoreCase(mOffline)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        } else {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
        }
        //-----------
        holder.onlineStatusTextView.setText(chatDoctor.getOnlineStatus());
        holder.paidStatusTextView.setText(chatDoctor.getPaidStatus() == DoctorConnectActivity.PAID ? "Rs 255/-" : "FREE");
        String doctorName = chatDoctor.getDoctorName();
        // Removed Dr. from doctor name to get starting letter of doctorName to set to image icon.
        doctorName = doctorName.replace("Dr. ", "");
        if (doctorName != null) {
            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            holder.imageOfDoctor.setImageDrawable(drawable);
        }
        //Used spannable to show searchtext in different colour
        SpannableString spannableStringSearch = null;
        if ((searchString != null) && (!searchString.isEmpty())) {

            spannableStringSearch = new SpannableString(chatDoctor.getDoctorName());

            spannableStringSearch.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(mContext, R.color.tagColor)),
                    4, 4 + searchString.length(),//hightlight searchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        if (spannableStringSearch != null) {
            holder.doctorName.setText(spannableStringSearch);
        } else {
            holder.doctorName.setText(chatDoctor.getDoctorName());
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);

                intent.putExtra(RescribeConstants.DOCTORS_INFO, chatDoctor);
                intent.putExtra(RescribeConstants.STATUS_COLOR, holder.onlineStatusTextView.getCurrentTextColor());
                ((DoctorConnectActivity) mContext).startActivityForResult(intent, 1111);
            }
        });


    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                searchString = charString;
                if (charString.isEmpty()) {

                    appointmentsList = mArrayList;
                } else {

                    ArrayList<ChatDoctor> filteredList = new ArrayList<>();

                    for (ChatDoctor doctorConnectModel : mArrayList) {

                        if (doctorConnectModel.getDoctorName().toLowerCase().startsWith(mContext.getString(R.string.dr).toLowerCase() + mContext.getString(R.string.space) + charString.toLowerCase())) {

                            filteredList.add(doctorConnectModel);
                        }
                    }

                    appointmentsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = appointmentsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                appointmentsList = (ArrayList<ChatDoctor>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

