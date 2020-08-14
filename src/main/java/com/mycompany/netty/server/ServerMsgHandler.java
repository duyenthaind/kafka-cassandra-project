/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.netty.server;

import com.mycompany.entities.MessageSent;
import com.mycompany.netty.constants.Constants;
import com.mycompany.netty.constants.TargetChat;
import com.mycompany.service.MessageSentServiceImpl;
import com.mycompany.service.MessageSentService;
import com.mycompany.utils.MessageUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Admin
 */
public class ServerMsgHandler extends SimpleChannelInboundHandler<MessageSent> {

    static String[] nodes = {"127.0.0.1", "127.0.0.2", "127.0.0.3"};
    static final Map<String, Channel> channelTarget = new HashMap<String, Channel>();
    static final List<String> listUsers = new ArrayList<String>();
    static final List<Channel> channels = new ArrayList<Channel>();
    static final List<TargetChat> listTarget = new ArrayList<TargetChat>();
    MessageSentService messageService = new MessageSentServiceImpl(nodes);

    /*
	 * Whenever client connects to server through channel, add his channel to the
	 * list of channels.
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        System.out.println("Server is available!");
        channels.add(ctx.channel());
        System.out.println(channels);
        MessageSent message2 = new MessageSent(UUID.randomUUID(), Constants.SERVER, null, new Date(), listUsers.toString(), "make");
        ctx.writeAndFlush(message2);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageSent msg) throws Exception {
        try {
            System.out.print("Server receive - ");
            MessageUtils.printMessage(msg);
            messageService.addValue(msg);
            if (!listUsers.contains(msg.getUserName())) {
                listUsers.add(msg.getUserName());
                channelTarget.put(msg.getUserName(), ctx.channel());
                System.out.println(listUsers);
            }
            if (msg.getReceiverName().equalsIgnoreCase("all")) {
                for (Channel channel : channels) {
                    channel.writeAndFlush(msg);
                }
            } else {
                if (!listUsers.contains(msg.getReceiverName())) {
                    MessageSent message = new MessageSent(UUID.randomUUID(), Constants.SERVER, msg.getUserName(), new Date(), "No available client or offline!", "none");
                    ctx.writeAndFlush(message);
                } else {
                    for (Map.Entry<String, Channel> tar : channelTarget.entrySet()) {
                        if (tar.getKey().equals(msg.getReceiverName())) {
                            tar.getValue().writeAndFlush(msg);
                            ctx.writeAndFlush(msg);
                            System.out.println("hit");
                        }
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("Closing connection for client - " + ctx);
        String userRemove = "";
        List<String> userListToRemove = new ArrayList<>();
        for (Map.Entry<String, Channel> tar : channelTarget.entrySet()) {
            if (tar.getValue().equals(ctx.channel())) {
                userRemove = tar.getKey();
                userListToRemove.add(tar.getKey());
            }
        }
        channelTarget.remove(userRemove);
        channels.remove((Channel) ctx.channel());
        listUsers.removeAll(userListToRemove);
        ctx.close();
        System.out.println(channels);
    }

}
