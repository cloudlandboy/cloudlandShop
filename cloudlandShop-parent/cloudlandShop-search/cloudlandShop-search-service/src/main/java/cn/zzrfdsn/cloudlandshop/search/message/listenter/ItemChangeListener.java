package cn.zzrfdsn.cloudlandshop.search.message.listenter;

import cn.zzrfdsn.cloudlandShop.vo.ItemSearchResult;
import cn.zzrfdsn.cloudlandshop.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午4:00
 * @Since 1.0.0
 */

public class ItemChangeListener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrClient solrClient;

    @Override
    public void onMessage(Message message) {

        logger.debug("接收改变商品的到消息");

        try {
            TextMessage textMessage = (TextMessage) message;
            long id = Long.parseLong(textMessage.getText());

            //根据id查询数据库
            ItemSearchResult item = itemMapper.getItemById(id);
            //添加索引
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", item.getId());
            document.addField("item_title", item.getTitle());
            document.addField("item_sell_point", item.getSell_point());
            document.addField("item_price", item.getPrice());
            document.addField("item_image", item.getImage());
            document.addField("item_category_name", item.getCategory_name());
            solrClient.add(document);
            //循环添加完毕，提交
            solrClient.commit();
        } catch (Exception e) {
            logger.error("添加商品同步索引出错：", e);
        }
    }
}