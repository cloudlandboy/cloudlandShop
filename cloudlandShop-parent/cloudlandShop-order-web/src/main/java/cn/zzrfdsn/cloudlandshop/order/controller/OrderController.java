package cn.zzrfdsn.cloudlandshop.order.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandshop.cart.service.CartService;
import cn.zzrfdsn.cloudlandshop.order.service.OrderService;
import cn.zzrfdsn.cloudlandshop.order.vo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 下午6:32
 * @Since 1.0.0
 */
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String orderCart(HttpServletRequest request, Model model) {
        //能够进入这里肯定是已经登录的
        TbUser user = (TbUser) request.getAttribute("user");

        List<TbItem> itemCart = cartService.getItemCart(user.getId());

        model.addAttribute("cartList", itemCart);

        return "order-cart";
    }

    @RequestMapping("/order/create")
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request, Model model) {
        TbUser user = (TbUser) request.getAttribute("user");
        CloudlandShopResult result = orderService.createOrder(orderInfo, user);

        //清空购物车
        cartService.clearCart(user.getId());

        model.addAttribute("orderId", result.getData());
        model.addAttribute("payment", orderInfo.getPayment());
        return "success";
    }
}