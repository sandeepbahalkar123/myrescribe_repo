package com.rescribe.util;

import android.animation.LayoutTransition;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

public class KeyboardEvent {

    public KeyboardEvent(RelativeLayout mainRelativeLayout, final KeyboardListener keyboardListener) {
        mainRelativeLayout.setLayoutTransition(new LayoutTransition());
        mainRelativeLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, final int oldBottom) {
                if (oldBottom != bottom) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (oldBottom < bottom)
                                keyboardListener.onKeyboardClose();
                            else keyboardListener.onKeyboardOpen();
                        }
                    }, 5);
                }
            }
        });
    }

    public interface KeyboardListener {
        void onKeyboardOpen();
        void onKeyboardClose();
    }
}
