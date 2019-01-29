package com.baoshen.common;

/**
 * Created by Shute on 2017/2/11.
 */
public class Stopwatch {
    private boolean isRunning;
    private long startTime;
    private long elapsed;
    public void stop(){
        if(!isRunning){
            return;
        }
        makeElapsed();
        isRunning = false;
    }
    public void start(){
        if(isRunning){
            return;
        }
        isRunning = true;
        startTime = Utils.getTime();
    }
    public void reset(){
        stop();
        startTime=0;
        elapsed=0;
    }

    public void restart(){
        reset();
        start();
    }

    public boolean getIsRunning(){
        return isRunning;
    }

    /**
     * Elapsed
     * @return
     */
    public long getElapsed() {
        makeElapsed();
        return elapsed;
    }

    /**
     * Elapsed
     * @return
     */
    public long getElapsedSecond() {
        return getElapsed()/1000;
    }

    private void makeElapsed(){
        if(isRunning){
            long now = Utils.getTime();
            elapsed = now - startTime;
        }
    }
}
