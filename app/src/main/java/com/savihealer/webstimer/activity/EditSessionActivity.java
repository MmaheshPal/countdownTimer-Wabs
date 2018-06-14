package com.savihealer.webstimer.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import com.savihealer.webstimer.model.Timer;

/**
 * Created by Savi on 07-05-2016.
 */
public class EditSessionActivity extends AddSessionActivity {

    @Override
    protected void setTimerData() {
        mTimer = new Gson().fromJson(getIntent().getStringExtra("edit"),Timer.class);
        oldSessionString = getIntent().getStringExtra("edit");
        mEditTextExcerciseName.setText(mTimer.timerIntro.excerciseName);
        mEditTextTag.setText(mTimer.timerIntro.tag);
    }

    @Override
    protected void setTimerTime() {
        mTextViewWarmUpTime.setText(getFormattedTime(mTimer.timerDetail.warmupTime));
        mTextViewExcerciseTime.setText(getFormattedTime(mTimer.timerDetail.exerciseTime));
        mTextViewBreakTime.setText(getFormattedTime(mTimer.timerDetail.breakTime));
        mTextViewRounds.setText(String.format("%1d",mTimer.timerDetail.round));

    }

    private String getFormattedTime(long milliseconds){

      int mins = (int)TimeUnit.MILLISECONDS.toMinutes(milliseconds);
      int secs = (int) TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
              (int) TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));

        return getFormattedMinSecs(mins,secs);
    }

    private String getFormattedMinSecs(int mins, int secs) {
        String minute ="",seconds = "";
        if(mins<10){
            minute = "0"+ mins;
        }else {
            minute = "" + mins ;
        }

        if(secs<10){
            seconds = "0"+secs ;
        }else {
            seconds = "" +secs;
        }

        return minute+":"+seconds;
    }
    @Override
    protected void setRadioButton() {
        if(mTimer.timerSounds.isMale)
            mRadioButtonMale.setChecked(true);
        else
            mRadioButtonFemale.setChecked(true);
    }

    @Override
    protected void setSwitchs() {
        if(mTimer.timerSounds.voiceNotifiactions)
            mSwitchVoiceNotification.setChecked(true);
        else
            mSwitchVoiceNotification.setChecked(false);

        if(mTimer.timerSounds.countDownbeeps)
            mSwitchCountDownBeeps.setChecked(true);
        else
            mSwitchCountDownBeeps.setChecked(false);

         setGenderLayoutVisibility();
    }

    public void onSaveClick(View view) {
        if (TextUtils.isEmpty(mEditTextExcerciseName.getText().toString())) {
            showSnackBar("Excercise Name Cannot be Empty", view);
            return;
        }
        if (mTimer.timerDetail.exerciseTime == 0) {
            showSnackBar("Excercise Time Cannot be 00:00", view);
            return;
        }
        Log.i("Saving - Edit", new Gson().toJson(mTimer));

        mTimer.timerIntro.excerciseName = mEditTextExcerciseName.getText().toString();
        mSQLiteHelper.updateSessionData(new Gson().toJson(mTimer), oldSessionString);

        Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
        intent.putExtra("position",itemposition);
        startActivity(intent);

    }


}
