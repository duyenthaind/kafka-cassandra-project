/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.netty.constants;

/**
 *
 * @author Admin
 */
public class TargetChat {
    private String sender;
    private String receiver;

    public TargetChat(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object obj) {
        return(this.sender.equals(((TargetChat)obj).sender) && this.receiver.equals(((TargetChat)obj).receiver));
    }
}
