/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import static com.stratio.cassandra.lucene.builder.Builder.phrase;
import static com.stratio.cassandra.lucene.builder.Builder.prefix;
import static com.stratio.cassandra.lucene.builder.Builder.search;

/**
 *
 * @author DuyenThai
 */
public class TestLuceneIndexSearch {

    static String[] nodes = {"127.0.0.1"};
    private static Session session = null;
    private Cluster cluster = null;

    public TestLuceneIndexSearch() {
        connect();
    }

    private void connect() {
        Cluster.Builder builder = Cluster.builder();
        for (String node : nodes) {
            builder.addContactPoint(node);
        }
        cluster = builder.withCredentials("admin", "admin").build();
        session = cluster.connect("simplehr");
    }

    public static void main(String[] args) {
        TestLuceneIndexSearch indexSearch = new TestLuceneIndexSearch();
        for (Row row : indexSearch.getListCensus()) {
            System.out.println(row);
        }
        
        System.out.println("");
        try {
            ResultSet rs = session.execute("select *from census where expr(census_index2,?)", search().query(phrase("body", "mot la").slop(2)).build());
            for (Row row : rs) {
                System.out.println(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        System.out.println("");
        try {
            ResultSet rs = session.execute("select *from census where expr(census_index2,?)", search().query(phrase("body", "mot la cai").slop(3)).build());
            for (Row row : rs) {
                System.out.println(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.exit(0);
    }

    public ResultSet getListCensus() {
        ResultSet result = null;
        try {
            String query = "select* from census";
            result = session.execute(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }

    public ResultSet executeSeachQuery(String query) {
        ResultSet result = null;
        try {
            result = session.execute(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return result;
        }
    }
}
