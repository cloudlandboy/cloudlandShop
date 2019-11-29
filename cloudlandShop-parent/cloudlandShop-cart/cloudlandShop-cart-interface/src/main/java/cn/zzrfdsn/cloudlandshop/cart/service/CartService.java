package cn.zzrfdsn.cloudlandshop.cart.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 上午11:20
 * @Since 1.0.0
 */

public interface CartService {

    /**
     * 添加商品到购物车
     *
     * @param userId
     * @param itemId
     * @param item
     * @return
     */
    CloudlandShopResult addCart(long userId, long itemId, int item);

    /**
     * 将cookie和redis购物车合并
     *
     * @param userId
     * @param itemCart
     */
    CloudlandShopResult merge(Long userId, List<TbItem> itemCart);

    /**
     * 获取购物车
     *
     * @param userId
     */
    List<TbItem> getItemCart(Long userId);

    /**
     * 修改商品数量
     *
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    CloudlandShopResult changeNum(Long userId, Long itemId, Integer num);

    /**
     * 删除购物车商品
     *
     * @param userId
     * @param itemId
     */
    CloudlandShopResult deleteItemCart(Long userId, Long itemId);

    /**
     * 清空购物车
     *
     * @param userId
     * @return
     */
    CloudlandShopResult clearCart(Long userId);
}