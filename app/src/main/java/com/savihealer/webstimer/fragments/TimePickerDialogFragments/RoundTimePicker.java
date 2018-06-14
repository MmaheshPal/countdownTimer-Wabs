package com.savihealer.webstimer.fragments.TimePickerDialogFragments;

import android.os.Bundle;
import android.view.View;

/**
 * Created by Savi on 24-04-2016.
 */
public class RoundTimePicker extends TimePickerDialogCustom {

    public static RoundTimePicker newInstance(String label) {
        Bundle bundle = new Bundle();
        RoundTimePicker fragment = new RoundTimePicker();
        bundle.putString("label",label);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void setTimePickerVisibility() {
        mLinearLayoutRoundPicker.setVisibility(View.VISIBLE);
        mLinearLayoutTimer.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void getTimerTime() {
        rounds = mNumberPickerRounds.getValue();
    }


    @Override
    protected void setMainTimerTime() {
        addSessionActivity.setRounds(rounds);
    }
}
