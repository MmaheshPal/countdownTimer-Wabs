package com.savihealer.webstimer.interfaces;

import android.widget.ToggleButton;

/**
 * Created by Savi on 27-04-2016.
 */
public interface PageSelectedListener {
    void startTimerNow();
    void onPlaybackControlChange(ToggleButton toggleButton);
    void setBackgroundColor(int color);
}
