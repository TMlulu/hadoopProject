package com.web.utils;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import scala.util.control.Exception;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class WebOption {
    private static Connection conn ;
    static{
        conn = HbaseDriver.HbaseConn();
    }

    //发布微博
    public static void  releaseWeb(String user) throws IOException {

        final Scanner scanner = new Scanner(System.in);
        System.out.println("请输入web内容");
        final String content = scanner.next();
        Table table = conn.getTable(TableName.valueOf(Names.TABLENAME_CONTENT));
        final Table userTable = conn.getTable(TableName.valueOf(Names.TABLENAME_USER));
        final Table rlsTable = conn.getTable(TableName.valueOf(Names.TABLENAME_RLS_));
        final long timeMillis = System.currentTimeMillis();
        String rowKey = user+"_"+timeMillis;
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("content"),Bytes.toBytes(content));

        final Put put2 = new Put(Bytes.toBytes(user));
        put2.addColumn(Bytes.toBytes("info"),Bytes.toBytes(user), Bytes.toBytes(rowKey));
        rlsTable.put(put2);
        try{
            table.put(put);
            //获取粉丝列表
            final Get get = new Get(Bytes.toBytes(user));
            get.addFamily(Bytes.toBytes("fans"));
            final Result result = userTable.get(get);
            final Cell[] cells1 = result.rawCells();

            for(Cell cell : cells1){
                //粉丝的id
                System.out.println(new String(CellUtil.cloneQualifier(cell)));
                final byte[] colum = CellUtil.cloneQualifier(cell);
                final Put put1 = new Put(colum);
                //放到响应粉丝的收件箱中
                put1.addColumn(Bytes.toBytes("info"),Bytes.toBytes(user),Bytes.toBytes(rowKey));
                rlsTable.put(put1);
            }


        }catch (java.lang.Exception e){
            System.out.println("发布失败");
            e.printStackTrace();
        }finally {
            table.close();
            userTable.close();
            rlsTable.close();
        }
        System.out.println("发布成功");
    }





    //关注 user 关注了 attention
    public static void follow(String user, String attention) throws IOException {
        //在用户中添加关注
        final Table userTable = conn.getTable(TableName.valueOf(Names.TABLENAME_USER));
        System.err.println("**************");
        final Put put = new Put(Bytes.toBytes(user));
        put.addColumn(Bytes.toBytes("attention"),Bytes.toBytes(attention),Bytes.toBytes(attention));
        userTable.put(put);


        //在被关注中添加粉丝
        final Put put1 = new Put(Bytes.toBytes(attention));
        put1.addColumn(Bytes.toBytes("fans"),Bytes.toBytes(user),Bytes.toBytes(user));
        userTable.put(put1);


        //为关注者(用户)发送 被关注者的最近的微博
        //被关注的发布的微博id
        List<byte[]> contentList = new ArrayList<>();
        final Table contentTable = conn.getTable(TableName.valueOf(Names.TABLENAME_CONTENT));
        final RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(attention + "_"));
        final Scan scan = new Scan();
        scan.setFilter(rowFilter);
        final ResultScanner scanner = contentTable.getScanner(scan);
        for(Result result : scanner){
            final Cell[] cells = result.rawCells();
            for (Cell cell : cells){
                final byte[] bytes = CellUtil.cloneRow(cell);
                contentList.add(bytes);
            }
        }

        Collections.sort(contentList,(a,b)->{return  (new String(a)).compareTo(new String(b));});
        //获取关系表
        final Table rlsTable = conn.getTable(TableName.valueOf(Names.TABLENAME_RLS_));


        if(contentList.size()>=5){
           for(int i=0;i<5;i++){
               final byte[] webRowKey = contentList.get(i);
               final Put put2 = new Put(Bytes.toBytes(user));
               put2.addColumn(Bytes.toBytes("info"),Bytes.toBytes(attention),webRowKey);
               rlsTable.put(put2);
           }
       }else{
           for (int i=0;i<contentList.size();i++){
               final byte[] webRowKey = contentList.get(i);
               final Put put2 = new Put(Bytes.toBytes(user));
               put2.addColumn(Bytes.toBytes("info"),Bytes.toBytes(attention),webRowKey);
               rlsTable.put(put2);
           }
       }


        rlsTable.close();
        contentTable.close();
        userTable.close();

        System.err.println("&&&&&&&&&&&&&&&^^^^^^^^^^^^^^^^^^^^^^");
    }



    //取消关注
    public static void  unfollow(String user,String attention) throws IOException {
        final Table userTable = conn.getTable(TableName.valueOf(Names.TABLENAME_USER));
        final Delete delete = new Delete(Bytes.toBytes(user));
        delete.addColumn(Bytes.toBytes("attention"),Bytes.toBytes(attention));
        userTable.delete(delete);

        final Delete delete1 = new Delete(Bytes.toBytes(attention));
        delete1.addColumn(Bytes.toBytes("fans"),Bytes.toBytes(user));
        userTable.delete(delete1);


        final Table rlsTable = conn.getTable(TableName.valueOf(Names.TABLENAME_RLS_));

        final RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(attention + "_"));
        final Scan scan = new Scan();
        /*final Scan scan1 =*/ scan.setFilter(rowFilter);
        final Delete delete2 = new Delete(Bytes.toBytes(user));

        delete2.addColumn(Bytes.toBytes("info"),Bytes.toBytes(attention));

        rlsTable.delete(delete2);

        rlsTable.close();
        userTable.close();



    }


    //显示博客, 查看user的博客(user刷新博客)，每刷新一次显示最新的20条

    public static void lookWeb(String user) throws IOException {
        final Table rlsTable = conn.getTable(TableName.valueOf(Names.TABLENAME_RLS_));
        //存放这个用户的所有关注着的微博
        List<String> list = new ArrayList<>();
        final Get get = new Get(Bytes.toBytes(user));
        final Result result = rlsTable.get(get);
        final Cell[] cells = result.rawCells();
        for(Cell cell: cells){
            final byte[] bytes = CellUtil.cloneQualifier(cell);
            list.add(new String(bytes));
        }

        final Comparator<String> comparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                final String[] s1 = o1.split("_");
                final String[] s2 = o2.split("_");
                return s1[1].compareTo(s2[1])!=0?  s1[1].compareTo(s2[1]):-1;
            }
        };
        Collections.sort(list,comparator);

        //存放前20条的rowKey
        List<String> list1 = new ArrayList<>();

        final Table contentTable = conn.getTable(TableName.valueOf(Names.TABLENAME_CONTENT));
        for(int i=0;i<list.size();i++){
            if(i==20){
                break;
            }
            //根据rowKey去contentTable表中取内容
            System.out.println(list.get(i));

            final RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(list.get(i) + "_"));
            final Scan scan = new Scan();
            /*final Scan scan1 =*/ scan.setFilter(rowFilter);

            /*Get get1 = new Get(Bytes.toBytes(list.get(i)));
            get1.addColumn(Bytes.toBytes("info"),Bytes.toBytes("content"));
            final Result result1 = contentTable.get(get1);*/
            final ResultScanner scanner = contentTable.getScanner(scan);

            for(Result result1: scanner){
                final Cell[] cells1 = result1.rawCells();
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
                for(Cell cell : cells1){
                    System.out.println("******************************");
                    System.out.println(new String (CellUtil.cloneValue(cell)));
                }
            }


        }
        rlsTable.close();
        contentTable.close();

    }




}
