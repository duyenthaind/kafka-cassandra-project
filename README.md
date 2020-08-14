# kafka-cassandra-project
A chat with kafka-netty-cassandra

Requirements:
- JDK 8
- Kafka apache 2.5.0
- Cassandra apache 3.11.6
- MySQL

Cassandra 
- This example is performed on a cluster with 3 node
- Change the node at CassandraConnector class to 1 node to run the project properly
- Keyspace:
CREATE KEYSPACE simplehr WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3'}  AND durable_writes = true;
- Column family used:
CREATE TABLE simplehr.message_sent (message_id text PRIMARY KEY,message_text text,receiver_name text,sent_time timestamp,type_interact text,user_name text);


KafKa
- Topic name: testDemo
- Partitions: 100

MySQL
- Framework: Hibernate
- Schema: Create base on cassandra and entities file java
