package com.rescribe.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.R;
import com.rescribe.model.doctor_connect.ConnectList;
import com.rescribe.ui.activities.ChatActivity;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 6/9/17.
 */

public class DoctorConnectAdapter extends RecyclerView.Adapter<DoctorConnectAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<ConnectList> connectLists;
    String searchString = "";
    private ColorGenerator mColorGenerator;

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


    public DoctorConnectAdapter(Context mContext, ArrayList<ConnectList> connectLists) {
        this.connectLists = connectLists;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_connect_chats_row_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        final ConnectList connectList = connectLists.get(position);
        holder.doctorType.setText(connectList.getSpecialization());
        if (connectList.getOnlineStatus().equalsIgnoreCase("Online")) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
        }
        holder.onlineStatusTextView.setText(connectList.getOnlineStatus());
        holder.paidStatusTextView.setText(connectList.getPaidStatus());

        String doctorName = connectList.getDoctorName();
        doctorName = doctorName.replace("Dr.", "");
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

        SpannableString spannableStringSearch = null;

        if ((searchString != null) && (!searchString.isEmpty())) {
            spannableStringSearch = new SpannableString(connectList.getDoctorName());
            Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(connectList.getDoctorName());
            while (matcher.find()) {
                spannableStringSearch.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(mContext, R.color.tagColor)),
                        matcher.start(), matcher.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (spannableStringSearch != null) {
            holder.doctorName.setText(spannableStringSearch);
        } else {
            holder.doctorName.setText(connectList.getDoctorName());
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(RescribeConstants.DOCTORS_INFO, connectList);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return connectLists.size();
    }

}

