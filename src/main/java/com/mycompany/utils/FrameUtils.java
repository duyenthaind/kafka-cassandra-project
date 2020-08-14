/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.utils;

import com.mycompany.controller.MenuListBean;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author DuyenThai
 */
public class FrameUtils {

    public static void addPanel(JPanel jpnRoot, JPanel jpnUserCommon) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = jpnRoot.getComponentCount();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.ipady = 40;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 1.0;
        jpnRoot.add(jpnUserCommon, gbc);
        jpnRoot.revalidate();
        jpnRoot.invalidate();
        jpnRoot.repaint();
    }

    public static MenuListBean addListItem(List<MenuListBean> listItem, String user) {
        JPanel jpanel = new JPanel();
        JLabel jlable = new JLabel();
        JTextArea jtextarea = new JTextArea();
        jlable.setText(user);
        jpanel.setName("jlb" + user);
        jlable.setName("jlb" + user);
        jlable.setFont(new Font("Calibri", 1, 14));
        jlable.setForeground(Color.BLACK);
        jtextarea.setName("jta" + user);
        jpanel.add(jlable);
        jpanel.setSize(new Dimension(169, 40));
        jpanel.setLayout(new BorderLayout());
        MenuListBean bean = new MenuListBean(user, jpanel, jlable, jtextarea);
        listItem.add(bean);
        return bean;
    }

    public static boolean existsListItem(List<MenuListBean> listItem, String user) {
        for (MenuListBean bean : listItem) {
            if (bean.getKind().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public static void renewJpnUser(List<MenuListBean> listItem, List<String> listUser, JPanel jpnRoot, JTextField jtfUser) {
        jpnRoot.removeAll();
        jpnRoot.revalidate();
        jpnRoot.invalidate();
        jpnRoot.repaint();
        listItem.clear();
        MenuListBean item = FrameUtils.addListItem(listItem, "server");
        FrameUtils.addPanel(jpnRoot, item.getJpanel());
        item.getJlabel().setText("all");
        item.getJpanel().add(item.getJlabel());
        if (!listUser.isEmpty()) {
            for (String user : listUser) {
                if (!jtfUser.getText().equals(user)) {
                    MenuListBean addExistedItem = addListItem(listItem, user);
                    addPanel(jpnRoot, addExistedItem.getJpanel());
                    addExistedItem.getJpanel().add(addExistedItem.getJlabel());
                } else {
                    continue;
                }
            }
        }
        jpnRoot.validate();
        jpnRoot.repaint();
    }
}
