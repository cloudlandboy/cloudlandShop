package cn.zzrfdsn.cloudlandshop;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/10/18 下午5:46
 * @Since 1.0.0
 */

public class SolrcloudTest {
    @Test
    public void testAdd() throws Exception {

        //创建一个SolrClient对象使用子类CloudSolrClient，SolrURL应指向Solr根路径
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SolrClient solrClient = (SolrClient) context.getBean("solrClient");

        System.out.println(solrClient.ping());
        SolrInputDocument document = new SolrInputDocument();

        String id = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        document.setField("id", id);
        document.setField("title", "测试solrj连接集群");

        solrClient.add(document);
        solrClient.commit();
        solrClient.close();
    }

    @Test
    public void testQuery() throws Exception {
        //创建一个SolrClient对象使用子类CloudSolrClient，SolrURL应指向Solr根路径
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SolrClient solrClient = (SolrClient) context.getBean("solrClient");

        SolrQuery query = new SolrQuery();

        query.setQuery("title:solrj");
        query.setHighlight(true);
        query.addHighlightField("title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");


        QueryResponse response = solrClient.query(query);
        SolrDocumentList results = response.getResults();
        System.out.println("总记录：" + results.getNumFound());

        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("title"));
            System.out.println(highlighting.get(result.get("id")));
        }

        solrClient.close();
    }
}