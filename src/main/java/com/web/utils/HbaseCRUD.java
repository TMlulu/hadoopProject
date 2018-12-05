package com.web.utils;

import org.apache.hadoop.hbase.TableName;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseCRUD {
    private static Connection conn;
    private static Table table;
    {
        conn = HbaseDriver.HbaseConn();
    }


    public static void putData(String tableName,String rowKey,String famliys,String colums,String value){
       Table table = null;
        try {
           table= conn.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(famliys),Bytes.toBytes(colums),
            Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {

            System.out.println("插入失败");
            e.printStackTrace();
        }


    }

}
