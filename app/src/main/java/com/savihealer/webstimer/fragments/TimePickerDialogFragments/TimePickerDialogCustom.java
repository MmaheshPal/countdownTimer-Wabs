package com.savihealer.webstimer.fragments.TimePickerDialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.savihealer.webstimer.activity.AddSessionActivity;
import com.savihealer.webstimer.R;

/**
 * Created by Savi on 24-04-2016.
 */
public abstract class TimePickerDialogCustom extends DialogFragment {
    protected NumberPicker mNumberPickerMins ;
    protected NumberPicker mNumberPickerSecs ;
    protected NumberPicker mNumberPickerRounds ;
    protected Button mButtonOk , mButtonCancel ;
    private TextView mTextViewDialogLabel ;
    protected LinearLayout mLinearLayoutRoundPicker , mLinearLayoutTimer ;
    protected  int min , secs ,rounds ;
    AddSessionActivity addSessionActivity ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.time_picker_dialog, container, false);
        String label = getArguments().getString("label");
        addSessionActivity =(AddSessionActivity) getActivity();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mButtonOk = (Button)view.findViewById(R.id.button_dialog_ok);
            mButtonCancel = (Button)view.findViewById(R.id.button_dialog_cancel);
        mTextViewDialogLabel = (TextView)view.findViewById(R.id.textview_timepicker_label);
        mTextViewDialogLabel.setText(label);
        mLinearLayoutRoundPicker = (LinearLayout)view.findViewById(R.id.linearlayout_round_picker);
        mLinearLayoutTimer = (LinearLayout)view.findViewById(R.id.linearlayout_timer);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mNumberPickerRounds = (NumberPicker)view.findViewById(R.id.numberPickerRound);
        mNumberPickerMins = (NumberPicker) view.findViewById(R.id.numberPickerMins);
        mNumberPickerSecs  = (NumberPicker) view. findViewById(R.id.numberPickerSecs);
        mNumberPickerRounds.setMinValue(1);
        mNumberPickerRounds.setMaxValue(50);
        mNumberPickerMins.setMaxValue(59);
        mNumberPickerMins.setMinValue(0);
        mNumberPickerSecs.setMaxValue(59);
        mNumberPickerSecs.setMinValue(0);
        setTimePickerVisibility();
      mButtonOk.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              getTimerTime();
              setMainTimerTime();
              dismiss();
          }
      });

        return view;
    }

    protected void setTimePickerVisibility() {
        mLinearLayoutTimer.setVisibility(View.VISIBLE);
        mLinearLayoutRoundPicker.setVisibility(View.GONE);
    }


    protected void getTimerTime(){
        min = mNumberPickerMins.getValue();
        secs = mNumberPickerSecs.getValue();
    }

    protected abstract void setMainTimerTime();
}
