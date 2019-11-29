package cn.zzrfdsn.cloudlandShop;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @Author cloudlandboy
 * @Date 2019/10/16 下午8:04
 * @Since 1.0.0
 */

public class ShiroJTest {

    @Test
    public void testAdd() throws Exception {

        //创建连接
        HttpSolrClient solrClient = new HttpSolrClient.Builder("http://172.16.145.131:8080/solr/collection1").build();

        //创建文档
        SolrInputDocument document=new SolrInputDocument();
        document.setField("id",System.currentTimeMillis());
        document.setField("title","测试solrj");

        //把document添加到索引库
        solrClient.add(document);
        //提交
        solrClient.commit();
    }
}