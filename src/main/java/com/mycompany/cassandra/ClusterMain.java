/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Row;
import com.mycompany.entities.MessageSent;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author DuyenThai
 */
public class ClusterMain {

    static String[] nodes = {"127.0.0.1", "127.0.0.2", "127.0.0.3"};
    static TestCluster manage = new TestCluster(nodes);

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        List<MessageSent> listMsg = new CopyOnWriteArrayList<MessageSent>();
        for (Row temp : manage.listResult()) {
            MessageSent message = new MessageSent();
            System.out.println(temp);
            message.setMessageId(temp.getString(0));
            message.setUserName(temp.getString(5));
            message.setReceiverName(temp.getString(2));
            message.setSentTime(temp.getTimestamp(3));
            message.setMessageText(temp.getString(1));
            message.setTypeInteract(temp.getString(4));
            listMsg.add(message);
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));
        System.out.println();
        listMsg.forEach((msg1) -> {
            System.out.println(msg1);
        });
        System.out.println("\n" + listMsg.size() + " record !\n");

        time = System.currentTimeMillis();
        MessageSent msg = new MessageSent(UUID.randomUUID(), "h", "t", new Date(), "hi", "normal");
        if (manage.addValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
            System.out.println("Insert successfully");
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));
        System.out.println();
        
        time = System.currentTimeMillis();
        if (manage.updateValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
            System.out.println("Update sucessfully");
        }
        System.out.println("Time processed" + (System.currentTimeMillis() - time));
        System.out.println();

//        time = System.currentTimeMillis();
//        if (manage.deleteValue(msg.getMessageId(), msg.getUserName(), msg.getReceiverName(), msg.getSentTime(), msg.getMessageText(), msg.getTypeInteract())) {
//            System.out.println("Delete sucessfully");
//        }
//        System.out.println("Time processed" + (System.currentTimeMillis() - time));
//        System.out.println();

        for (Row temp : manage.listResult()) {
            System.out.println(temp);
        }

        manage.close();
        System.exit(0);
    }
}
