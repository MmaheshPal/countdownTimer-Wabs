package com.savihealer.webstimer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.savihealer.webstimer.adapters.ViewPagerAdapterMain;
import com.savihealer.webstimer.database.SQLiteHelper;
import com.savihealer.webstimer.interfaces.SetFadingBackground;
import com.savihealer.webstimer.model.Timer;
import com.savihealer.webstimer.R;


public class WelcomeActivity extends AppCompatActivity implements RadioButton.OnCheckedChangeListener{
    static public ViewPager viewPager ;
    static public ViewPagerAdapterMain viewPagerAdapter ;
    static public List<Timer> sessionList ;
    SQLiteHelper sqLiteHelper ;
    RadioGroup radioGroup ;
    int[] radioGroupIds ;
    ArrayList<String> sessionStringList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_welcome);
        sessionList = new ArrayList<>();
        sqLiteHelper = new SQLiteHelper(getBaseContext());
       if(savedInstanceState!=null)
        sessionStringList = savedInstanceState.getStringArrayList("session") ;
        else
        sessionStringList = sqLiteHelper.getList();

        if(sessionStringList.size()>0){
            for(int i = 0 ; i<sessionStringList.size();i++){
                sessionList.add(new Gson().fromJson(sessionStringList.get(i),Timer.class));
            }
        }

        findViewById(R.id.imageview_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(100, 100, 100, 100);
        viewPager.setPageMargin(50);
        radioGroup.clearCheck();
     radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*for(int i=0 ; i<radioGroupIds.length ;i++){
                    if(checkedId==radioGroupIds[i]){
                        viewPager.getChildAt(i);
                        break;
                    }
                }*/
               Toast.makeText(WelcomeActivity.this, checkedId + "", Toast.LENGTH_SHORT);
         }
     });

            if(sessionList.size()>=10)
                addRadioButtons(sessionList.size());
        else
                addRadioButtons(sessionList.size() + 1);


        viewPagerAdapter = new ViewPagerAdapterMain(getSupportFragmentManager(),sessionList);
        viewPager.setAdapter(viewPagerAdapter);
        radioGroup.check(radioGroupIds[getIntent().getIntExtra("position",0)]);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                SetFadingBackground background = (SetFadingBackground) viewPagerAdapter.instantiateItem(viewPager, position);
                background.setFadeDisable();
                radioGroup.check(radioGroupIds[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == viewPager.SCROLL_STATE_DRAGGING) {
                    SetFadingBackground background = (SetFadingBackground) viewPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                    background.setFadeEnable();
                }

                if (state == viewPager.SCROLL_STATE_IDLE) {
                    SetFadingBackground background = (SetFadingBackground) viewPagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                    background.setFadeDisable();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("session",sessionStringList);
    }

    public void addRadioButtons(int number) {
        radioGroupIds = new int[number];
        for (int row = 0; row < 1; row++) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            for (int i = 1; i <= number; i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setGravity(Gravity.CENTER );
                rdbtn.setId((row * 2) + i);
                rdbtn.setScaleX(0.7f);
                rdbtn.setScaleY(0.7f);
                rdbtn.setTag(i-1);
                rdbtn.setOnCheckedChangeListener(this);
                radioGroupIds[i-1]=(row * 2) + i ;
                ll.addView(rdbtn);
            }
            radioGroup.addView(ll);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            String tag = buttonView.getTag().toString();
            int currentItem = viewPager.getCurrentItem();
            if (currentItem != (Integer.parseInt(tag)))
                viewPager.setCurrentItem(Integer.parseInt(buttonView.getTag().toString()));
        }
    }
}
