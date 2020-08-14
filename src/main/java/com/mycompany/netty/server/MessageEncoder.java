/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.netty.server;

import com.mycompany.entities.MessageSent;
import com.mycompany.utils.MessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Admin
 */
public class MessageEncoder extends MessageToByteEncoder<MessageSent>{

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageSent message, ByteBuf out) throws Exception {
        String content = MessageUtils.encodeJson(message);
        out.writeInt(content.length());
        out.writeBytes(content.getBytes(StandardCharsets.UTF_8));
    }
    
}
