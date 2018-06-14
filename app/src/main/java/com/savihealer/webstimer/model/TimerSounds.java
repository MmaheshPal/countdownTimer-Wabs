package com.savihealer.webstimer.model;

/**
 * Created by Savi on 06-05-2016.
 */
public class TimerSounds {
  /*  final static String MALE = "male" ;
    final static String FEMALE = "female" ;*/

    public boolean voiceNotifiactions ;
    public  boolean countDownbeeps ;
    public  boolean isMale  ;

    public TimerSounds() {
        voiceNotifiactions = true ;
        countDownbeeps = true ;
        isMale = true;
    }
}
