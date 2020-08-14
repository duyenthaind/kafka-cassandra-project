/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.mycompany.utils.MessageUtils;
import java.util.Date;

/**
 *
 * @author DuyenThai
 */
public class TestCluster {
    private final String[] nodes;
    private Cluster cluster;
    private Session session;

    public TestCluster(String[] nodes) {
        this.nodes = nodes;
        connect();
    }
    
    public Session getSession(){
        return session;
    }
    
    public Cluster getCluster(){
        return cluster;
    }
    
    private void connect(){
        Cluster.Builder builder = Cluster.builder();
        for(String node : nodes){
            builder.addContactPoint(node);
        }
        cluster = builder.build();
        session = cluster.connect("simplehr");
    }
    
    public void close(){
        session.close();
        cluster.close();
    }
    
    public ResultSet listResult() {
        ResultSet result = null;
        try {
            String query = "select * from message_sent";
            result = session.execute(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }

    public boolean addValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteractIn) {
        boolean result = false;
        try {
            String query = "insert into message_sent(message_id, user_name, receiver_name, sent_time, message_text, type_interact) values('" + 
                    messageIdIn + "','" + userNameIn + "','" + receiverNameIn + "','" + MessageUtils.formatDateTime(sentTimeIn) + "','" + messageTextIn 
                    +"','" + typeInteractIn +"');";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }

    public boolean updateValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteractIn) {
        boolean result = false;
        try {
            String query = "update message_sent set message_text = '" + messageTextIn + "' where message_id = " + "'" + messageIdIn+ "';";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }

    public boolean deleteValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteractIn) {
        boolean result = false;
        try {
            String query = "delete from message_sent where message_id = " + "'" + messageIdIn+ "';";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }
}
