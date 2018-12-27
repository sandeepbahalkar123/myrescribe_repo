package com.rescribe.util;

import android.text.Spanned;
import android.text.method.DigitsKeyListener;

public class InputFilterDecimal extends DigitsKeyListener {

        private final int beforeDecimal;
        private final int afterDecimal;
        private boolean IsDecimalAllow;

        public InputFilterDecimal(int beforeDecimal, int afterDecimal ,boolean IsDecimalAllow) {
            super(Boolean.FALSE, Boolean.TRUE);
            this.beforeDecimal = beforeDecimal;
            this.afterDecimal = afterDecimal;
            this.IsDecimalAllow=IsDecimalAllow;

        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            StringBuilder builder = new StringBuilder(dest);
            builder.insert(dstart, source);
            String temp = builder.toString();

            if (IsDecimalAllow) {
                if (temp.equals(".")) {
                    return "0.";
                } else if (temp.indexOf('.') == -1) {
                    if (temp.length() > beforeDecimal) {
                        return "";
                    }
                } else {
                    if (temp.substring(0, temp.indexOf('.')).length() > beforeDecimal || temp.substring(temp.indexOf('.') + 1, temp.length()).length() > afterDecimal) {
                        return "";
                    }
                }
            }else {
                if (temp.length() > beforeDecimal) {
                    return "";
                }
            }

            return super.filter(source, start, end, dest, dstart, dend);
        }

    }