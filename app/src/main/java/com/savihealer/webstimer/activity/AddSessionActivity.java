package com.savihealer.webstimer.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import static com.savihealer.webstimer.utils.Constants.*;
import com.google.gson.Gson;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import com.savihealer.webstimer.database.SQLiteHelper;
import com.savihealer.webstimer.fragments.TimePickerDialogFragments.BreakTimePickerDialog;
import com.savihealer.webstimer.fragments.TimePickerDialogFragments.ExcerciseTimePicker;
import com.savihealer.webstimer.fragments.TimePickerDialogFragments.RoundTimePicker;
import com.savihealer.webstimer.fragments.TimePickerDialogFragments.TimePickerDialogCustom;
import com.savihealer.webstimer.fragments.TimePickerDialogFragments.WarmUpTimePicker;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


public class AddSessionActivity extends AppCompatActivity {
    TextView mTextViewBreakTime ;
    TextView mTextViewExcerciseTime ;
    TextView mTextViewWarmUpTime ;
    TextView mTextViewRounds ;
    protected String oldSessionString ;
    protected EditText mEditTextExcerciseName ;
    protected EditText mEditTextTag ;
    int itemposition ;
    Button mButtonSave ;

    RadioGroup mRadioGroupGender;
    protected RadioButton mRadioButtonMale ,mRadioButtonFemale ;
    
    SQLiteHelper mSQLiteHelper ;
    private int[] mColors ;
    Drawable background ;
    GradientDrawable gradientDrawable ;
    ImageView mImageViewCardColor , mImageViewPreparationTime ,mImageViewExcerciseTime ,mImageViewBreakTime ,mImageViewSuccessTime ;

    Switch mSwitchCountDownBeeps , mSwitchVoiceNotification ;

    LinearLayout mLinearLayoutGenderOptions ;
    Timer mTimer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);

        mEditTextExcerciseName = (EditText)findViewById(R.id.edittext_excerciseName);
        mEditTextTag = (EditText)findViewById(R.id.edittext_excerciseTag);
        setTimerData();
        itemposition = getIntent().getIntExtra("position",0);
        mTextViewBreakTime = (TextView) findViewById(R.id.textview_breakTime);
        mTextViewExcerciseTime = (TextView) findViewById(R.id.textview_exerciseTime);
        mTextViewWarmUpTime = (TextView) findViewById(R.id.textview_preparationTime);
        mTextViewRounds = (TextView) findViewById(R.id.textview_number_of_rounds);

        mLinearLayoutGenderOptions = (LinearLayout)findViewById(R.id.linearlayout_gender_options);
        setTimerTime();

        mImageViewCardColor = (ImageView)findViewById(R.id.imageview_cardColor);
        ((GradientDrawable)mImageViewCardColor.getBackground()).setColor(mTimer.timerColors.cardColor);

        mImageViewPreparationTime = (ImageView)findViewById(R.id.imageview_preparationTime);
        ((GradientDrawable)mImageViewPreparationTime.getBackground()).setColor(mTimer.timerColors.warmupColor);

        mImageViewExcerciseTime = (ImageView)findViewById(R.id.imageview_exerciseTime);
        ((GradientDrawable)mImageViewExcerciseTime.getBackground()).setColor(mTimer.timerColors.exerciseColor);

        mImageViewBreakTime = (ImageView)findViewById(R.id.imageview_breakTime);
        ((GradientDrawable)mImageViewBreakTime.getBackground()).setColor(mTimer.timerColors.breakColor);

        mImageViewSuccessTime = (ImageView)findViewById(R.id.imageview_successTime);
        ((GradientDrawable)mImageViewSuccessTime.getBackground()).setColor(mTimer.timerColors.successColor);


        mRadioGroupGender = (RadioGroup)findViewById(R.id.radio_group_gender);
        mRadioButtonMale = (RadioButton)findViewById(R.id.radio_button_male);
        mRadioButtonFemale = (RadioButton)findViewById(R.id.radio_button_female);

        setRadioButton();
        mButtonSave = (Button)findViewById(R.id.buton_save);

        mSwitchVoiceNotification = (Switch)findViewById(R.id.switch_voiceNotification);
        mSwitchCountDownBeeps = (Switch)findViewById(R.id.switch_countdown_beeps);
        setSwitchs();
        mSQLiteHelper = new SQLiteHelper(getBaseContext());


        mColors = getResources().getIntArray(R.array.default_rainbow);
        mTextViewBreakTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogCustom dialog = BreakTimePickerDialog.newInstance("Break Time");
                dialog.show(getFragmentManager(), "BreakTime");
            }
        });

        mTextViewExcerciseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogCustom dialog = ExcerciseTimePicker.newInstance("Excercise Time");
                dialog.show(getFragmentManager(),"ExcerciseTime");
            }
        });

        mTextViewWarmUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogCustom dialog = WarmUpTimePicker.newInstance("WarmUp Time");
                dialog.show(getFragmentManager(), "WarmUpTime");
            }
        });

        mTextViewRounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogCustom dialog = RoundTimePicker.newInstance("Rounds");
                dialog.show(getFragmentManager(), "Rounds");
            }
        });

        mImageViewCardColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = mImageViewCardColor.getBackground();
                gradientDrawable = (GradientDrawable)background;
                getColorFromPicker(gradientDrawable, CARD_COLOR);
        }
        });

        mImageViewPreparationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = mImageViewPreparationTime.getBackground();
                gradientDrawable = (GradientDrawable)background;
                getColorFromPicker(gradientDrawable, WARMUP_COLOR);
            }
        });

        mImageViewExcerciseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = mImageViewExcerciseTime.getBackground();
                gradientDrawable = (GradientDrawable)background;
                getColorFromPicker(gradientDrawable,EXERCISE_COLOR);
            }
        });

        mImageViewBreakTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = mImageViewBreakTime.getBackground();
                gradientDrawable = (GradientDrawable)background;
                getColorFromPicker(gradientDrawable,BREAK_COLOR);
            }
        });

        mImageViewSuccessTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background = mImageViewSuccessTime.getBackground();
                gradientDrawable = (GradientDrawable)background;
                getColorFromPicker(gradientDrawable,SUCCESS_COLOR);

            }
        });

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSaveClick(v);

            }
        });

        mSwitchCountDownBeeps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTimer.timerSounds.countDownbeeps = isChecked;
            }
        });

        mSwitchVoiceNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setGenderLayoutVisibility();
                mTimer.timerSounds.voiceNotifiactions = isChecked ;
            }
        });

        mRadioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_button_male)
                mTimer.timerSounds.isMale= true ;
                else
                mTimer.timerSounds.isMale = false ;
            }
        });
    }

    protected void setTimerTime() {
    }

    protected void setSwitchs() {
        setGenderLayoutVisibility();
    }

    protected void setRadioButton() {
        mRadioButtonMale.setChecked(true);

    }

    protected void setTimerData() {
        mTimer = new Timer();
    }

    protected void showSnackBar(String message,View v) {
        Snackbar snack =Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;        tv.setTextColor(getResources().getColor(R.color.red));
        view.setLayoutParams(params);
        snack.show();
    }


    public void setBreakTime(int min, int secs) {
        mTextViewBreakTime.setText(getFormattedMinSecs(min, secs));
        mTimer.timerDetail.breakTime = getMilliseconds(min,secs);
    }

    public void setExcerciseTime(int min, int secs) {
        mTextViewExcerciseTime.setText(getFormattedMinSecs(min, secs));
        mTimer.timerDetail.exerciseTime = getMilliseconds(min,secs);
    }

    public void setWarmupTime(int min, int secs) {
        mTextViewWarmUpTime.setText(getFormattedMinSecs(min,secs));
        mTimer.timerDetail.warmupTime = getMilliseconds(min, secs);
    }

    private long getMilliseconds(int min, int secs) {
        return (min * 60000 ) + (secs * 1000);
    }

    public void setRounds(int rounds) {
        String round = rounds+"" ;

        if(rounds<10)
            round = "0"+round;

        mTextViewRounds.setText(round);
        mTimer.timerDetail.totalRound = rounds ;
        mTimer.timerDetail.round = rounds ;
    }

    protected void setGenderLayoutVisibility(){
        if(mSwitchVoiceNotification.isChecked()){
            mLinearLayoutGenderOptions.animate().alpha(1.0f).setDuration(1000);
            mLinearLayoutGenderOptions.setVisibility(View.VISIBLE);
        }else {
            mLinearLayoutGenderOptions.animate().alpha(0.0f).setDuration(2000);;
            mLinearLayoutGenderOptions.setVisibility(View.GONE);
        }
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

    private void getColorFromPicker(final GradientDrawable gradientDrawable, final int nameTag){
        final ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                mColors,
                getSelectedColor(nameTag),
                4, // Number of columns
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                switch (nameTag){
                    case CARD_COLOR :   mTimer.timerColors.cardColor =color ; break;
                    case WARMUP_COLOR :   mTimer.timerColors.warmupColor = color ; break;
                    case EXERCISE_COLOR :   mTimer.timerColors.exerciseColor = color ; break;
                    case BREAK_COLOR :   mTimer.timerColors.breakColor = color ; break;
                    case SUCCESS_COLOR :   mTimer.timerColors.successColor = color ; break;
                }
                gradientDrawable.setColor(color);
            }
        });

        dialog.show(getFragmentManager(), "Select");
    }

    private int getSelectedColor(int nameTag) {
        int color ;
        switch (nameTag){
            case CARD_COLOR :   color = (mTimer.timerColors.cardColor); ; break;
            case WARMUP_COLOR :   color = (mTimer.timerColors.warmupColor);  break;
            case EXERCISE_COLOR :  color = (mTimer.timerColors.exerciseColor);  break;
            case BREAK_COLOR :   color = (mTimer.timerColors.breakColor); break;
            case SUCCESS_COLOR :  color = (mTimer.timerColors.successColor);  break;
            default: return 0;
        }
        return color;
    }


    public void onSaveClick(View view) {
        if(TextUtils.isEmpty(mEditTextExcerciseName.getText().toString())) {
            showSnackBar("Excercise Name Cannot be Empty", view);
            return;
        }
        if(mTimer.timerDetail.exerciseTime==0) {
            showSnackBar("Excercise Time Cannot be 00:00",view);
            return;
        }
        mTimer.timerIntro.excerciseName =mEditTextExcerciseName.getText().toString();
        mTimer.timerIntro.tag =mEditTextTag.getText().toString();

        Log.i("Saving",new Gson().toJson(mTimer));
        mSQLiteHelper.insertSessionString(new Gson().toJson(mTimer));

        Toast.makeText(getBaseContext(),"Success",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getBaseContext(),WelcomeActivity.class);
        intent.putExtra("position",itemposition);
        startActivity(intent);
    }
}
