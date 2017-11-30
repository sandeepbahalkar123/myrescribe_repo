package com.rescribe.adapters.saved_article;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.R;
import com.rescribe.interfaces.dashboard_menu_click.IOnMenuClickListener;
import com.rescribe.model.dashboard_api.ClickOption;
import com.rescribe.model.saved_article.SavedArticleInfo;
import com.rescribe.ui.activities.saved_articles.SavedArticles;
import com.rescribe.ui.customesViews.CustomTextView;
import com.rescribe.util.CommonMethods;

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

    public SavedArticleListAdapter(SavedArticles mContext, ArrayList<SavedArticleInfo> mReceivedSavedArticleList, OnArticleClickListener savedArticles) {
        this.mContext = mContext;
        this.mReceivedSavedArticleList = mReceivedSavedArticleList;
        this.onMenuClickListener = savedArticles;
    }

    @Override
    public SavedArticleListAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_saved_articles, parent, false);

        return new SavedArticleListAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SavedArticleListAdapter.ListViewHolder holder, int position) {

        SavedArticleInfo savedArticleInfo = mReceivedSavedArticleList.get(position);

        holder.savedArticleTitle.setText(savedArticleInfo.getArticleTitle());
        holder.doctorName.setText(savedArticleInfo.getAuthorName());
        holder.doctorDetail.setText(savedArticleInfo.getSpecialization() + ", " + savedArticleInfo.getAddress());

        //-----------
        SpannableString date = new SpannableString(savedArticleInfo.getArticleUpdatedDate());
        date.setSpan(new UnderlineSpan(), 0, date.length(), 0);
        holder.articleDate.setText(date);
        //-----------
        SpannableString s = addReadMoreTextToString(savedArticleInfo.getArticleExcerpt(), 20);

        if (s == null) {
            holder.articleText.setText("" + savedArticleInfo.getArticleExcerpt());
        } else {
            holder.articleText.setText(s);
        }

        //------------
        int imageSizeToLoadImage = CommonMethods.getImageSizeToLoadImage(mContext, 2);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.override(imageSizeToLoadImage, imageSizeToLoadImage);

        if (savedArticleInfo.getAuthorImageURL() != null) {
            Glide.with(mContext)
                    .load(savedArticleInfo.getAuthorImageURL())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(holder.doctorImage);
        }

        if (savedArticleInfo.getArticleImageURL() != null) {
            requestOptions.placeholder(R.drawable.image_1);

            Glide.with(mContext)
                    .load(savedArticleInfo.getArticleImageURL())
                    .apply(requestOptions).thumbnail(0.5f)

                    .into(holder.articleImage);
        }

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

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnArticleClickListener {
        public void onArticleClicked(SavedArticleInfo data);
    }

    private SpannableString addReadMoreTextToString(String text, int wordSize) {
        int spaceCount = 0;
        int lastIndex = 0;
        String[] stringSplitted = new String[wordSize];//assuming the sentence has 100 words or less, you can change the value to Integer.MAX_VALUE instead of 10

        String finalString = "";
        int stringLength = 0;//this will give the character count in the string to be split

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {   //check whether the character is a space, if yes then count the words
                spaceCount++;// increment the count as you have encountered a word
            }
            if (spaceCount == wordSize) {     //after encountering 10 words split the sentence from lastIndex to the 10th word. For the first time lastIndex would be zero that is starting position of the string
                stringSplitted[stringLength++] = text.substring(lastIndex, i);
                lastIndex = i;// to get the next part of the sentence, set the last index to 10th word
                spaceCount = 0;//set the number of spaces to zero to starting counting the next 10 words
                System.out.println(stringSplitted[0]);
            }
        }
        stringSplitted[stringLength++] = text.substring(lastIndex, text.length() - 1);//If the sentence has 14 words, only 10 words would be copied to stringSplitted array, this would copy rest of the 4 words into the string splitted array

        for (int i = 0; i < stringSplitted.length; i++) {
            if (stringSplitted[i] != null) {

                finalString = stringSplitted[i] + "... READ MORE";

                SpannableString modifiedText = new SpannableString(finalString);
                modifiedText.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(mContext, R.color.tagColor)),
                        stringSplitted[i].length() + 4, finalString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.e("addReadMoreTextToString", "" + stringSplitted[i]);//Print the splitted strings here
                return modifiedText;
            }
        }
        return null;
    }
}
