package com.myrescribe.ui.customesViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.myrescribe.R;
import com.myrescribe.singleton.MyRescribeApplication;
import com.myrescribe.util.MyRescribeConstants;

/**
 * Created by Sandeep Bahalkar
 */

public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        setTextColor(Color.parseColor(MyRescribeConstants.TEXT_COLOR));
        setLinkTextColor(Color.parseColor(MyRescribeConstants.HEADER_COLOR));
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextView);
        String customFont = a.getString(R.styleable.TextView_fontName);

        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset) {
        Typeface typeface = MyRescribeApplication.get(ctx, asset);
        setTypeface(typeface);
    }
}
