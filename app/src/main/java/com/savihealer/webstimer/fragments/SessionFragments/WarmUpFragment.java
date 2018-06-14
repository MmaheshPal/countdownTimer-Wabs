package com.savihealer.webstimer.fragments.SessionFragments;

import android.os.Bundle;

import com.savihealer.webstimer.activity.MainActivity;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


public class WarmUpFragment extends SessionFragment {
    Timer mTimer ;

    public static WarmUpFragment newInstance(Timer timerDetail,String message){
        WarmUpFragment fragment = new WarmUpFragment();
        fragment.mTimer = timerDetail ;
        Bundle bundle = new Bundle();
        bundle.putString("message",message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public long getTime() {
        return mTimer.timerDetail.getWarmupTime();
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
        mRelativeLayout.setBackgroundColor(mTimer.timerColors.warmupColor);
        if(MainActivity.viewPager.getCurrentItem()==0)
            startTimerNow();
    }

    @Override
    public void playTimerFeedback() {
            if(mTimer.timerSounds.isMale)
                play(R.raw.warmup_started);
            else
                play(R.raw.warmup_started_female);
        }


    @Override
    public void setBackgroundColor(int color) {
        mRelativeLayout.setBackgroundColor(color);

    }

}