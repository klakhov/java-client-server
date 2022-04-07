package com.mycompany.alarm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import java.io.OutputStream;
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
public class ClientObserver {
  Server server;
  Socket cs;
  UUID clientId;

  DataInputStream dis;
  DataOutputStream dos;
  Gson convert = new Gson();

//  Model m = BModel.build();
  
  
  public ClientObserver(Socket cs, Server server) {
    this.cs = cs;
    this.server = server;
    this.clientId = UUID.randomUUID();
    System.out.println("Подключился клиент  \n");
    
    try {
      dos = new DataOutputStream(cs.getOutputStream());
    
      Thread clientThread = new Thread(
      ()->{
      try {
        dis = new DataInputStream(cs.getInputStream());
        dos = new DataOutputStream(cs.getOutputStream());
        while (true) {
            String messageJson;
            messageJson = dis.readUTF();
            Message message = convert.fromJson(messageJson, Message.class);
            if("sync".equals(message.action)){
                System.out.println(clientId+" Sever Got sync message");
                server.newSyncClientRequest(this);
            } else if ("new-event".equals(message.action)){
                System.out.println(clientId+" Sever Got new event message");
                server.newEventClientRequest(this, message.event);
            }
        }
        
      } catch (IOException ex) {
      }   
      }
    );
    clientThread.start();
    } catch (IOException ex) {
      Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
    }
}
  public void sync(LocalDateTime newTime, ArrayList<Event> events){
      Message message = new Message();
      message.setId(clientId);
      message.setAction("sync");
      message.setServerTime(newTime.toString());
      message.setNewEvents(events);
      String messageJson = convert.toJson(message);
      try {
          dos.writeUTF(messageJson);
      } catch (IOException ex) {
          Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  
  public void sendEvent(Event event){
      Message message = new Message();
      message.setId(clientId);
      message.setAction("event");
      message.setServerTime(server.timer.currentTime.toString());
      message.setEvent(event);
      String messageJson = convert.toJson(message);
      try {
          dos.writeUTF(messageJson);
      } catch (IOException ex) {
          Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  
    public void sendNewEvents(ArrayList<Event> events){
      Message message = new Message();
      message.setId(clientId);
      message.setAction("new-event");
      message.setServerTime(server.timer.currentTime.toString());
      message.setNewEvents(events);
      String messageJson = convert.toJson(message);
      try {
          dos.writeUTF(messageJson);
      } catch (IOException ex) {
          Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
}
