package com.savihealer.webstimer.fragments.SessionFragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import com.savihealer.webstimer.activity.AddSessionActivity;
import com.savihealer.webstimer.activity.EditSessionActivity;
import com.savihealer.webstimer.activity.MainActivity;
import com.savihealer.webstimer.activity.WelcomeActivity;
import com.savihealer.webstimer.adapters.ViewPagerAdapterMain;
import com.savihealer.webstimer.database.SQLiteHelper;
import com.savihealer.webstimer.interfaces.SetFadingBackground;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


/**
 * Created by devuser on 04-05-2016.
 */
public class SessionPageFragment extends Fragment implements SetFadingBackground {

    final String FADE_BACKGROUND = "#90757575" ;
    TextView mTextViewBreakTime ;
    TextView mTextViewExcerciseTime ;
    TextView mTextViewWarmUpTime ;
    TextView mTextViewRounds ;
    TextView mTextViewExcerciseName ;
    TextView mTextViewAddSession ;

    SQLiteHelper sqLiteHelper ;
    ImageView mImageViewCard ;
    WelcomeActivity mainactivity ;
    Timer mTimer ;

    Button mButtonStart ; ImageView mImageviewEdit , mImageviewDelete ;
    LinearLayout mLinearLayoutInfo , mLinearLayoutStart ,mLinearLayoutMain ,linearlayout_change ;
    FrameLayout mFramelayout ;

    public static SessionPageFragment newInstance(Timer mTimer) {
        SessionPageFragment fragment = new SessionPageFragment();
        fragment.mTimer = mTimer ;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.item_session_list,container,false);
        mImageViewCard = (ImageView)view.findViewById(R.id.imageview_card);
        mLinearLayoutMain = (LinearLayout)view.findViewById(R.id.linearlayout_main);
        mTextViewBreakTime = (TextView) view.findViewById(R.id.textview_breakTime);
        mTextViewExcerciseTime = (TextView) view.findViewById(R.id.textview_exerciseTime);
        mTextViewWarmUpTime = (TextView) view.findViewById(R.id.textview_preparationTime);
        mTextViewRounds = (TextView) view.findViewById(R.id.textview_number_of_rounds);
        mTextViewExcerciseName = (TextView)view.findViewById(R.id.textview_exercise_name);

        mButtonStart = (Button)view.findViewById(R.id.button_start);
        mImageviewEdit = (ImageView)view.findViewById(R.id.button_edit);
        mImageviewDelete = (ImageView)view.findViewById(R.id.button_delete);

        sqLiteHelper = new SQLiteHelper(getContext());
        mFramelayout = (FrameLayout) view.findViewById(R.id.framelayout_fade);
        mLinearLayoutInfo = (LinearLayout)view.findViewById(R.id.layout_info);
        mLinearLayoutStart = (LinearLayout)view.findViewById(R.id.layout_start_edit);
        linearlayout_change = (LinearLayout)view.findViewById(R.id.linearlayout_change);
        mTextViewAddSession = (TextView)view.findViewById(R.id.textview_add_session);
        
        mTextViewAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddSessionActivity.class);
                intent.putExtra("position",WelcomeActivity.sessionList.size());
                intent.putExtra("edit", new Gson().toJson(mTimer));
                startActivity(intent);
            }
        });
        if(mTimer==null ){
          if(ViewPagerAdapterMain.isLast){
            mLinearLayoutMain.setVisibility(View.GONE);
            mTextViewAddSession.setVisibility(View.VISIBLE);
            mFramelayout.setVisibility(View.GONE);
            return view ;
          }
            else if(savedInstanceState!=null) {
              mTimer = new Gson().fromJson(savedInstanceState.getString("timerstring"),Timer.class);
          }

        }else {
            mFramelayout.setVisibility(View.VISIBLE);
            mLinearLayoutMain.setVisibility(View.VISIBLE);
            mTextViewAddSession.setVisibility(View.GONE);
        }

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("timerdata", new Gson().toJson(mTimer));
                startActivity(intent);
            }
        });
        mImageviewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditSessionActivity.class);
                intent.putExtra("position",WelcomeActivity.sessionList.indexOf(mTimer));
                intent.putExtra("edit", new Gson().toJson(mTimer));
                startActivity(intent);
            }
        });
        mImageViewCard.setBackgroundColor(mTimer.timerColors.cardColor);
        linearlayout_change.setBackgroundColor(mTimer.timerColors.cardColor);

        Log.i("Excercise Name", mTimer.timerColors.cardColor + "");
        mTextViewExcerciseName.setText(mTimer.timerIntro.excerciseName);

        mTextViewBreakTime.setText(getFormattedTime(mTimer.timerDetail.getBreakTime()));
        mTextViewExcerciseTime.setText(getFormattedTime(mTimer.timerDetail.getExcerciseTime()));
        mTextViewWarmUpTime.setText(getFormattedTime(mTimer.timerDetail.getWarmupTime()));
        mTextViewRounds.setText(mTimer.timerDetail.getRound()+"");

        mLinearLayoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinearLayoutInfo.getVisibility() == View.VISIBLE) {
                    mLinearLayoutInfo.setVisibility(View.GONE);
                    mLinearLayoutStart.setVisibility(View.VISIBLE);
                    startCountdown();
                }
            }
        });


        mImageviewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteOperation().execute(new Gson().toJson(mTimer));
                Intent intent = getActivity().getIntent();
                intent.putExtra("position",0);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        return view ;
    }

    private void startCountdown() {
        new CountDownTimer(3000, 500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mLinearLayoutInfo.setVisibility(View.VISIBLE);
                mLinearLayoutStart.setVisibility(View.GONE);
            }
        }.start();
    }

    private String getFormattedTime(long breakTime) {
        int mins = (int) TimeUnit.MILLISECONDS.toMinutes(breakTime);
        int secs = (int) (TimeUnit.MILLISECONDS.toSeconds(breakTime) -
                 TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(breakTime)));
                return  getFormattedMinSecs(mins,secs) ;
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
    public void setFadeEnable() {
        mFramelayout.setBackgroundColor(Color.parseColor(FADE_BACKGROUND));
    }

    @Override
    public void setFadeDisable() {
        mFramelayout.setBackgroundResource(0);

    }

    class DeleteOperation extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... params) {
            sqLiteHelper.deleteRecord(params[0]);
            return "";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("timerstring",new Gson().toJson(mTimer));
    }

}
