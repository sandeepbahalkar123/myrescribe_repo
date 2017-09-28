package com.rescribe.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.model.book_appointment.doctor_data.DoctorList;

import java.util.ArrayList;

public class ShowRecentVisitedDoctorPagerAdapter extends PagerAdapter {

    private ArrayList<DoctorList> doctorLists;
    private LayoutInflater inflater;
    private Context mContext;


    public ShowRecentVisitedDoctorPagerAdapter(Context context, ArrayList<DoctorList> doctorLists) {
        this.mContext = context;
        this.doctorLists = doctorLists;
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

        final TextView doctorName = (TextView) imageLayout
                .findViewById(R.id.doctorName);
        final TextView doctorType = (TextView) imageLayout
                .findViewById(R.id.doctorType);
        final TextView doctorExperience = (TextView) imageLayout
                .findViewById(R.id.doctorExperience);
        final TextView doctorAddress = (TextView) imageLayout
                .findViewById(R.id.doctorAddress);
        final TextView doctorFees = (TextView) imageLayout
                .findViewById(R.id.doctorFees);
        final TextView kilometers = (TextView) imageLayout
                .findViewById(R.id.kilometers);
        final ImageView favoriteView = (ImageView) imageLayout
                .findViewById(R.id.favoriteView);
        final TextView recentVisitView = (TextView) imageLayout
                .findViewById(R.id.recentVisitView);


        if(!doctorLists.get(position).getRecentlyVisited()){
            recentVisitView.setVisibility(View.INVISIBLE);
        }else if(!doctorLists.get(position).getFavourite()){
            favoriteView.setVisibility(View.INVISIBLE);
        }else if(!doctorLists.get(position).getRecentlyVisited()&&!doctorLists.get(position).getFavourite()){
            favoriteView.setVisibility(View.INVISIBLE);
            recentVisitView.setVisibility(View.INVISIBLE);
        }else{
            favoriteView.setVisibility(View.VISIBLE);
            recentVisitView.setVisibility(View.VISIBLE);
        }
        doctorName.setText(doctorLists.get(position).getDocName());
        doctorType.setText(doctorLists.get(position).getDegree());
        doctorExperience.setText(""+doctorLists.get(position).getExperience()+mContext.getString(R.string.space)+mContext.getString(R.string.years_experience));
        doctorAddress.setText(doctorLists.get(position).getDoctorAddress());
        doctorFees.setText(""+doctorLists.get(position).getAmount());
        SpannableString content = new SpannableString(""+doctorLists.get(position).getDistance());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        kilometers.setText(content);
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