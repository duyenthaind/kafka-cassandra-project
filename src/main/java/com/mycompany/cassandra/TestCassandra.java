/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Row;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.entities.MessageSent;
import com.mycompany.entities.MessageSentTest;
import com.mycompany.utils.MessageUtils;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author DuyenThai
 */
public class TestCassandra {

    static ManageMessageCassandraSpeedUp manage = new ManageMessageCassandraSpeedUp("127.0.0.1");

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        for (Row temp : manage.listResult()) {
            System.out.println(temp);
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));
        MessageSentTest msg = new MessageSentTest(UUID.randomUUID(), "h", "t", new Date(), "hi", "normal");

        time = System.currentTimeMillis();
        if (manage.addValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
            System.out.println("Insert successfully");
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        if (manage.updateValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
            System.out.println("Update sucessfully");
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        if (manage.deleteValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
            System.out.println("Delete sucessfully");
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));

        for (Row temp : manage.listResult()) {
            System.out.println(temp);
        }

        manage.close();
        System.exit(0);
    }
    
    public static String encodeJson(MessageSentTest message){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(message);
        return json;
    }
}
