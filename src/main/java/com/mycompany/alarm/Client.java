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
import java.util.UUID;
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
    
    UUID clientId;
    
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
            syncRequest();
            while(true){
                String messageJson;
                messageJson = dis.readUTF();
                Message message = convert.fromJson(messageJson, Message.class);
                if(null != message.action)switch (message.action) {
                    case "sync" -> {
                        clientId = message.clientId;
                        System.out.println(clientId+" Client Got sync message");
                        if(clientTimer != null){
                            clientTimer.setTime(message.serverTime);
                        } else {
                            clientTimer = new Timer(this, message.serverTime);
                            clientTimer.startTimer();
                        }   gui.setEvents(message.newEvents);
                    }
                    case "event" -> {
                        System.out.println(clientId+" Client Got event message");
                        gui.removeEvent(message.event);
                        gui.showNotification(message.event.getMessage()+" on " + message.event.getTimestamp());
                    }
                    case "new-event" -> {
                        System.out.println(clientId+" Client Got new event message");
                        gui.addNewEvents(message.newEvents);
                    }
                    default -> {
                    }
                }
            }
        } catch (IOException ex) {
          Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void syncRequest(){
        Message message = new Message();
        message.setAction("sync");
        String messageJson = convert.toJson(message);
        try {
            dos.writeUTF(messageJson);
        } catch (IOException ex) {
            Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void newEventRequest(Event event) {
        Message message = new Message();
        message.setAction("new-event");
        message.setId(clientId);
        message.setEvent(event);
        String messageJson = convert.toJson(message);
        try {
            dos.writeUTF(messageJson);
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
                (newTime.getHour()/10 == 0 ? "0"+newTime.getHour() : newTime.getHour()) + ":"+
                (newTime.getMinute()/10 == 0 ? "0"+newTime.getMinute() : newTime.getMinute())+":"+
                (newTime.getSecond()/10 == 0 ? "0"+newTime.getSecond() : newTime.getSecond());
        gui.notifyTimer(timerRepresentation);
    }
}
