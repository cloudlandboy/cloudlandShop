package cn.zzrfdsn.cloudlandshop.order.vo;

import cn.zzrfdsn.cloudlandShop.pojo.TbOrder;
import cn.zzrfdsn.cloudlandShop.pojo.TbOrderItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 下午8:45
 * @Since 1.0.0
 */

public class OrderInfo extends TbOrder implements Serializable {

    private List<TbOrderItem> orderItems;

    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}