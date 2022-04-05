package com.mycompany.alarm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import java.io.OutputStream;

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

  DataInputStream dis;
  DataOutputStream dos;
  Gson convert = new Gson();

//  Model m = BModel.build();
  
  
  public ClientObserver(Socket cs, Server server) {
    this.cs = cs;
    this.server = server;
    System.out.println("Подключился клиент  \n");
    
    try {
      dos = new DataOutputStream(cs.getOutputStream());
    
      Thread clientThread = new Thread(
      ()->{
      try {
        dis = new DataInputStream(cs.getInputStream());
        dos = new DataOutputStream(cs.getOutputStream());
        // TODO
        // wait for new events from client
        // (in parallel send from timer)
        while (true) {          
            // send sync data
            Message message = new Message("sync", server.timer.currentTime.toString());
            String messageJson = convert.toJson(message);
            dos.writeUTF(messageJson);
            Thread.sleep(2000);
//          Msg msg = convert.fromJson(obj, Msg.class);
//          System.out.println("Получил " + msg);
//          m.setP(msg.getP(), msg.getId());
          
        }
        
      } catch (IOException ex) {
      }   catch (InterruptedException ex) {
              Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
          }
       
      }
    );
    clientThread.start();

    
    } catch (IOException ex) {
      Logger.getLogger(ClientObserver.class.getName()).log(Level.SEVERE, null, ex);
    }
}

}
