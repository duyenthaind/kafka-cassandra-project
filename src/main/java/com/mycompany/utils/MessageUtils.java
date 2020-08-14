/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.entities.MessageSent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author DuyenThai
 */
public class MessageUtils {
    public static String encodeJson(MessageSent message){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String json = gson.toJson(message);
        return json;
    }
    
    public static MessageSent decodeJson(String json){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        MessageSent message = gson.fromJson(json, MessageSent.class);
        return message;
    }
    
    public static String formatDateTime(Date time){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }
    
    public static Date parseDateTime(String time){
        try{
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        }catch(ParseException ex){
            return null;
        }
    }
    
    public static void printMessage(MessageSent message){
        System.out.println("");
        System.out.println(MessageUtils.formatDateTime(message.getSentTime()) + " to " + message.getReceiverName());
        System.out.println(message.getUserName()  + ": "+ message.getMessageText());
    }
}
