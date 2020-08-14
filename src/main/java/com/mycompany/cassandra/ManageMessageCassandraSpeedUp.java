/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.mycompany.entities.MessageSent;
import com.mycompany.entities.MessageSentTest;
import com.mycompany.utils.MessageUtils;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author DuyenThai
 */
public class ManageMessageCassandraSpeedUp {

    private String node;
    private Cluster cluster;
    private Session session;

    public ManageMessageCassandraSpeedUp(String node) {
        this.node = node;
        connect();
    }

    private void connect() {
        cluster = Cluster.builder().addContactPoints("127.0.0.1","127.0.0.2","127.0.0.3").build();
        session = cluster.connect("simplehr");
    }

    public void close() {
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
                        String query = "insert into message_sent JSON" + "'" +TestCassandra.encodeJson(new MessageSentTest(UUID.fromString(messageIdIn), userNameIn, receiverNameIn, sentTimeIn, messageTextIn, typeInteractIn)) + "'";

//            String query = "insert into message_sent(message_id, user_name, receiver_name, sent_time, message_text, type_interact) values('" + 
//                    messageIdIn + "','" + userNameIn + "','" + receiverNameIn + "','" + MessageUtils.formatDateTime(sentTimeIn) + "','" + messageTextIn 
//                    +"','" + typeInteractIn +"');";
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
