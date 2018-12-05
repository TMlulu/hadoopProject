package com.web.utils;


import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;


public class HbaseDDL {
    private static Connection conn ;
    private static  Admin admin;

    static{
        conn = HbaseDriver.HbaseConn();
        try  {
            Admin admin1 = admin = conn.getAdmin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param spaceName 创建namespace 的名称
     */
    public static void createSpaceName(String spaceName ){
        final NamespaceDescriptor ds = NamespaceDescriptor.create(spaceName).build();
        try {
            admin.createNamespace(ds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createTable(String tableName,String ... familyColumn){
        final HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        for(String column:familyColumn){
            final HColumnDescriptor columnDescriptor = new HColumnDescriptor(column);
            if(tableName.equals(Names.TABLENAME_RLS_)){
                columnDescriptor.setMinVersions(100);
                columnDescriptor.setMaxVersions(100);
            }

            tableDescriptor.addFamily(columnDescriptor);
        }
        try {
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            System.out.println("创建表失败");
            e.printStackTrace();
        }
    }




}
