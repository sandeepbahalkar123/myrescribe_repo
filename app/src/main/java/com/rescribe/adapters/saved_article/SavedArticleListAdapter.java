package com.rescribe.adapters.saved_article;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;
import com.rescribe.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RiteshP on 29/11/17.
 */

public class SavedArticleListAdapter extends RecyclerView.Adapter<SavedArticleListAdapter.ListViewHolder> {

    private final OnArticleClickListener onMenuClickListener;
    private ArrayList<SavedArticleInfo> mReceivedSavedArticleList;
    private Context mContext;
    private ColorGenerator mColorGenerator;

    public SavedArticleListAdapter(Context mContext, ArrayList<SavedArticleInfo> mReceivedSavedArticleList, OnArticleClickListener savedArticles) {
        this.mContext = mContext;
        this.mReceivedSavedArticleList = mReceivedSavedArticleList;
        this.onMenuClickListener = savedArticles;
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public SavedArticleListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_articles, parent, false);

        return new SavedArticleListAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SavedArticleListAdapter.ListViewHolder holder, int position) {

        final SavedArticleInfo savedArticleInfo = mReceivedSavedArticleList.get(position);

        holder.savedArticleTitle.setText(savedArticleInfo.getArticleTitle());
        holder.doctorName.setText(savedArticleInfo.getAuthorName());
        holder.doctorDetail.setText(savedArticleInfo.getSpecialization() + ", " + savedArticleInfo.getAddress());

        //-----------
        SpannableString date = new SpannableString(savedArticleInfo.getArticleUpdatedDate());
        date.setSpan(new UnderlineSpan(), 0, date.length(), 0);
        holder.articleDate.setText(date);
        //-----------
        SpannableString s = CommonMethods.addTextToStringAtLast(savedArticleInfo.getArticleExcerpt(), 20, "... READ MORE", ContextCompat.getColor(mContext, R.color.tagColor));

        if (s == null) {
            holder.articleText.setText("" + savedArticleInfo.getArticleExcerpt());
        } else {
            holder.articleText.setText(s);
        }

        //------------
        int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(mContext, 2);

        if (!savedArticleInfo.getAuthorImageURL().equals(null)) {


            String doctorName = savedArticleInfo.getAuthorName();


            int color2 = mColorGenerator.getColor(doctorName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctorName.charAt(0)).toUpperCase(), color2);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);
            requestOptions.placeholder(drawable);
            requestOptions.error(drawable);
            Glide.with(mContext)
                    .load(savedArticleInfo.getAuthorImageURL())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.doctorImage);
        }

        if (savedArticleInfo.getArticleImageURL() != null) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);
            requestOptions.placeholder(R.drawable.image_1);

            Glide.with(mContext)
                    .load(savedArticleInfo.getArticleImageURL())
                    .apply(requestOptions).thumbnail(0.5f)

                    .into(holder.articleImage);
        } else {
            holder.articleImage.setVisibility(View.GONE);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuClickListener.onArticleClicked(savedArticleInfo);
            }
        });

        holder.bookMarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onMenuClickListener.onBookMarkIconClicked(savedArticleInfo);
            }
        });
     /*   if(savedArticleInfo.getIsSaved()){
            holder.bookMarkIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bookmark));
        }else{
            holder.bookMarkIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_bookmark_border));
        }*/

        //--------------
    }

    @Override
    public int getItemCount() {
        return mReceivedSavedArticleList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.savedArticleTitle)
        CustomTextView savedArticleTitle;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorDetail)
        CustomTextView doctorDetail;
        @BindView(R.id.articleDate)
        CustomTextView articleDate;
        @BindView(R.id.articleImage)
        ImageView articleImage;
        @BindView(R.id.doctorImage)
        ImageView doctorImage;
        @BindView(R.id.articleText)
        CustomTextView articleText;
        @BindView(R.id.bookMarkIcon)
        ImageView bookMarkIcon;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnArticleClickListener {
        public void onArticleClicked(SavedArticleInfo data);

        public void onBookMarkIconClicked(SavedArticleInfo data);
    }


}
