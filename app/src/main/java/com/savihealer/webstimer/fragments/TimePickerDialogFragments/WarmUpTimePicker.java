package com.savihealer.webstimer.fragments.TimePickerDialogFragments;

import android.os.Bundle;

/**
 * Created by Savi on 24-04-2016.
 */
public class WarmUpTimePicker extends TimePickerDialogCustom {

    public static WarmUpTimePicker newInstance(String label) {
        Bundle bundle = new Bundle();
        bundle.putString("label",label);
        WarmUpTimePicker fragment = new WarmUpTimePicker();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setMainTimerTime() {

        addSessionActivity.setWarmupTime(min, secs);

    }
}
