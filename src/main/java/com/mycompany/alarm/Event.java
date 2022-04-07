/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.alarm;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author kirill
 */
public class Event {
    public UUID id;
    public String timestamp;
    public String message;
    
    public Event(LocalDateTime timestamp, String message){
        this.timestamp = timestamp.toString();
        this.message = message;
    }
    
    public Event(String timestamp, String message){
        this.timestamp = timestamp;
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return LocalDateTime.parse(timestamp);
    }
    
    public void setId(UUID id){
        this.id = id;
    }
}
