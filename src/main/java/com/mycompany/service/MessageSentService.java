/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.service;

import com.datastax.driver.core.ResultSet;
import com.mycompany.entities.MessageSent;
import java.util.List;

/**
 *
 * @author DuyenThai
 */
public interface MessageSentService {
    
    boolean addValue(MessageSent message);
    
    boolean updateValue(MessageSent message);
    
    boolean deleteValue(MessageSent message);
    
    List<MessageSent> listValue();
    
    ResultSet listValueCass();
    
    void closeConnect();
}
