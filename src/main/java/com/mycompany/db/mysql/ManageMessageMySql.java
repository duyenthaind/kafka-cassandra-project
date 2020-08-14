/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.db.mysql;

import com.mycompany.entities.MessageSent;
import com.mycompany.utils.HibernateUtil;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author DuyenThai
 */
public class ManageMessageMySql {

    private Session session;

    public ManageMessageMySql() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void closeSessionHql() {
        session.close();
    }

    public boolean addValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteract) {
        boolean result = false;
//        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            MessageSent msg = new MessageSent(UUID.randomUUID(),userNameIn, receiverNameIn, sentTimeIn, messageTextIn, typeInteract);
            session.save(msg);
            tx.commit();
            result = true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
//            session.close();
            return result;
        }
    }

    public boolean updateValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteract) {
        boolean result = false;
//        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        MessageSent msg = null;
        try {
            tx = session.beginTransaction();
            String hql = "from MessageSent where messageId =: messageIdIn and userName =:userNameIn and sentTime =:sentTimeIn and receiverName =:receiverNameIn";
            Query query = session.createQuery(hql);
            query.setParameter("messageIdIn", messageIdIn);
            query.setParameter("userNameIn", userNameIn);
            query.setParameter("sentTimeIn", sentTimeIn);
            query.setParameter("receiverNameIn", receiverNameIn);
            List<MessageSent> listResult = query.list();
            if (listResult.isEmpty()) {
                msg = null;
            } else {
                msg = listResult.get(0);
            }
            msg.setMessageText(messageTextIn);
            session.update(msg);
            tx.commit();
            result = true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
//            session.close();
            return result;
        }
    }

    public boolean deleteValue(String messageIdIn, String userNameIn, String receiverNameIn, Date sentTimeIn, String messageTextIn, String typeInteract) {
        boolean result = false;
//        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        MessageSent message = null;
        try {
            tx = session.beginTransaction();
            String hql = "from MessageSent where messageId =: messageIdIn and userName = :userNameIn and sentTime = :sentTimeIn and receiverName = :receiverNameIn";
            Query query = session.createQuery(hql);
            query.setParameter("messageIdIn", messageIdIn);
            query.setParameter("userNameIn", userNameIn);
            query.setParameter("sentTimeIn", sentTimeIn);
            query.setParameter("receiverNameIn", receiverNameIn);
            List<MessageSent> listResult = query.list();
            if (listResult.isEmpty()) {
                message = null;
            } else {
                message = listResult.get(0);
            }
            session.delete(message);
            tx.commit();
            result = true;
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            ex.printStackTrace();
        } finally {
//            session.close();
            return result;
        }
    }

    public List<MessageSent> listValue() {
//        Session session = HibernateUtil.getSessionFactory().openSession();
        List<MessageSent> listResults = null;
        try {
            String hql = "from MessageSent";
            Query query = session.createQuery(hql);
            listResults = query.list();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
//            session.close();
            return listResults;
        }
    }
}
