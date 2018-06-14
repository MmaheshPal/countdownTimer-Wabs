package com.savihealer.webstimer.utils;

import android.content.Context;
import android.graphics.Typeface;


public class Constants {

    static Typeface mFont  ;
    public static final int CARD_COLOR = 1111 ;
    public static final int WARMUP_COLOR = 2222 ;
    public static final int EXERCISE_COLOR = 3333 ;
    public static final int BREAK_COLOR = 4444 ;
    public static final int SUCCESS_COLOR = 5555 ;

    public static final int HALF_SECOND = 500 ;
    public static final int ONE_SECOND = 1000 ;
    final static String MALE = "male" ;
    final static String FEMALE = "female" ;
    public static Typeface getTypeface(Context context, String typeface) {
        typeface = "fonts/"+typeface+".ttf" ;
        mFont = Typeface.createFromAsset(context.getAssets(), typeface);
        return mFont;
    }
}
