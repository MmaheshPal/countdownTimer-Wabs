package com.savihealer.webstimer.model;

/**
 * Created by devuser on 27-04-2016.
 */
public class TimerDetail {


    public long breakTime ;
    public long exerciseTime ;
    public long warmupTime ;
    public int round ;
    public int totalRound ;

    public TimerDetail(long warmupTime, long exerciseTime, long breakTime, int round) {
        this.warmupTime = warmupTime;
        this.breakTime = breakTime;
        this.round = round ;
        this.totalRound = round ;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public long getWarmupTime() {
        return warmupTime;
    }

    public void setWarmupTime(long warmupTime) {
        this.warmupTime = warmupTime;
    }

    public long getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(long breakTime) {
        this.breakTime = breakTime;
    }

    public long getExcerciseTime() {
        return exerciseTime;
    }

    public void setExcerciseTime(long exerciseTime) {
        this.exerciseTime = exerciseTime;
    }
}
