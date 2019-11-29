package cn.zzrfdsn.cloudlandshop.order.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandshop.order.vo.OrderInfo;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 下午8:49
 * @Since 1.0.0
 */

public interface OrderService {

    /**
     * 生成订单
     *
     * @param orderInfo
     * @param user
     * @return
     */
    CloudlandShopResult createOrder(OrderInfo orderInfo, TbUser user);
}