package com.savihealer.webstimer.model;

/**
 * Created by Savi on 06-05-2016.
 */
public class Timer {

   public TimerIntro timerIntro ;
    public TimerDetail timerDetail ;
    public TimerColors timerColors ;
    public TimerSounds timerSounds ;

    public Timer() {
        timerIntro = new TimerIntro();
        timerDetail = new TimerDetail(0,0,0,1);
        timerColors = new TimerColors();
        timerSounds = new TimerSounds();
    }


    public TimerIntro getTimerIntro() {
        return timerIntro;
    }

    public void setTimerIntro(TimerIntro timerIntro) {
        this.timerIntro = timerIntro;
    }

    public TimerDetail getTimerDetail() {
        return timerDetail;
    }

    public void setTimerDetail(TimerDetail timerDetail) {
        this.timerDetail = timerDetail;
    }

    public TimerColors getTimerColors() {
        return timerColors;
    }

    public void setTimerColors(TimerColors timerColors) {
        this.timerColors = timerColors;
    }

    public TimerSounds getTimerSounds() {
        return timerSounds;
    }

    public void setTimerSounds(TimerSounds timerSounds) {
        this.timerSounds = timerSounds;
    }
}
