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
public class ManageMessageCassandra {

    private String node;
    private Cluster cluster;
    private Session session;

    public ManageMessageCassandra(String node) {
        this.node = node;
    }

    private void connect() {
        cluster = Cluster.builder().addContactPoint(node).build();
        session = cluster.connect("simplehr");
    }

    private void close() {
        session.close();
        cluster.close();
    }

    public ResultSet listResult() {
        ResultSet result = null;
        connect();
        try {
            String query = "select * from message_sent";
            result = session.execute(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean addValue(String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn) {
        boolean result = false;
        connect();
        try {
            String query = "insert into message_sent(user_name,receiver_name, sent_time, message_text) values('" + userNameIn + "','" + receiverNameIn
                    + "','" + MessageUtils.formatDateTime(sentTimeIn) + "','" + messageTextIn + "');";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean updateValue(String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn) {
        boolean result = false;
        connect();
        try {
            String query = "update message_sent set message_text = '" + messageTextIn + "' where user_name = '" + userNameIn
                    + "' and receiver_name = '" + receiverNameIn + "' and sent_time = '" + MessageUtils.formatDateTime(sentTimeIn) + "';";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public boolean deleteValue(String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn) {
        boolean result = false;
        connect();
        try {
            String query = "delete from message_sent where user_name = '" + userNameIn
                    + "' and receiver_name = '" + receiverNameIn + "' and sent_time = '" + MessageUtils.formatDateTime(sentTimeIn) + "';";
            session.execute(query);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close();
        }
        return result;
    }
}
