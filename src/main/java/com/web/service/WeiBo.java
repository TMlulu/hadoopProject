package com.web.service;

import com.web.utils.HbaseDDL;
import com.web.utils.HbaseDriver;
import com.web.utils.Names;
import com.web.utils.WebOption;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;

import javax.naming.Name;
import java.io.IOException;

public class WeiBo {
    static{
        final Connection conn = HbaseDriver.HbaseConn();
        try {
            final Admin admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* HbaseDDL.createSpaceName(Names.NAMESPACE);
        HbaseDDL.createTable(Names.TABLENAME_CONTENT,"info","content");*/
        //HbaseDDL.createTable(Names.TABLENAME_USER,"attention","fans");
       // HbaseDDL.createTable(Names.TABLENAME_RLS_,"info");
    }
    public static void main(String[] args) throws IOException {


         //WebOption.follow("004","002");
        //WebOption.releaseWeb("002");
         WebOption.lookWeb("001");
        //WebOption.follow("003","002");
        //WebOption.follow("001","003");


    }
}
