package cn.zzrfdsn.cloudlandshop.order.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbOrderItemMapper;
import cn.zzrfdsn.cloudlandShop.mapper.TbOrderMapper;
import cn.zzrfdsn.cloudlandShop.mapper.TbOrderShippingMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbOrderItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbOrderShipping;
import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import cn.zzrfdsn.cloudlandshop.order.service.OrderService;
import cn.zzrfdsn.cloudlandshop.order.vo.OrderInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 下午8:58
 * @Since 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Value("${orderid_key}")
    private String ORDERID_KEY;

    @Override
    public CloudlandShopResult createOrder(OrderInfo orderInfo, TbUser user) {
        Date date = new Date();
        //生成唯一Id，当前时间加上redis的自增
        String orderId = DateFormatUtils.format(date, "yyyyMMddHHmmss") + jedisClient.incr(ORDERID_KEY);
        orderInfo.setOrderId(orderId);
        //设置状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        //订单创建时间
        orderInfo.setCreateTime(date);
        //订单更新时间
        orderInfo.setUpdateTime(date);
        //用户id
        orderInfo.setUserId(user.getId());
        //保存到数据库
        orderMapper.insert(orderInfo);

        //完善订单项信息
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(UUID.randomUUID().toString().replace(",", ""));
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        //完善订单物流信息
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setCreated(date);
        orderShipping.setOrderId(orderId);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        return CloudlandShopResult.ok(orderId);
    }
}