package com.rescribe.adapters.guide_pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rescribe.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private final List<String> title;
    private Context context;
    private final List<Integer> images;
    private final List<String> descriptions;

    public SliderAdapter(Context context, List<Integer> images, List<String> title, List<String> descriptions) {
        this.context = context;
        this.images = images;
        this.descriptions = descriptions;
        this.title = title;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView titleText = (TextView) view.findViewById(R.id.titleText);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        ImageView iconImage = (ImageView) view.findViewById(R.id.iconImage);

        titleText.setText(title.get(position));
        textView.setText(descriptions.get(position));
        iconImage.setImageResource(images.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}