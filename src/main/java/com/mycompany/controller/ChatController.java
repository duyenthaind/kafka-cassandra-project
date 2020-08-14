/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import static com.mycompany.controller.ChatController.listItem;
import static com.mycompany.controller.ChatController.listUsers;
import com.mycompany.entities.MessageSent;
import com.mycompany.kafka.artifact.KafKaConsumer;
import com.mycompany.kafka.artifact.KafKaProducer;
import com.mycompany.netty.client.ClientDecodeMsgHandler;
import com.mycompany.netty.client.ClientMsgHandler;
import com.mycompany.netty.client.MessageEncoder;
import com.mycompany.utils.FrameUtils;
import com.mycompany.utils.MessageUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;

/**
 *
 * @author DuyenThai
 */
public class ChatController {

    private JTextField jtfUser;
    private JTextField jtfHost;
    private JTextField jtfPort;
    private JButton btnSend;
    private JButton btnConnect;
    private JTextArea jtaHistoryChat;
    private JTextArea jtaMessage;
    private JPanel jpnUser;
    private JButton btnLogOut;
    private Consumer<Long, String> consumer = null;
    private Producer<Long, String> producer = null;
    private KafKaProducer kafkaProducer = null;
    private RunTimeThread threadRun;

    private String kindSelected = "";
    public static List<MenuListBean> listItem = new CopyOnWriteArrayList<MenuListBean>();
    static List<String> listUsers = new CopyOnWriteArrayList<String>();
    private MenuListBean choosed = null;

    public ChatController(JTextField jtfUser, JTextField jtfHost, JTextField jtfPort, JButton btnSend, JButton btnConnect, JTextArea jtaHistoryChat, JTextArea jtaMessage, JPanel jpnUser, JButton btnLogOut) {
        this.jtfUser = jtfUser;
        this.jtfHost = jtfHost;
        this.jtfPort = jtfPort;
        this.btnSend = btnSend;
        this.btnConnect = btnConnect;
        this.jtaHistoryChat = jtaHistoryChat;
        this.jtaMessage = jtaMessage;
        this.jpnUser = jpnUser;
        this.btnLogOut = btnLogOut;
        this.btnLogOut.setEnabled(false);
        this.threadRun = new RunTimeThread();
        kafkaProducer = new KafKaProducer();
        consumer = KafKaConsumer.createConsumer();
    }
    NioEventLoopGroup workLoopGroup = new NioEventLoopGroup();
    ChannelFuture channelFuture = null;

    public void setHandle() {

        btnConnect.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (jtfUser.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(new JFrame(), "Fill in username!");
                        return;
                    }
                    producer = kafkaProducer.createProducer();
                    threadRun.start();
                    initUser();
                    MessageSent msg = new MessageSent(UUID.randomUUID(), jtfUser.getText(), "all", new Date(), jtfUser.getText() + " has joined!", "join");
                    kafkaProducer.runProducer(1, msg, producer);

                    btnLogOut.setEnabled(true);
                    btnConnect.setEnabled(false);
                    jtfUser.setEditable(false);

                    Bootstrap clientBootstrap = new Bootstrap();
                    clientBootstrap.group(workLoopGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel channel) throws Exception {
                                    channel.pipeline().addLast(new MessageEncoder(), new ClientDecodeMsgHandler(), new ClientMsgHandler(jtaHistoryChat, jtfUser, jtfHost, jpnUser));
                                }
                            }).option(ChannelOption.SO_KEEPALIVE, true);
                    channelFuture = clientBootstrap.connect(jtfHost.getText(), Integer.parseInt(jtfPort.getText())).sync();
                    System.out.println("hit");
                    JOptionPane.showMessageDialog(new JFrame(), "Connect successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), "Failed to connect!");
                    ex.printStackTrace();
                } 
            }
        });

        btnSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (jtfUser.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Fill in user name!");
                    return;
                }
                if (jtaMessage.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Fill in message!");
                    return;
                }
                new NettyRuntimeThread().start();
            }
        });

        btnLogOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                endProcess();
            }
        });
    }

    public void endProcess() {
        MessageSent msg = new MessageSent(UUID.randomUUID(), jtfUser.getText(), "all", new Date(), jtfUser.getText() + " has left!", "leave");
        try {
            kafkaProducer.runProducer(1, msg, producer);
        } catch (Exception ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        producer.close();
        threadRun.closeThread();
        System.exit(0);
    }

    private void initUser() {
        jpnUser.setLayout(new GridBagLayout());
        MenuListBean item = FrameUtils.addListItem(listItem, "server");
        FrameUtils.addPanel(jpnUser, item.getJpanel());
        item.getJlabel().setText("all");
        item.getJpanel().add(item.getJlabel());
        jpnUser.validate();
        jpnUser.repaint();
        setEven(listItem);
        listUsers.add(jtfUser.getText());
    }

    private void setEven(List<MenuListBean> menuListBeans) {
        this.listItem = menuListBeans;
        for (MenuListBean item : menuListBeans) {
            item.getJlabel().addMouseListener(new LabelEvent(item.getKind(), item.getJpanel(), item.getJlabel()));
        }
    }

    private void addNewUserPanel(MessageSent msg, String userName) {
        if (!FrameUtils.existsListItem(listItem, msg.getUserName()) && !userName.equals(jtfUser.getText())) {
            listUsers.add(userName);
            MenuListBean itemBean = FrameUtils.addListItem(listItem, userName);
            FrameUtils.addPanel(jpnUser, itemBean.getJpanel());
            itemBean.getJpanel().add(itemBean.getJlabel());
            jpnUser.revalidate();
            jpnUser.repaint();
            setEven(listItem);
        }
    }

    private void displayMessage(MessageSent msg) {
        for (MenuListBean beanChoosed : listItem) {
            if (msg.getReceiverName() != null) {
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

    private void initAlterUserPanel(MessageSent msg) {
        String stringList = msg.getMessageText();
        stringList = stringList.replace("[", "").replace("]", "");
        String contents[] = stringList.split(",");
        System.out.println(contents);
        for (String content : contents) {
            if (!listUsers.contains(content.trim())) {
                listUsers.add(content);
            }
        }
        System.out.println("listUsers new: " + listUsers.toString());
        FrameUtils.renewJpnUser(listItem, listUsers, jpnUser, jtfUser);
        setEven(listItem);
    }

    private void deleteUserPanel(MessageSent msg) {
        for (MenuListBean menuListBean : listItem) {
            System.out.println("To remove" + menuListBean.getKind().equals(msg.getMessageText().substring(0, msg.getMessageText().indexOf(" has left!"))));
            if (menuListBean.getKind().equals(msg.getUserName())) {
                listUsers.remove(menuListBean.getKind());
                listItem.remove(menuListBean);
            }
        }
        FrameUtils.renewJpnUser(listItem, listUsers, jpnUser, jtfUser);
        setEven(listItem);
        System.out.println("list " + listUsers);
        System.out.println("DONE");
    }

    private void makeList() {
        MessageSent message = new MessageSent(UUID.randomUUID(), "server", null, new Date(), listUsers.toString(), "make");
        try {
            kafkaProducer.runProducer(1, message, producer);
        } catch (Exception ex) {
            Logger.getLogger(ChatController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    class LabelEvent implements MouseListener {

        private JTextArea node;

        private String kind;
        private JPanel jpnItem;
        private JLabel jlbItem;

        public LabelEvent(String kind, JPanel jpnItem, JLabel jlbItem) {
            this.kind = kind;
            this.jpnItem = jpnItem;
            this.jlbItem = jlbItem;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            for (MenuListBean bean : listItem) {
                if (kind.equals(bean.getKind())) {
                    choosed = bean;
                    node = choosed.getJtaHistory();
                    break;
                }
            }
            jtaHistoryChat.removeAll();
            jtaHistoryChat.setLayout(new BorderLayout());
            jtaHistoryChat.setText("");
            jtaHistoryChat.add(node);
            jtaHistoryChat.validate();
            jtaHistoryChat.repaint();
            setChangeBackground(kind);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            jpnItem.setBackground(new Color(96, 100, 191));
            jlbItem.setBackground(new Color(96, 100, 191));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            jpnItem.setBackground(new Color(96, 100, 191));
            jlbItem.setBackground(new Color(96, 100, 191));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!kindSelected.equalsIgnoreCase(kind)) {
                jpnItem.setBackground(new Color(76, 175, 80));
                jlbItem.setBackground(new Color(76, 175, 80));
            }
        }

        private void setChangeBackground(String kind) {
            for (MenuListBean item : listItem) {
                if (item.getKind().equalsIgnoreCase(kind)) {
                    item.getJpanel().setBackground(new Color(96, 100, 191));
                    item.getJlabel().setBackground(new Color(96, 100, 191));
                } else {
                    item.getJpanel().setBackground(new Color(76, 175, 80));
                    item.getJlabel().setBackground(new Color(76, 175, 80));
                }
            }
        }
    }

    class RunTimeThread extends Thread {

        private AtomicBoolean running = new AtomicBoolean(true);

        @Override
        public void run() {

            while (running.get()) {
                final ConsumerRecords<Long, String> consumerRecords = consumer.poll(1000);

                consumerRecords.forEach(record -> {
                    System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
                            record.key(), record.value(),
                            record.partition(), record.offset());

                    MessageSent msg = MessageUtils.decodeJson(record.value());

                    String userName = msg.getUserName();
                    jtaHistoryChat.append(MessageUtils.formatDateTime(msg.getSentTime()) + " to " + msg.getReceiverName() + "\n" + msg.getUserName() + ": " + msg.getMessageText());
                    switch (msg.getTypeInteract()) {
                        case "normal":
                            addNewUserPanel(msg, userName);
                            break;
                        case "make":
                            initAlterUserPanel(msg);
                            addNewUserPanel(msg, userName);
                            break;
                        case "leave":
                            deleteUserPanel(msg);
                            break;
                        case "join":
                            addNewUserPanel(msg, userName);
                            makeList();
                            break;
                        default:
                            break;
                    }
//                    displayMessage(msg);
                });
                consumer.commitAsync();
            }
            consumer.close();
            consumer.unsubscribe();
            System.out.println("DONE");
        }

        public void closeThread() {
            running.set(false);

        }
    }

    class NettyRuntimeThread extends Thread {

        @Override
        public void run() {
            try {
                MessageSent message = new MessageSent(UUID.randomUUID(), jtfUser.getText(), choosed.getJlabel().getText(), new Date(), jtaMessage.getText(), "normal");
                Channel channel = channelFuture.sync().channel();
                channel.writeAndFlush(message);
                jtaMessage.setText("");
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                workLoopGroup.shutdownGracefully();
            }
        }
    }

}
