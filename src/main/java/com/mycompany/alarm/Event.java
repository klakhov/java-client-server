/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.alarm;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author kirill
 */
@Entity
@Table (name="alarm")
public class Event implements Serializable {
    @Id
    UUID _id;
    
    @Column
    String _timestamp;
    
    @Column
    String _message;
    
    public Event() {
        _id = UUID.randomUUID();
        _timestamp = LocalDateTime.now().toString();
        _message = "";
    }
    
    public Event(LocalDateTime timestamp, String message){
        this._timestamp = timestamp.toString();
        this._message = message;
    }
    
    public Event(String timestamp, String message){
        this._timestamp = timestamp;
        this._message = message;
    }
   
    public Event setId(UUID id){
        this._id = id;
        return this;
    }
    
    public UUID getID(){
        return this._id;
    }
    
    public Event setTimestamp(LocalDateTime timestamp) {
        this._timestamp = timestamp.toString();
        return this;
    }
    
    public Event setTimestamp(String timestamp) {
        this._timestamp = timestamp;
        return this;
    }
    
    public LocalDateTime getTimestamp() {
        return LocalDateTime.parse(_timestamp);
    }
    
    
    public Event setMessage(String message) {
        this._message = message;
        return this;
    }
    
    public String getMessage() {
        return this._message;
    }
}
