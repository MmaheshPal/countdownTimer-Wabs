package com.savihealer.webstimer.fragments.SessionFragments;

import android.os.Bundle;

import com.savihealer.webstimer.activity.MainActivity;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


public class BreakFragment extends SessionFragment {


    Timer mTimer ;

    public static BreakFragment newInstance(Timer timerDetail,String message){
        BreakFragment fragment = new BreakFragment();
        fragment.mTimer = timerDetail ;
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        return fragment ;
    }

    @Override
    public long getTime() {
        return mTimer.timerDetail.getBreakTime();
    }

    @Override
    public void nevigation() {
        MainActivity.viewPager.setCurrentItem(1,true);
    }

    @Override
    public Timer getTimerDetail() {
        return mTimer ;
    }

    @Override
    public void setBackgroundColor() {
        mRelativeLayout.setBackgroundColor(mTimer.timerColors.breakColor);
    }

    @Override
    public void playTimerFeedback() {
        if(mTimer.timerSounds.isMale)
            play(R.raw.break_started);
        else
            play(R.raw.break_started_female);
    }

    @Override
    public void setBackgroundColor(int color) {
        mRelativeLayout.setBackgroundColor(color);

    }
}

