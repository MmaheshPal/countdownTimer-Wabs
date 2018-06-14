package com.savihealer.webstimer.fragments.SessionFragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import com.savihealer.webstimer.interfaces.PageSelectedListener;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


public class SuccessFragment extends SessionFragment implements PageSelectedListener {

    Timer mTimer;
    public static SuccessFragment newInstance(Timer timerDetail,String message){
        SuccessFragment fragment = new SuccessFragment();
        fragment.mTimer = timerDetail ;
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        fragment.setArguments(bundle);
        return fragment ;
    }


    @Override
    public void startTimerNow() {
        if(mTimer.timerSounds.isMale)
            play(R.raw.web_ended);
        else
            play(R.raw.web_ended_female);
        mTextViewRound.setVisibility(View.GONE);
        mTextViewTitile.setText("Success");
        mTextViewTitile.setVisibility(View.VISIBLE);
        mTextViewRoundLabel.setText("Completed");
    }

    @Override
    public  long getTime() {
        return 0 ;
    }

    @Override
    public void nevigation() {
    }

    @Override
    public void onPlaybackControlChange(ToggleButton toggleButton) {
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public Timer getTimerDetail() {
        return mTimer ;
    }

    @Override
    public void setBackgroundColor() {
        mRelativeLayout.setBackgroundColor(mTimer.timerColors.successColor);
    }

    @Override
    public void playTimerFeedback() {

    }

    @Override
    public void setBackgroundColor(int color) {
        mRelativeLayout.setBackgroundColor(color);

    }
}
