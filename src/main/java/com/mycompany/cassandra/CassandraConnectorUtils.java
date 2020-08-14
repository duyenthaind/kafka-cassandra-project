/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 *
 * @author DuyenThai
 */
public class CassandraConnectorUtils {
    private Cluster cluster;
    private Session session;
    
    public Session getSession(String node, int port){
        cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
        session = cluster.connect();
        return this.session;
    }
    
    public void close(){
        session.close();
        cluster.close();
    }
}
