package com.savihealer.webstimer.fragments.TimePickerDialogFragments;

import android.os.Bundle;

/**
 * Created by Savi on 24-04-2016.
 */
public class BreakTimePickerDialog extends TimePickerDialogCustom {

    public static BreakTimePickerDialog newInstance(String label){
        BreakTimePickerDialog dialogCustom = new BreakTimePickerDialog();
        Bundle args = new Bundle();
        args.putString("label", label);
        dialogCustom.setArguments(args);
        return dialogCustom ;
    }

    protected void setMainTimerTime(){

        addSessionActivity.setBreakTime(min, secs);
    }

}
