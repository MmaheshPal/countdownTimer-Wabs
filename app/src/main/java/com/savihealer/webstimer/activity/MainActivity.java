package com.savihealer.webstimer.activity;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import com.savihealer.webstimer.fragments.SessionFragments.BreakFragment;
import com.savihealer.webstimer.fragments.SessionFragments.SessionFragment;
import com.savihealer.webstimer.utils.CustomViewPager;
import com.savihealer.webstimer.fragments.SessionFragments.ExcerciseFragment;
import com.savihealer.webstimer.fragments.SessionFragments.SuccessFragment;
import com.savihealer.webstimer.fragments.SessionFragments.WarmUpFragment;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.interfaces.PageSelectedListener;
import com.savihealer.webstimer.R;

public class MainActivity extends AppCompatActivity {


    private TextView mTextViewExcercise ;

    private MyPagerAdapter viewPagerAdapter ;

    private ToggleButton mToggleButtonPlayback ;
    private ToggleButton mToggleButtonVibrate ;

    private Timer mTimer   ;
    public long totaltime ;
    private ProgressBar mProgressBar ;
    private TabLayout tabLayout ;

    private CountDownTimer countDownTimer ;
    private CountDownTimer seekBarTimer ;

    private RelativeLayout mRelativelatyoutQuickAccess ;
    private LinearLayout mLinearLayoutQuickAccess ;

    private ImageButton mImageButtonVolume ;
    private ImageButton mImageButtonBrightness ;
    private ImageButton mImageButtonColorPicker ;

    private SeekBar mSeekBarVolume ;
    private SeekBar mSeekBarBrightness ;

    private NotificationCompat.Builder builder ;
    private NotificationManager mNotificationManager ;
    private Notification timerNotification ;

    private int[] mColors ;
    private int mSelectedColor ;
    private long remainingTime;
    int progress ;

    public static int volume ;
    public static CustomViewPager viewPager ;
    public static boolean vibrateMode = true;
    private boolean isInBackground = false;

    boolean isPlaying = true ;
    int quickAccesslayoutHeight ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        hideactionbar();
        setContentView(R.layout.activity_main);

        Intent timeIntent = getIntent() ;
        mTimer = (Timer) new Gson().fromJson((timeIntent.getStringExtra("timerdata")),Timer.class);
        totaltime = mTimer.timerDetail.getWarmupTime() + (mTimer.timerDetail.getExcerciseTime() * mTimer.timerDetail.getRound()) + (mTimer.timerDetail.getBreakTime() * mTimer.timerDetail.getRound()-1);

        isInBackground = false ;
        mColors = getResources().getIntArray(R.array.default_rainbow);

        mToggleButtonVibrate = (ToggleButton)findViewById(R.id.togglebutton_vibrate);

        mImageButtonBrightness = (ImageButton)findViewById(R.id.imagebutton_brightness);
        mImageButtonColorPicker = (ImageButton)findViewById(R.id.imagebutton_color_picker);

        mSeekBarVolume = (SeekBar)findViewById(R.id.seekbaar_sound);
        mSeekBarBrightness = (SeekBar)findViewById(R.id.seekbar_brightness);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.selector_warmup_icon,null)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_excercise_icon, null)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_rest_icon, null)));
        tabLayout.addTab(tabLayout.newTab().setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_success_icon, null)));

        mToggleButtonPlayback = (ToggleButton)findViewById(R.id.togglebutton_playback);
        mToggleButtonPlayback.setBackgroundColor(Color.TRANSPARENT);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mRelativelatyoutQuickAccess = (RelativeLayout)findViewById(R.id.relativelayout_quick_access);
        mLinearLayoutQuickAccess = (LinearLayout)findViewById(R.id.linearlayout_quickaccess);

        //disable tab touch
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }


        mImageButtonVolume = (ImageButton)findViewById(R.id.imagebutton_volume);
        mImageButtonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mLinearLayoutQuickAccess.setVisibility(View.GONE);
                    mSeekBarBrightness.setVisibility(View.GONE);
                    mImageButtonBrightness.setVisibility(View.GONE);
                    mSeekBarVolume.setVisibility(View.VISIBLE);
                    mImageButtonVolume.setVisibility(View.VISIBLE);
                    startSeekBarBarCountDown();
            }
        });


        final ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                mSelectedColor,
                5, // Number of columns
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                mSelectedColor = color;
                final PageSelectedListener listener = (PageSelectedListener) viewPagerAdapter.instantiateItem(viewPager, tabLayout.getSelectedTabPosition());
                if (listener != null) {
                    listener.setBackgroundColor(mSelectedColor);
                }
            }
        });


        mImageButtonColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getFragmentManager(), "color_dialog_test");            }
        });
        mImageButtonBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageButtonVolume.setVisibility(View.GONE);
                mSeekBarVolume.setVisibility(View.GONE);
                mLinearLayoutQuickAccess.setVisibility(View.GONE);
                mImageButtonBrightness.setVisibility(View.VISIBLE);
                mSeekBarBrightness.setVisibility(View.VISIBLE);
                startSeekBarBarCountDown();
            }
        });

        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float brightness = progress / (float) 255;
                WindowManager.LayoutParams layout = getWindow().getAttributes();
                layout.screenBrightness = brightness;
                getWindow().setAttributes(layout);
                startSeekBarBarCountDown();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        mSeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startSeekBarBarCountDown();
            }
        });

        mToggleButtonVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vibrateMode = true;
                if(isChecked)
                    vibrateMode = false;
            }
        });

        remainingTime = totaltime ;
        startTimer(remainingTime);
        updateProgressBar();

        setVolumeSeekbar();
        setBrightnessSeekbar();


        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        viewPager = (CustomViewPager)findViewById(R.id.viewpager);
        viewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),mTimer);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setBackgroundColor(Color.parseColor("#10FFFFFF"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                final PageSelectedListener listener = (PageSelectedListener) viewPagerAdapter.instantiateItem(viewPager, tab.getPosition());
                if (listener != null) {
                    listener.startTimerNow();
                }

                if (tab.getPosition() == 3) {
                    mToggleButtonPlayback.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_restart, null));
                }
                if (isInBackground)
                    updateNotification();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        mToggleButtonPlayback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final PageSelectedListener listener = (PageSelectedListener) viewPagerAdapter.instantiateItem(viewPager,tabLayout.getSelectedTabPosition());
                if (listener != null) {
                    listener.onPlaybackControlChange(mToggleButtonPlayback);
                }

                if(!isChecked){
                    isPlaying = true ;
                    mRelativelatyoutQuickAccess.animate().translationY(quickAccesslayoutHeight).setDuration(500);
                    startTimer(remainingTime);
                }else {
                    mRelativelatyoutQuickAccess.animate().translationY(-quickAccesslayoutHeight).setDuration(500);
                    isPlaying = false ;
                    countDownTimer.cancel();
                }

            }
        });

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(this,1,intent,0);
        builder = new NotificationCompat.Builder(getBaseContext());
        builder.setSmallIcon(R.drawable.ic_play);
        builder.setContentIntent(notificationIntent);
        builder.setContentText("Xtimer Running , Tap to Launch");
        builder.setContentTitle("Xtimer");
        timerNotification = builder.getNotification();
        mNotificationManager.cancel(11);
    }

    private void setBrightnessSeekbar() {
        int curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS,-1);
        mSeekBarBrightness.setProgress((curBrightnessValue*100)/255);
    }

    private void setVolumeSeekbar() {
        AudioManager am = (AudioManager)getSystemService(MainActivity.AUDIO_SERVICE);
        volume =(int) (am.getStreamVolume(AudioManager.STREAM_MUSIC) * 7.5);
        mSeekBarVolume.setProgress((volume));
    }

    private void hideactionbar() {
        try {
            getSupportActionBar().hide();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void startSeekBarBarCountDown() {
        if(seekBarTimer!= null)
        seekBarTimer.cancel();

        seekBarTimer = new CountDownTimer(2000,100){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                backtoNormalViews();
            }
        }.start();
    }

    private void backtoNormalViews() {
        mLinearLayoutQuickAccess.setVisibility(View.VISIBLE);
        mImageButtonBrightness.setVisibility(View.VISIBLE);
        mImageButtonVolume.setVisibility(View.VISIBLE);
        mSeekBarBrightness.setVisibility(View.GONE);
        mSeekBarVolume.setVisibility(View.GONE);
    }

    void startTimer(long time) {
        remainingTime = time ;
        countDownTimer = new CountDownTimer(remainingTime, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                    remainingTime = millisUntilFinished ;
                    updateProgressBar();
            }

            @Override
            public void onFinish() {
                mProgressBar.setProgress(100);
            }
        }.start();
    }

    private void updateProgressBar() {
        Thread t = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        {
                            progress = (((int)totaltime - (int)remainingTime )*100)/(int)totaltime ;
                            mProgressBar.setProgress(progress);

                        }
                    }
                });
            }
        };
        t.start();
    }

    private void updateNotification() {
        String message ;
        switch (tabLayout.getSelectedTabPosition()){
            case 0 : message = "Warmup Time     \nRound : "+ mTimer.timerDetail.round ; break;
            case 1 : message = "Excercise Time  \nRound : "+ mTimer.timerDetail.round ; break;
            case 2 : message = "Break Time      \nRound : "+ mTimer.timerDetail.round ; break;
            case 3 : message = "Success "; break;
            default:  message = "Success "; break;
        }
        builder.setContentTitle(message);
        timerNotification = builder.getNotification();
        mNotificationManager.notify(11, timerNotification);
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Timer will be cleared !");
        builder.setMessage("Are You sure you want to continue ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPlaying = false;
                SessionFragment.Timer.cancel();
                countDownTimer.cancel();
                supportFinishAfterTransition();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInBackground = true ;
        if(isPlaying)
        updateNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInBackground = false ;
        mNotificationManager.cancel(11);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        quickAccesslayoutHeight = mRelativelatyoutQuickAccess.getHeight();
    }

}

 class MyPagerAdapter extends FragmentStatePagerAdapter {
     Timer mTimer ;
     public MyPagerAdapter(FragmentManager fm ,  Timer mTimer ) {

         super(fm);
         this.mTimer = mTimer ;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0 : return WarmUpFragment.newInstance      (mTimer,        "warmup");
            case 1 : return ExcerciseFragment.newInstance   (mTimer,        "excercise");
            case 2 : return BreakFragment.newInstance       (mTimer,        "rest");
            case 3 : return SuccessFragment.newInstance     (mTimer,        "success");
            default: return SuccessFragment.newInstance     (mTimer,        "DEFAULT");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


     @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
 }


