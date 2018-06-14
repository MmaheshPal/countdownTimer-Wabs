package com.savihealer.webstimer.fragments.SessionFragments;

import android.os.Bundle;

import com.savihealer.webstimer.activity.MainActivity;
import com.savihealer.webstimer.interfaces.PageSelectedListener;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;

public class ExcerciseFragment extends SessionFragment implements PageSelectedListener {

    Timer mTimer ;

    public static ExcerciseFragment newInstance(Timer timerDetail ,String message) {
        Bundle args = new Bundle();
        args.putString("message", message);
        ExcerciseFragment fragment = new ExcerciseFragment();
        fragment.mTimer = timerDetail ;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public long getTime() {
        return mTimer.timerDetail.getExcerciseTime() ;
    }

    @Override
    public void nevigation() {
        mTimer.timerDetail.round = mTimer.timerDetail.round - 1 ;

        if(mTimer.timerDetail.round!=0)
            MainActivity.viewPager.setCurrentItem(2,true);
        else
            MainActivity.viewPager.setCurrentItem(3,true);
    }

    @Override
    public Timer getTimerDetail() {
        return mTimer ;
    }

    @Override
    public void setBackgroundColor() {
        mRelativeLayout.setBackgroundColor(mTimer.timerColors.exerciseColor);
    }

    @Override
    public void playTimerFeedback() {
        if(mTimer.timerSounds.isMale)
            play(R.raw.exercise_started);
        else
            play(R.raw.exercise_started_female);
    }

    @Override
    public void setBackgroundColor(int color) {
        mRelativeLayout.setBackgroundColor(color);

    }
}
