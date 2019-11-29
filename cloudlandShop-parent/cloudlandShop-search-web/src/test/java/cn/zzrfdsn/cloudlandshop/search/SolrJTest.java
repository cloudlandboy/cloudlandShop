package cn.zzrfdsn.cloudlandshop.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/10/17 下午3:56
 * @Since 1.0.0
 */

public class SolrJTest {

    /**
     * 测试索引查询
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

       //创建一个SolrClient对象
        SolrClient solrClient = new HttpSolrClient.Builder("http://172.16.145.131:8080/solr/collection1").build();
        //创建一个查询对象，可以参考solr的后台的查询功能设置条件
        SolrQuery query = new SolrQuery();
        //设置查询条件
        //query.setQuery("*:*");
        query.set("q", "item_title:华为");
        //设置分页条件
        query.setStart(0);
        query.setRows(5);
        //开启高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        //设置默认搜索域
        query.set("df","item_title");
        //执行查询，得到一个QueryResponse对象。
        QueryResponse queryResponse = solrClient.query(query);
        //获取查询结果
        SolrDocumentList results = queryResponse.getResults();
        //获取高亮的结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        //总记录数
        long total = results.getNumFound();
        System.out.println("总记录数："+total);
        //取查询结果
        for (SolrDocument document : results) {
            String id= (String) document.get("id");
            //获取高亮的title数据
            Map<String, List<String>> stringListMap = highlighting.get(id);
            List<String> item_title = stringListMap.get("item_title");
            String title;
            if (CollectionUtils.isEmpty(item_title)){
                title= (String) document.getFieldValue("item_title");
            }else{
                title=item_title.get(0);
            }
            System.out.println("id："+id);
            System.out.println("标题："+title);
            System.out.println("卖点："+document.get("item_sell_point"));
            System.out.println("价格："+document.get("item_price"));
            System.out.println("图片："+document.get("item_image"));
            System.out.println("分类："+document.get("item_category_name"));

            System.out.println("-----------------------------------------------");
        }
    }
}