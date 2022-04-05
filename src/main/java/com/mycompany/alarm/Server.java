/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.alarm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 *
 * @author kirill
 */
public class Server implements IAlarmObserver {
    boolean started = false;
    String status = "Server offline";
    ServerGUI gui;
    
    int port = 3124;
    InetAddress ip;
    ServerSocket ss;
    
    Timer timer = null;
    
    public Server(ServerGUI gui) {
        this.gui = gui;
        setStatus(status);
    }

    public void start() {
        if (started) return;
        started = true;
        try {
            timer = new Timer(this);
            timer.startTimer();
            ip = InetAddress.getLocalHost();

            ServerSocket ss = new ServerSocket(port, 0, ip);
            setStatus("Server Started on port:" + port);

            while (true) {        
                Socket cs = ss.accept();
                ClientObserver co = new ClientObserver(cs, this);
    //          co.isCient = false;
    //          m.addObserver(co);
            }


          } catch (IOException ex) {
                setStatus("Server cant start");
          }
    }
    
        
    public final void setStatus(String message) {
        status = message;
        gui.notify(status);
    }
    
    @Override
    public void timerTick(LocalDateTime newTime){
        String timerRepresentation = 
                newTime.getYear()+"-"+
                newTime.getMonth()+"-"+
                newTime.getDayOfMonth()+" "+
                newTime.getHour() + ":"+
                newTime.getMinute()+":"+
                newTime.getSecond();
        gui.notifyTimer(timerRepresentation);
    }
}
