/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.alarm;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author kirill
 */
public class DB {
    private static SessionFactory sessionFactory;
    
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Event.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e){
            }
        }
        return sessionFactory;
    }
    
    public static ArrayList<Event> getAllEvents() {
        Session session = DB.getSessionFactory().openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = builder.createQuery(Event.class);
        criteria.from(Event.class);
        List<Event> data = session.createQuery(criteria).getResultList();
        session.close();
        return new ArrayList<>(data);
    }
    

    public static void saveEvent(Event event) {
        Session session = DB.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(event);
        tx.commit();
        session.close();
    }
    
    public static void removeEvent(Event event) {
        Session session = DB.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.remove(event);
        tx.commit();
        session.close();
    }
    
}
