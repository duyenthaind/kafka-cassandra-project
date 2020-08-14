/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "message_sent")
public class MessageSentTest implements Serializable {

    private String message_id;
    private String user_name;
    private String receiver_name;
    private Date sent_time;
    private String message_text;
    private String type_interact;

    public MessageSentTest() {
    }

    public MessageSentTest(UUID messageId, String userName, String receiverName, Date sentTime, String messageText, String typeInteract) {
        this.message_id = messageId.toString();
        this.user_name = userName;
        this.receiver_name = receiverName;
        this.sent_time = sentTime;
        this.message_text = messageText;
        this.type_interact = typeInteract;
    }

    @Id
    @Column(name = "message_id", length = 20, nullable = false)
    public String getMessageId() {
        return message_id;
    }

    public void setMessageId(String messageId) {
        this.message_id = messageId;
    }
    
    @Column(name = "user_name", length = 50, nullable = false)
    public String getUserName() {
        return user_name;
    }

    public void setUserName(String userName) {
        this.user_name = userName;
    }

    @Column(name = "receiver_name", length = 50, nullable = false)
    public String getReceiverName() {
        return receiver_name;
    }

    public void setReceiverName(String receiverName) {
        this.receiver_name = receiverName;
    }

    @Column(name = "sent_time", length = 20, nullable = false)
    public Date getSentTime() {
        return sent_time;
    }

    public void setSentTime(Date sentTime) {
        this.sent_time = sentTime;
    }

    @Column(name = "message_text", length = 200, nullable = true)
    public String getMessageText() {
        return message_text;
    }

    public void setMessageText(String messageText) {
        this.message_text = messageText;
    }

    @Column(name = "type_interact", length = 10, nullable = true)
    public String getTypeInteract() {
        return type_interact;
    }

    public void setTypeInteract(String typeInteract) {
        this.type_interact = typeInteract;
    }

    @Override
    public String toString() {
        return "MessageSentTest{" + "message_id=" + message_id + ", user_name=" + user_name + ", receiver_name=" + receiver_name + ", sent_time=" + sent_time + ", message_text=" + message_text + ", type_interact=" + type_interact + '}';
    }
}
