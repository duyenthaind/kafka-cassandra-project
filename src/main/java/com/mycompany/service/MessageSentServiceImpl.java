/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.service;

import com.datastax.driver.core.ResultSet;
import com.mycompany.cassandra.TestCluster;
import com.mycompany.db.mysql.ManageMessageMySql;
import com.mycompany.entities.MessageSent;
import java.util.List;

/**
 *
 * @author DuyenThai
 */
public class MessageSentServiceImpl implements MessageSentService{
    private ManageMessageMySql controller = null;
    private TestCluster controllerCass = null;

    public MessageSentServiceImpl(String[] node) {
        this.controller = new ManageMessageMySql();
        this.controllerCass = new TestCluster(node);
    }

    @Override
    public boolean addValue(MessageSent message) {
        return controller.addValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract()) && 
                controllerCass.addValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract());
    }

    @Override
    public boolean updateValue(MessageSent message) {
        return controller.updateValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract()) &&
                controllerCass.updateValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract());
    }

    @Override
    public boolean deleteValue(MessageSent message) {
        return controller.deleteValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract()) &&
                controllerCass.deleteValue(message.getMessageId() ,message.getUserName(), message.getReceiverName(), message.getSentTime(), message.getMessageText(), message.getTypeInteract());
    }

    @Override
    public List<MessageSent> listValue() {
        return controller.listValue();
    }

    @Override
    public ResultSet listValueCass() {
        return controllerCass.listResult();
    }

    @Override
    public void closeConnect() {
        controllerCass.close();
        controller.closeSessionHql();
    }

}
