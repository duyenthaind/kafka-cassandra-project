package com.mycompany.controller;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Admin
 */
public class MenuListBean {
    private String kind;
    private JPanel jpanel;
    private JLabel jlabel;
    private JTextArea jtaHistory;

    public MenuListBean() {
    }

    public MenuListBean(String kind, JPanel jpanel, JLabel jlabel, JTextArea jtaHistory) {
        this.kind = kind;
        this.jpanel = jpanel;
        this.jlabel = jlabel;
        this.jtaHistory = jtaHistory;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public JPanel getJpanel() {
        return jpanel;
    }

    public void setJpanel(JPanel jpanel) {
        this.jpanel = jpanel;
    }

    public JLabel getJlabel() {
        return jlabel;
    }

    public void setJlabel(JLabel jlabel) {
        this.jlabel = jlabel;
    }

    public JTextArea getJtaHistory() {
        return jtaHistory;
    }

    public void setJtaHistory(JTextArea jtaHistory) {
        this.jtaHistory = jtaHistory;
    }

    
}
