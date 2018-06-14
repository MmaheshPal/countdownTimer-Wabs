package com.savihealer.webstimer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

import com.savihealer.webstimer.fragments.SessionFragments.SessionPageFragment;
import com.savihealer.webstimer.model.Timer;


public class ViewPagerAdapterMain extends FragmentStatePagerAdapter {
    List<Timer> sessionList = new ArrayList<>();
    public static boolean isLast = false ;
    public ViewPagerAdapterMain(FragmentManager fm, List<Timer> sessionList) {
        super(fm);
        this.sessionList = sessionList ;
    }

    @Override
    public Fragment getItem(int position) {

            if (position >= sessionList.size()) {
                isLast = true ;
                return SessionPageFragment.newInstance(null);
            }
        isLast = false ;
        return SessionPageFragment.newInstance(sessionList.get(position));
    }

    @Override
    public int getCount() {
        if(sessionList.size()>=10)
            return sessionList.size();
        else
            return sessionList.size()+1;
    }

   /* @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }*/

}
