package cn.zzrfdsn.cloudlandshop.cart.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbItemMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import cn.zzrfdsn.cloudlandshop.cart.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 上午11:23
 * @Since 1.0.0
 */

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper itemMapper;

    @Value("${item_cart_key_pre}")
    private String ITEM_CART_KEY_PRE;

    @Override
    public CloudlandShopResult addCart(long userId, long itemId, int num) {

        //判断redis中该用户的购物车是否已经存在要添加的商品
        String itemInfo = jedisClient.hget(ITEM_CART_KEY_PRE + ":" + userId, itemId + "");
        TbItem item = null;
        //购物车中已经存在该商品
        if (itemInfo != null) {
            item = JsonUtils.jsonToPojo(itemInfo, TbItem.class);
            item.setNum(item.getNum() + num);
        } else {
            //购物车中没有该商品，查询数据库然后添加
            item = itemMapper.selectByPrimaryKey(itemId);
            //设置购买数量
            item.setNum(num);
            //只用取出一张图片即可
            String images = item.getImage();
            if (StringUtils.isNotBlank(images)) {
                item.setImage(images.split(",")[0]);
            }
        }
        //添加到购物车
        jedisClient.hset(ITEM_CART_KEY_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult merge(Long userId, List<TbItem> itemCart) {
        //调用添加方法即可
        for (TbItem item : itemCart) {
            this.addCart(userId, item.getId(), item.getNum());
        }
        return CloudlandShopResult.ok();
    }

    @Override
    public List<TbItem> getItemCart(Long userId) {

        List<String> result = jedisClient.hvals(ITEM_CART_KEY_PRE + ":" + userId);

        List<TbItem> itemList = new ArrayList<>();
        for (String item : result) {
            itemList.add(JsonUtils.jsonToPojo(item, TbItem.class));
        }

        return itemList;
    }

    @Override
    public CloudlandShopResult changeNum(Long userId, Long itemId, Integer num) {
        String itemInfo = jedisClient.hget(ITEM_CART_KEY_PRE + ":" + userId, itemId + "");
        if (StringUtils.isNotBlank(itemInfo)) {
            TbItem item = JsonUtils.jsonToPojo(itemInfo, TbItem.class);
            item.setNum(num);
            jedisClient.hset(ITEM_CART_KEY_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
        }

        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult deleteItemCart(Long userId, Long itemId) {
        jedisClient.hdel(ITEM_CART_KEY_PRE + ":" + userId, itemId + "");
        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult clearCart(Long userId) {
        jedisClient.del(ITEM_CART_KEY_PRE + ":" + userId);
        return CloudlandShopResult.ok();
    }
}