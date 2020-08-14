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
public class MessageSent implements Serializable {

    private String messageId;
    private String userName;
    private String receiverName;
    private Date sentTime;
    private String messageText;
    private String typeInteract;

    public MessageSent() {
    }

    public MessageSent(UUID messageId, String userName, String receiverName, Date sentTime, String messageText, String typeInteract) {
        this.messageId = messageId.toString();
        this.userName = userName;
        this.receiverName = receiverName;
        this.sentTime = sentTime;
        this.messageText = messageText;
        this.typeInteract = typeInteract;
    }

    @Id
    @Column(name = "message_id", length = 20, nullable = false)
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    @Column(name = "user_name", length = 50, nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "receiver_name", length = 50, nullable = false)
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Column(name = "sent_time", length = 20, nullable = false)
    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    @Column(name = "message_text", length = 200, nullable = true)
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Column(name = "type_interact", length = 10, nullable = true)
    public String getTypeInteract() {
        return typeInteract;
    }

    public void setTypeInteract(String typeInteract) {
        this.typeInteract = typeInteract;
    }

    @Override
    public String toString() {
        return "MessageSent{" + "messageId=" + messageId + ", userName=" + userName + ", receiverName=" + receiverName + ", sentTime=" + sentTime + ", messageText=" + messageText + ", typeInteract=" + typeInteract + '}';
    }

}
