package com.mycompany.alarm;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kirill
 */
public class Message {
    public String action;
    public String serverTime;
    public UUID clientId;
    public Event event;
    public ArrayList<Event> newEvents = new ArrayList<Event>();
    
    public Message(){
    }
    
    public Message(String action, UUID id){
        this.action = action;
        this.clientId = id;
    }
    
    public Message(String action, String serverTime){
        this.action = action;
        this.serverTime = serverTime;
    }
    
    public void setId(UUID id){
        this.clientId = id;
    }
    
    public void setAction(String action){
        this.action = action;
    }
    
    public void setServerTime(String time) {
        this.serverTime = time;
    }
    
    public void setEvent(Event event){
        this.event = event;
    }
    
    public void setNewEvents(ArrayList<Event> newEvents){
        this.newEvents = newEvents;
    }
}
