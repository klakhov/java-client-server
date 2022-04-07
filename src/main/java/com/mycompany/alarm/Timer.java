package com.mycompany.alarm;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kirill
 */
public class Timer {
    LocalDateTime currentTime;
    Thread timeThread = null;
    IAlarmObserver observer;
    boolean started = false;
    
    public Timer(IAlarmObserver observer){
        currentTime = LocalDateTime.now();
        this.observer = observer;
    }
    
    public Timer(IAlarmObserver observer, String initTime){
        currentTime = LocalDateTime.parse(initTime);
        this.observer = observer;
    }
    
    public void startTimer() {
        started = true;
        timeThread = new Thread(()-> {
            while(started){
                try {
                    Thread.sleep(1000);
                    setTime(currentTime.plusSeconds(1));
                    observer.timerTick(currentTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        timeThread.start();
    }
    
    public void stopTimer() {
        started = false;
    }
    
    public synchronized void setTime(String time){
        currentTime = LocalDateTime.parse(time);
    }
    
    public synchronized void setTime(LocalDateTime time){
        currentTime = time;
    }
}
