package com.rescribe.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;
import com.rescribe.model.book_appointment.doctor_data.DoctorServicesModel;

import java.util.ArrayList;
import java.util.Random;

public class ShowRecentVisitedDoctorPagerAdapter extends PagerAdapter {

    private ArrayList<DoctorList> doctorLists;
    private LayoutInflater inflater;
    private Context context;


    public ShowRecentVisitedDoctorPagerAdapter(Context context, ArrayList<DoctorList> doctorLists) {
        this.context = context;
        this.doctorLists=doctorLists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return doctorLists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.recently_visit_doctors_layout, view, false);

        assert imageLayout != null;
        final TextView imageView = (TextView) imageLayout
                .findViewById(R.id.doctorName);


        imageView.setText(doctorLists.get(position).getDocName());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


   /* public void addItem() {
        mSize++;
        notifyDataSetChanged();
    }

    public void removeItem() {
        mSize--;
        mSize = mSize < 0 ? 0 : mSize;

        notifyDataSetChanged();
    }*/
}