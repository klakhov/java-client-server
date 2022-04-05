package com.mycompany.alarm;


import java.time.LocalDateTime;

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
    
    public Message(String action){
        this.action = action;
    }
    
    public Message(String action, String serverTime){
        this.action = action;
        this.serverTime = serverTime;
    }
}
