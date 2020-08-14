/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.netty.client;

import com.mycompany.controller.ChatController;
import com.mycompany.controller.MenuListBean;
import com.mycompany.entities.MessageSent;
import com.mycompany.utils.MessageUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class ClientMsgHandler extends SimpleChannelInboundHandler<MessageSent> {

    private JTextArea jtaHistoryChat;
    private JTextField jtfUser;
    private JTextField jtfHost;
    private JPanel jpnUser;
    static boolean flagAdd;
    static List<MenuListBean> listItem = new CopyOnWriteArrayList<MenuListBean>();

    public ClientMsgHandler(JTextArea jtaHistoryChat, JTextField jtfUser, JTextField jtfHost, JPanel jpnUser) {
        this.jtaHistoryChat = jtaHistoryChat;
        this.jtfUser = jtfUser;
        this.jtfHost = jtfHost;
        this.jpnUser = jpnUser;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessageSent message = new MessageSent(UUID.randomUUID(), jtfUser.getText(), "all", new Date(), jtfUser.getText() + " has joined!", "join");
        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageSent msg) throws Exception {
        if (!listItem.equals(ChatController.listItem)) {
            listItem = ChatController.listItem;
        }
        displayMessage(msg);
    }

    private void displayMessage(MessageSent msg) {
        if (msg.getReceiverName() != null) {
            for (MenuListBean beanChoosed : listItem) {
                if (msg.getReceiverName().equals("all")) {
                    if (beanChoosed.getJlabel().getText().equals("all")) {
                        beanChoosed.getJtaHistory().append(MessageUtils.formatDateTime(msg.getSentTime()) + " to " + msg.getReceiverName() + "\n" + msg.getUserName() + ": " + msg.getMessageText() + "\n");
                        break;
                    }
                } else {
                    if (beanChoosed.getKind().equals(msg.getUserName()) || beanChoosed.getKind().equals(msg.getReceiverName())) {
                        beanChoosed.getJtaHistory().append(MessageUtils.formatDateTime(msg.getSentTime()) + " to " + msg.getReceiverName() + "\n" + msg.getUserName() + ": " + msg.getMessageText() + "\n");
                    }
                }
            }
        }
    }

}
