package com.qf.es;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;

public class ESXRUD {
    private TransportClient client = null;


    @Before
    public void init() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "my-es")
               //sniff  自动感知功能（可以通过当前指定的节点获取所有es节点的信息）
                //意思就是当你的elastic节点有很多台时，你可以不用全部指定
                //只需要指定最少2台，因为一台不安全，如果只有一条，当挂掉了，就连接不上了

                .put("client.transport.sniff", true)
                .build();

        //创建client端
        client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new InetSocketTransportAddress(InetAddress.getByName("192.168.136.150"),9300),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.136.151"),9300),
                new InetSocketTransportAddress(InetAddress.getByName("192.168.136.152"),9300)
        );

    }

    @Test
    public void testCreate() throws IOException {

       IndexResponse response = client.prepareIndex("gamelog", "users", "3")
                .setSource(
                        //默认是不建立索引的
                        jsonBuilder().startObject()
                                .field("username", "老韩")
                                .field("gender", "male")
                                .field("birthday", new Date())
                                .field("fv", 1000)
                                .field("message", "trying out ElasticSearch")
                                .endObject()
                ).get();

    }



    //查询一条数据
    @Test
    public void testGet(){
        //get相当于actionGet，是同步的方法
        GetResponse response = client.prepareGet("gamelog","users","1").get();

        //只打印数据
        System.out.println(response.getSourceAsString());
        //直接打印response对象，会把元数据也打印
        System.out.println(response);
    }


    //查找多条
    @Test
    public void testMultiGet(){
        final MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("gamelog", "users", "1")
                .add("gamelog", "users", "2")
                .add("gamelog","users","3")
                .add("news", "fulltext", "1")
                .get();
        for (MultiGetItemResponse itemResponse : multiGetItemResponses){
            GetResponse response = itemResponse.getResponse();
            if(response.isExists()){
                String json = response.getSourceAsString();
                System.out.println(json);
            }
        }
    }


    //更新
    @Test
    public void testUpdate() throws IOException, ExecutionException, InterruptedException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("gamelog");
        updateRequest.type("users");
        updateRequest.id("1");
        updateRequest.doc(
                jsonBuilder().startObject()
                    .field("fv",999.66)
                .endObject()
        );
        client.update(updateRequest).get();
    }

    //删除一条数据
    @Test
    public void testDelete(){
        final DeleteResponse response = client.prepareDelete("gamelog", "users", "2").get();
        System.out.println(response);
    }


    //按照查询条件删除删除
    @Test
    public void testDeleteByQuery(){
        final BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                //指定查询条件
                //有分词器，会对删除内容进行分词，用的是标准分词器，一个字一分
                .filter(QueryBuilders.matchQuery("username",
                        "段老师"))
                //指定查询的索引名称
                .source("gamelog")
                .get();
         long deleted = response.getDeleted();
        System.out.println(deleted);

    }

    @Test
    public void testDeleteByQueryAsync(){
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender","mele"))
                .source("gamelog")
                .execute(new ActionListener<BulkByScrollResponse>(){

                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        final long deleted = response.getDeleted();
                        System.out.println("数据删除了");
                        System.out.println(deleted);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });


        try {
            System.out.println("异步删除");
            //因为是异步进程，如果不等待10000毫秒，那么就会发生子线程还没执行完，主线程就已经退出了，接收不到结果
            Thread.sleep(10001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testRange(){
        final RangeQueryBuilder qu = rangeQuery("fv")
                //没有指定以什么方式建索引，默认是按字符串建索引
                //88.99 > 10000
                //想要按照数字建立索引需要设置，LongPoint，DoublePoint
                .from(88.99)
                .to(10001)
                .includeLower(true)
                .includeUpper(false);//包前不包后
        final SearchResponse response = client.prepareSearch("gamelog").setQuery(qu).get();
        System.out.println(response);
    }






    @Test
    public void testAddPlayer() throws IOException {
        //按照

        final IndexResponse response = client.prepareIndex("player_info", "player", "2")
                .setSource(
                        jsonBuilder().startObject()
                                .field("name", "James")
                                .field("age", 33)
                                .field("salary", 3000)
                                .field("team", "cav")
                                .field("postition", "sf")
                                .endObject()
                ).get();

    }

/* use play_info
select team , count(*) from  player group by team
 */
    @Test
    public void testAgg1(){

        SearchRequestBuilder builder  =   client.prepareSearch("player_info").setTypes("player");
       // 按team分组然后进行聚合，但并没有指定聚合函数
        final TermsAggregationBuilder teamAgg = AggregationBuilders.terms("player_count").field("team");
        //添加到聚合器
        builder.addAggregation(teamAgg);
        final SearchResponse response = builder.execute().actionGet();
        final Map<String, Aggregation> aggMap = response.getAggregations().getAsMap();
        final StringTerms terms = (StringTerms)aggMap.get("player_count");
        //一次迭代出分组聚合函数
        for(Terms.Bucket bucket : terms.getBuckets()){
            final String team = (String) bucket.getKey();
            //count，分组后一个组有多少数据
            final long count = bucket.getDocCount();
            System.out.println(team+" "+count);


        }


     /*       Iterator<Terms.Bucket> teamBucketIt = terms.getBuckets().iterator();
            while(teamBucketIt.hasNext()){
                Terms.Bucket bucket1 = teamBucketIt.next();
                String team1 = (String)bucket1.getKey();
                long count1 = bucket1.getDocCount();
                System.out.println(team1+" "+count1);
            }
*/


    }





}
