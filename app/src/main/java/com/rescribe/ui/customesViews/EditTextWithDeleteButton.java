package com.rescribe.ui.customesViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.rescribe.R;
import com.rescribe.util.CommonMethods;


public class EditTextWithDeleteButton extends LinearLayout {
    protected EditText editText;
    protected ImageButton clearTextButton;
    private OnClearButtonClickedInEditTextListener mClearButtonClickedInEditTextListener;
    private OnKeyboardDoneKeyPressedInEditTextListener mOnKeyboardKeyPressedInEditTextListener;

    public interface TextChangedListener extends TextWatcher {
    }

    TextChangedListener editTextListener = null;

    public void addTextChangedListener(TextChangedListener listener) {
        this.editTextListener = listener;
    }

    public EditTextWithDeleteButton(Context context) {
        super(context);
        //LayoutInflater.from(context).inflate(R.layout.activity_main, this); // Previosuly uncommented, I commented this still working properly.
    }

    public EditTextWithDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public EditTextWithDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(final Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EditTextWithDeleteButton, 0, 0);
        String hintText;
        int deleteButtonRes;
        try {
            // get the text and colors specified using the names in attrs.xml
            hintText = a.getString(R.styleable.EditTextWithDeleteButton_hintText);
            deleteButtonRes = a.getResourceId(
                    R.styleable.EditTextWithDeleteButton_deleteButtonRes,
                    R.drawable.close);

        } finally {
            a.recycle();
        }
        editText = createEditText(context, hintText);
        clearTextButton = createImageButton(context, deleteButtonRes);

        this.addView(editText);
        this.addView(clearTextButton);
        editText.addTextChangedListener(txtEntered());

        /*editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.GONE);

            }
        });*/

        clearTextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClearButtonClickedInEditTextListener != null)
                    mClearButtonClickedInEditTextListener.onClearButtonClicked();
                editText.setText("");
            }
        });

        editText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        // Identifier of the action. This will be either the identifier you supplied,
                        // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                        mOnKeyboardKeyPressedInEditTextListener.onKeyPressed(actionId,event);
                        // Return true if you have consumed the action, else false.
                        return true;
                    }
                });
    }

    public TextWatcher txtEntered() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (editTextListener != null)
                    editTextListener.onTextChanged(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextListener != null)
                    editTextListener.afterTextChanged(s);
                /*if (editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.GONE);*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (editTextListener != null)
                    editTextListener.beforeTextChanged(s, start, count, after);

            }

        };
    }

    @SuppressLint("NewApi")
    private EditText createEditText(Context context, String hintText) {
        editText = new EditText(context);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setLayoutParams(new TableLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
        editText.setHorizontallyScrolling(false);
        editText.setVerticalScrollBarEnabled(true);
        editText.setGravity(Gravity.LEFT);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setHintTextColor(getResources().getColor(R.color.grey_hint_color));
        editText.setCompoundDrawablePadding(CommonMethods.convertDpToPixel(6));
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        editText.setBackground(null);
        editText.setTextColor(getResources().getColor(R.color.white));
        editText.setHint(hintText);
        return editText;
    }

    private ImageButton createImageButton(Context context, int deleteButtonRes) {
        clearTextButton = new ImageButton(context);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER_VERTICAL;
        clearTextButton.setLayoutParams(params);
        clearTextButton.setPadding(CommonMethods.convertDpToPixel(6), CommonMethods.convertDpToPixel(6), CommonMethods.convertDpToPixel(6), CommonMethods.convertDpToPixel(6));
        clearTextButton.setBackgroundResource(deleteButtonRes);
//        clearTextButton.setVisibility(View.GONE);
        return clearTextButton;
    }

    public Editable getText() {
        Editable text = editText.getText();
        return text;
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public interface OnClearButtonClickedInEditTextListener {
        void onClearButtonClicked();
    }

    public interface OnKeyboardDoneKeyPressedInEditTextListener {
        void onKeyPressed(int actionId,KeyEvent event);
    }

    public void addClearTextButtonListener(OnClearButtonClickedInEditTextListener onClearButtonClickedInEditTextListener) {
        this.mClearButtonClickedInEditTextListener = onClearButtonClickedInEditTextListener;
    }

    public void addKeyboardDoneKeyPressedInEditTextListener(OnKeyboardDoneKeyPressedInEditTextListener mOnKeyboardClickedInEditTextListener) {
        this.mOnKeyboardKeyPressedInEditTextListener = mOnKeyboardClickedInEditTextListener;
    }
}
