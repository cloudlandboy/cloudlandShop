package cn.zzrfdsn.cloudlandShop.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbItemDescMapper;
import cn.zzrfdsn.cloudlandShop.mapper.TbItemMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemDesc;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemExample;
import cn.zzrfdsn.cloudlandShop.service.ItemService;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.IDUtils;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * @author cloudlandboy
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ItemServiceImpl implements ItemService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name = "topicDestination")
    private Destination destination;

    @Autowired
    private JedisClient jedisClient;

    @Override
    @Transactional(readOnly = true)
    public TbItem findItemById(Long id) {

        String key = "ITEM_INFO_PRE" + ":" + id + ":BASE";

        //先查询缓存
        try {
            String data = jedisClient.get(key);
            if (StringUtils.isNotBlank(data)) {
                return JsonUtils.jsonToPojo(data, TbItem.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //没有缓存查询数据库
        TbItem item = itemMapper.selectByPrimaryKey(id);

        try {
            //将数据添加到缓存
            jedisClient.set(key, JsonUtils.objectToJson(item));
            //设置过期时间，1小时
            jedisClient.expire(key, 60 * 60);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public TbItemDesc findItemDescById(Long id) {
        String key = "ITEM_INFO_PRE" + ":" + id + ":DESC";

        //先查询缓存
        try {
            String data = jedisClient.get(key);
            if (StringUtils.isNotBlank(data)) {
                return JsonUtils.jsonToPojo(data, TbItemDesc.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        //没有缓存查询数据库
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);

        try {
            //将数据添加到缓存
            jedisClient.set(key, JsonUtils.objectToJson(itemDesc));
            //设置过期时间，1小时
            jedisClient.expire(key, 60 * 60);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return itemDesc;
    }

    @Override
    public EasyUIDataGridResult<TbItem> findItemForPage(int page, int size) {
        PageHelper.startPage(page, size);
        TbItemExample itemExample = new TbItemExample();
        List<TbItem> items = itemMapper.selectByExample(itemExample);
        PageInfo<TbItem> pageInfo = new PageInfo<>(items);

        EasyUIDataGridResult<TbItem> result = new EasyUIDataGridResult<TbItem>();

        result.setTotal(pageInfo.getTotal());
        result.setRows(items);
        return result;
    }

    @Override
    public CloudlandShopResult addItem(TbItem item, String desc) {
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);

        //生成时间戳+两位随机数Id
        long id = IDUtils.genItemId();
        item.setId(id);
        itemDesc.setItemId(id);
        //设置商品状态  商品状态，1-正常，2-下架，3-删除
        item.setStatus((byte) 1);

        //获取当前时间
        Date date = new Date();
        //创建时间
        item.setCreated(date);
        itemDesc.setCreated(date);
        //更新时间
        item.setUpdated(date);
        itemDesc.setUpdated(date);

        itemMapper.insert(item);
        itemDescMapper.insert(itemDesc);

        //把id返回
        return CloudlandShopResult.ok(id);
    }

    @Override
    public CloudlandShopResult queryItemDesc(Long id) {
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(id);
        return CloudlandShopResult.ok(itemDesc.getItemDesc());
    }

    @Override
    public CloudlandShopResult updateItem(TbItem item, String desc) {
        TbItem oldItem = itemMapper.selectByPrimaryKey(item.getId());
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(item.getId());
        item.setCreated(oldItem.getCreated());

        Date updateDate = new Date();
        item.setUpdated(updateDate);
        item.setStatus(oldItem.getStatus());

        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(updateDate);

        itemMapper.updateByPrimaryKey(item);
        //注意 WithBLOBs，text类型要用这个方法
        itemDescMapper.updateByPrimaryKeyWithBLOBs(itemDesc);

        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult deleteItem(long[] ids) {
        return this.updateStatus(ids, 3);
    }

    @Override
    public CloudlandShopResult instockItem(long[] ids) {
        return this.updateStatus(ids, 2);
    }

    @Override
    public CloudlandShopResult reshelfItem(long[] ids) {
        return this.updateStatus(ids, 1);
    }

    @Override
    public void itemChangeSendMessage(long id) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(id + "");
                return textMessage;
            }
        });
    }

    private CloudlandShopResult updateStatus(long[] ids, int status) {
        for (long id : ids) {
            TbItem item = new TbItem();
            item.setId(id);
            //设置商品状态  商品状态，1-正常，2-下架，3-删除
            item.setStatus((byte) status);
            itemMapper.updateStatus(item);
        }
        return CloudlandShopResult.ok();
    }
}
