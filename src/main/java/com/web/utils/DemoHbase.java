package com.web.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class DemoHbase {
    public static void main(String[] args) throws IOException {
        final Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","192.168.136.150:2181," +
                "192.168.136.151:2181,192.168.136.152:2181");
        conf.set("hbase.master.port", "60000");
        final Connection connection = ConnectionFactory.createConnection(conf);

       /* final Admin admin = connection.getAdmin();
        admin.createTable(new  HTableDescriptor(TableName.valueOf("test")));
        admin.close();*/
        final Table test = connection.getTable(TableName.valueOf("test"));
        Put put = new Put(Bytes.toBytes("002"));
        put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("age"),Bytes.toBytes("19"));
        test.put(put);
        connection.close();

    }
}
