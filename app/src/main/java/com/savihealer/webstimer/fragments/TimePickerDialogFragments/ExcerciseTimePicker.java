package com.savihealer.webstimer.fragments.TimePickerDialogFragments;

import android.os.Bundle;

/**
 * Created by Savi on 24-04-2016.
 */
public class ExcerciseTimePicker extends TimePickerDialogCustom {
    
    public static ExcerciseTimePicker newInstance(String label){
        ExcerciseTimePicker excerciseTimePicker = new ExcerciseTimePicker();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        excerciseTimePicker.setArguments(bundle);
        return excerciseTimePicker ;
    }


    @Override
    protected void setMainTimerTime() {
        addSessionActivity.setExcerciseTime(min, secs);
    }
}
