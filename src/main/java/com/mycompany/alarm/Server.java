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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;

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
    
    public final LinkedHashSet<ClientObserver> syncQueue = new LinkedHashSet();
    public final ArrayList<ClientObserver> clients = new ArrayList();
    public final ArrayList<Event> events = new ArrayList();
    public final ArrayList<Event> newEvents = new ArrayList();


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
                synchronized (clients){
                    clients.add(new ClientObserver(cs, this));                
                }
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
                newTime.getYear() +"-"+
                newTime.getMonth()+"-"+
                newTime.getDayOfMonth()+" "+
                (newTime.getHour()/10 == 0 ? "0"+newTime.getHour() : newTime.getHour()) + ":"+
                (newTime.getMinute()/10 == 0 ? "0"+newTime.getMinute() : newTime.getMinute())+":"+
                (newTime.getSecond()/10 == 0 ? "0"+newTime.getSecond() : newTime.getSecond());
        gui.notifyTimer(timerRepresentation);
        
        Thread syncThread = new Thread(() -> {
            synchronized(syncQueue) {
                syncQueue.forEach((client) -> {
                    synchronized(events){
                        client.sync(newTime, events);
                    }
                });
                syncQueue.clear();
            }
        });
        syncThread.start();
        
        Thread eventNotificationThread = new Thread(()->{
            synchronized(events) {
                LocalDateTime currentMoment = LocalDateTime.now();
                ArrayList<Event> pastEvents = new ArrayList();
                events.forEach((event) -> {
                    if(currentMoment.isAfter(event.getTimestamp())) {
                        pastEvents.add(event);
                        synchronized (clients){
                            clients.forEach((client) -> {
                                 client.sendEvent(event);
                            });
                        }
                    }
                });
                events.removeAll(pastEvents);
            }
        });
        eventNotificationThread.start();
        
        Thread eventsSyncThread = new Thread(() -> {
            synchronized(newEvents) {
                if(!newEvents.isEmpty()){
                    synchronized (clients){
                        clients.forEach((client) -> {
                            client.sendNewEvents(newEvents);
                        });
                    }
                    newEvents.clear();
                }
            }
        });
        eventsSyncThread.start();
    }
    
    public void newSyncClientRequest(ClientObserver client){
        synchronized (syncQueue){
            syncQueue.add(client);
        }
    }
    
    public void newEventClientRequest(ClientObserver client, Event event){
        synchronized (events){
            event.setId(UUID.randomUUID());
            events.add(event);
        }
        
        synchronized (newEvents){
            newEvents.add(event);
        }
    }
    
    public void disconnectClient(ClientObserver client) {
        synchronized (clients) {
            clients.remove(client);
        }
    }
}
