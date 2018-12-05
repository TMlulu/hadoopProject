package com.qf.es;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class es01 {
    public static void main(String[] args) {
        try{
            //这里时测试代码
            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", "my-es").build();
            //创建client
            final TransportClient client = new PreBuiltTransportClient(settings).addTransportAddresses(
                    new InetSocketTransportAddress(InetAddress.getByName("192.168.136.150"), 9300),
                    new InetSocketTransportAddress(InetAddress.getByName("192.168.136.151"), 9300),
                    new InetSocketTransportAddress(InetAddress.getByName("192.168.136.152"), 9300)
            );
            // .getactionGet() 方法是同步方法，没有返回就等待
            //从索引库里查询
            GetResponse response = client.prepareGet("news","fulltext","3").execute().actionGet();
            System.out.println(response);
             client.close();


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
