package com.savihealer.webstimer.fragments.SessionFragments;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

import com.savihealer.webstimer.activity.MainActivity;
import com.savihealer.webstimer.interfaces.PageSelectedListener;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;
import com.savihealer.webstimer.utils.Constants;


public abstract class SessionFragment extends Fragment implements PageSelectedListener {

    protected TextView mTextViewRound ;
    protected TextView mTextViewTitile ;
    protected TextView mTextViewTimer ;
    protected TextView mTextViewMins0,  mTextViewMins1;
    protected TextView mTextViewSecs0 , mTextViewSecs1 ;
    protected TextView mTextViewRoundLabel ;

    protected Timer mTimer ;
    static public CountDownTimer Timer ;
    protected Vibrator vibrator ;

    protected RelativeLayout mRelativeLayout ;

    public abstract long getTime();
    public abstract void nevigation();
    public abstract Timer getTimerDetail();
    public abstract void setBackgroundColor();
    public abstract void playTimerFeedback();

    Animation mAnimationBlink ;

    MediaPlayer player ;

    int maxVolume = 100;
    protected long timeRemaining ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session,container,false);

        mTimer = getTimerDetail();

        mTextViewTitile = (TextView)view.findViewById(R.id.textview_title);
        mTextViewTitile.setTypeface(Constants.getTypeface(getContext(),"gotham_light"));

        mTextViewMins0 = (TextView)view.findViewById(R.id.textview_excercise_mins_0);
        mTextViewSecs0 = (TextView)view.findViewById(R.id.textview_excercise_secs_0);

        mTextViewMins1 = (TextView)view.findViewById(R.id.textview_excercise_mins_1);
        mTextViewSecs1 = (TextView)view.findViewById(R.id.textview_excercise_secs_1);

      mTextViewMins0.setTypeface(Constants.getTypeface(getContext(),"gotham_bold"));
        mTextViewSecs0.setTypeface(Constants.getTypeface(getContext(),"gotham_bold"));

        mTextViewMins1.setTypeface(Constants.getTypeface(getContext(),"gotham_bold"));
        mTextViewSecs1.setTypeface(Constants.getTypeface(getContext(),"gotham_bold"));

        mTextViewRoundLabel   = (TextView)view.findViewById(R.id.lbl_rounds);
        mTextViewRoundLabel.setTypeface(Constants.getTypeface(getContext(),"gotham_light"));

        mTextViewRound = (TextView)view.findViewById(R.id.textview_round);
        mTextViewRound.setTypeface(Constants.getTypeface(getContext(),"gotham_light"));


        mTextViewRound.setText(mTimer.timerDetail.getRound() + "/" + mTimer.timerDetail.totalRound);
        mRelativeLayout = (RelativeLayout)view.findViewById(R.id.relativeLayout_main);
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        mTextViewTitile.setText(getArguments().getString("message"));


        mAnimationBlink = new AlphaAnimation(0.0f, 1.0f);
        mAnimationBlink.setDuration(Constants.ONE_SECOND); //You can manage the blinking time with this parameter
        mAnimationBlink.setStartOffset(20);
        mAnimationBlink.setRepeatMode(Animation.REVERSE);
        mAnimationBlink.setRepeatCount(Animation.INFINITE);

        setBackgroundColor();
        return  view ;



    }


    @Override
    public void startTimerNow() {
        if(mTimer.timerSounds.voiceNotifiactions)
            playTimerFeedback();

        mTextViewRound.setText(mTimer.timerDetail.getRound()+"/" + mTimer.timerDetail.totalRound);
        long time = getTime();
        countDownTimer(time);
    }


    void countDownTimer(final long time){
        Timer = new CountDownTimer(time ,100){
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                setTimerText(millisUntilFinished);

                if(mTimer.timerSounds.countDownbeeps) {
                    vibrate(millisUntilFinished, 3900, 4000, Constants.HALF_SECOND);
                    vibrate(millisUntilFinished, 2900, 3000, Constants.HALF_SECOND);
                    vibrate(millisUntilFinished, 1900, 2000, Constants.HALF_SECOND);
                    vibrate(millisUntilFinished, 900, 1000, Constants.ONE_SECOND);
                }
            }
            @Override
            public void onFinish() {

                nevigation();
            }
        }.start();
    }

    void setTimerText(long millisUntilFinished){
        mTextViewMins0.setText(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)/10+"");
        mTextViewMins1.setText(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%10+"");

        long secs = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));


        mTextViewSecs0.setText(secs/10+"");
        mTextViewSecs1.setText(secs % 10 + "");
    }


    @Override
    public void onPlaybackControlChange(ToggleButton toggleButton) {
        if(!toggleButton.isChecked()){
            setPlayTextColor("#FFFFFF");
            clearAnimation();
            countDownTimer(timeRemaining);
        }else {
            setPauseTextColor("#80FFFFFF");
            setAnimation();
            Timer.cancel();
            setTimerText(timeRemaining);
        }
        
        
      
    }

    private void setAnimation() {
        mTextViewSecs0.startAnimation(mAnimationBlink);
        mTextViewSecs1.startAnimation(mAnimationBlink);
        mTextViewMins0.startAnimation(mAnimationBlink);
        mTextViewMins1.startAnimation(mAnimationBlink);
    }

    private void clearAnimation() {
        mTextViewSecs0.clearAnimation();
        mTextViewSecs1.clearAnimation();
        mTextViewMins0.clearAnimation();
        mTextViewMins1.clearAnimation();
    }

    void setPlayTextColor(String colorString){
        mTextViewSecs0.setTextColor(Color.parseColor(colorString));
        mTextViewSecs1.setTextColor(Color.parseColor(colorString));
        mTextViewMins0.setTextColor(Color.parseColor(colorString));
        mTextViewMins1.setTextColor(Color.parseColor(colorString));
    }
    
    void setPauseTextColor(String colorString){
        mTextViewSecs0.setTextColor(Color.parseColor(colorString));
        mTextViewSecs1.setTextColor(Color.parseColor(colorString));
        mTextViewMins0.setTextColor(Color.parseColor(colorString));
        mTextViewMins1.setTextColor(Color.parseColor(colorString));
    }

    void play(int resourceID){
        float log1=(float)(Math.log(maxVolume-MainActivity.volume)/Math.log(maxVolume));
        player = MediaPlayer.create(getActivity(),resourceID);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setVolume(1-log1,1-log1);
        player.start();
        player.release();
    }

    void vibrate(long currentMilli ,long startMilli , long stopMilli ,int duration){
        if (currentMilli >= startMilli && currentMilli <= stopMilli) {

            if(MainActivity.vibrateMode)
            vibrator.vibrate(duration);

        if(duration>=Constants.ONE_SECOND){
            play(R.raw.longbeep);
        }else {
            play(R.raw.shortbeep);
        }
        }
    }
}
