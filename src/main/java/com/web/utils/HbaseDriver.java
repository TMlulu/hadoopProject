package com.web.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class HbaseDriver {

    public static Connection HbaseConn(){
        final Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","192.168.136.150:2181," +
                "192.168.136.151:2181,192.168.136.152:2181");
        Connection  conn = null;
        try {
           conn = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
