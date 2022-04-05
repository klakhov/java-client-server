/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.alarm;

import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kirill
 */
public class Client implements IAlarmObserver{
    boolean started = false;
    ClientGUI gui;
    
    int port = 3124;
    InetAddress ip;
    Socket cs;
    Timer clientTimer = null;
    
    DataInputStream dis;
    DataOutputStream dos;
    Gson convert = new Gson();
    
    public Client(ClientGUI gui){
        this.gui = gui;
    }
    
    public void start(){
        if (started) return;
        started = true;
        try{
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
        }
        
        try {
            cs = new Socket(ip, port);
            dis = new DataInputStream(cs.getInputStream());
            dos = new DataOutputStream(cs.getOutputStream());
            while(true){
                String messageJson;
                messageJson = dis.readUTF();
                Message message = convert.fromJson(messageJson, Message.class);
                if("sync".equals(message.action)){
                    if(clientTimer != null){
                        System.out.println("Got sync message");
                        clientTimer.setTime(message.serverTime);
                    } else {
                        clientTimer = new Timer(this, message.serverTime);
                        clientTimer.startTimer();
                    }
                }
                
            }
        } catch (IOException ex) {
          Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void timerTick(LocalDateTime newTime) {
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
