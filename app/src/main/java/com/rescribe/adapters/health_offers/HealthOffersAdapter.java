package com.rescribe.adapters.health_offers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import com.rescribe.R;
import com.rescribe.util.CommonMethods;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/11/17.
 */

public class HealthOffersAdapter extends RecyclerView.Adapter<HealthOffersAdapter.ListViewHolder> {

    private Context mContext;
    private int imageSize;
    Integer[] mImageMenuICons = {
            R.drawable.metropolis,
            R.drawable.dentist_health_offers,
            R.drawable.full_body_checkup,
            R.drawable.medilife,
            R.drawable.full_body_checkup
    };

    public HealthOffersAdapter(final Context mContext) {
        this.mContext = mContext;
        setColumnNumber(mContext, 2);
    }

    private void setColumnNumber(Context context, int columnNum) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = (widthPixels / columnNum) - CommonMethods.convertDpToPixel(30);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_offers_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
            holder.healthOffersImage.setImageResource(mImageMenuICons[position]);
    }

    @Override
    public int getItemCount() {
        return mImageMenuICons.length;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.healthOffersImage)
        ImageView healthOffersImage;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


}