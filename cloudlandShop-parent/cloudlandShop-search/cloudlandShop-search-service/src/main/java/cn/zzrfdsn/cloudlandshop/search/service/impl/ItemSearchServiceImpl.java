package cn.zzrfdsn.cloudlandshop.search.service.impl;

import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.ItemSearchData;
import cn.zzrfdsn.cloudlandShop.vo.ItemSearchResult;
import cn.zzrfdsn.cloudlandshop.search.mapper.ItemMapper;
import cn.zzrfdsn.cloudlandshop.search.service.ItemSearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author cloudlandboy
 * @Date 2019/10/17 上午9:58
 * @Since 1.0.0
 */

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public CloudlandShopResult importIndex() {

        try {

            //重数据库查询索引
            List<ItemSearchResult> itemList = itemMapper.getItemList();


            if (!CollectionUtils.isEmpty(itemList)) {
                for (ItemSearchResult item : itemList) {
                    SolrInputDocument document = new SolrInputDocument();
                    document.addField("id", item.getId());
                    document.addField("item_title", item.getTitle());
                    document.addField("item_sell_point", item.getSell_point());
                    document.addField("item_price", item.getPrice());
                    document.addField("item_image", item.getImage());
                    document.addField("item_category_name", item.getCategory_name());
                    solrClient.add(document);
                }
                //循环添加完毕，提交
                solrClient.commit();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return CloudlandShopResult.error();
        }

        return CloudlandShopResult.ok();
    }

    @Override
    public ItemSearchData search(String keyword, int page, int rows) throws Exception {
        //创建一个查询对象，可以参考solr的后台的查询功能设置条件
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.set("q", "item_title:" + keyword);
        //设置分页条件
        int start = (page - 1) * rows;
        query.setStart(start);
        query.setRows(rows);
        //开启高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em class=\"highlight\">");
        query.setHighlightSimplePost("</em>");

        //设置默认搜索域
        query.set("df", "item_title");
        //执行查询，得到一个QueryResponse对象。
        QueryResponse queryResponse = solrClient.query(query);
        //获取查询结果
        SolrDocumentList results = queryResponse.getResults();
        //获取高亮的结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        ItemSearchData itemSearchData = new ItemSearchData();
        //总记录数
        long total = results.getNumFound();

        itemSearchData.setPage(page);
        itemSearchData.setTotalPages((int) ((total + rows - 1) / rows));
        itemSearchData.setRecourdCount((int) total);
        itemSearchData.setQuery(keyword);


        //取查询结果
        List<ItemSearchResult> itemList = new ArrayList<>();
        for (SolrDocument document : results) {

            ItemSearchResult itemSearchResult = new ItemSearchResult();
            String id = (String) document.get("id");
            //获取高亮的title数据
            Map<String, List<String>> highlightingData = highlighting.get(id);
            List<String> item_title = highlightingData.get("item_title");


            String title;
            if (CollectionUtils.isEmpty(item_title)) {
                title = (String) document.get("item_title");
            } else {
                title = item_title.get(0);
            }
            itemSearchResult.setId(id);
            itemSearchResult.setCategory_name((String) document.get("item_category_name"));
            itemSearchResult.setTitle(title);
            itemSearchResult.setSell_point((String) document.get("item_sell_point"));
            itemSearchResult.setImage((String) document.get("item_image"));
            Object item_price = document.get("item_price");
            itemSearchResult.setPrice((long) item_price);

            itemList.add(itemSearchResult);
        }

        itemSearchData.setItemList(itemList);

        return itemSearchData;
    }
}