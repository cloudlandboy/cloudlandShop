package cn.zzrfdsn.cloudlandshop.cart.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.service.ItemService;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.CookieUtils;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandshop.cart.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/26 下午6:55
 * @Since 1.0.0
 */

@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @Value("${item_cart_cookie}")
    private String ITEM_CART_COOKIE;
    @Value("${item_cart_cookie_expire}")
    private int ITEM_CART_COOKIE_EXPIRE;

    @RequestMapping("/cart/add/{itemId}")
    public String joinCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断是否是登录状态
        TbUser user = (TbUser) request.getAttribute("user");

        if (user != null) {
            //已经登录,调用服务将购物车添加到redis中
            cartService.addCart(user.getId(), itemId, num);
            //返回添加成功页面
            return "cartSuccess";
        }


        //从cookie中查询商品列表
        List<TbItem> items = getItemCartListFromCookie(request);

        //定义一个标识
        boolean exist = false;

        //遍历商品，查询购物车是否已经存在该商品
        for (TbItem item : items) {
            if (item.getId() == itemId.longValue()) {
                //用库存数量字段代替购物车数量，存在则对数量相加
                item.setNum(item.getNum() + num);
                //跳出循环
                exist = true;
                break;
            }
        }

        //循环完毕，判断是否存在,不存在则查询数据库
        if (!exist) {
            //调用manger的ItemService服务
            TbItem item = itemService.findItemById(itemId);
            item.setNum(num);
            //只用取出一张图片即可
            String images = item.getImage();
            if (StringUtils.isNotBlank(images)) {
                item.setImage(images.split(",")[0]);
            }
            //添加到购物车列表
            items.add(item);
        }

        //把购车商品列表写入cookie。
        CookieUtils.setCookie(request, response, ITEM_CART_COOKIE, JsonUtils.objectToJson(items), ITEM_CART_COOKIE_EXPIRE, true);
        //返回添加成功页面
        return "cartSuccess";
    }


    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request, Model model, HttpServletResponse response) {
        //从cookie中查询
        List<TbItem> itemCart = getItemCartListFromCookie(request);

        //判断是否是登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            //判断cookie中的商品是否为空，不为空则合并
            if (!CollectionUtils.isEmpty(itemCart)) {
                cartService.merge(user.getId(), itemCart);
                //合并后删除cookie
                CookieUtils.deleteCookie(request, response, ITEM_CART_COOKIE);
            }
            //然后从redis中获取购物车列表
            itemCart = cartService.getItemCart(user.getId());

        }

        //放到model中转发
        model.addAttribute("cartList", itemCart);

        return "cart";
    }

    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public CloudlandShopResult updateCart(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response) {
        //判断是否是登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.changeNum(user.getId(), itemId, num);
        } else {
            //从cookie中查询商品列表
            List<TbItem> items = getItemCartListFromCookie(request);
            //遍历商品，查询该商品
            for (TbItem item : items) {
                if (item.getId() == itemId.longValue()) {
                    //用库存数量字段代替购物车数量，存在则对数量相加
                    item.setNum(num);
                    //跳出循环
                    break;
                }
            }
            //把购车商品列表写入cookie。
            CookieUtils.setCookie(request, response, ITEM_CART_COOKIE, JsonUtils.objectToJson(items), ITEM_CART_COOKIE_EXPIRE, true);
        }

        return CloudlandShopResult.ok();
    }

    @RequestMapping("/cart/delete/{itemId}")
    public String deleteItemCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
        //判断是否是登录状态
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null) {
            cartService.deleteItemCart(user.getId(), itemId);
        } else {
            //从cookie中查询商品列表
            List<TbItem> items = getItemCartListFromCookie(request);
            //遍历商品，查询该商品
            for (TbItem item : items) {
                if (item.getId() == itemId.longValue()) {
                    //从列表中删除该商品
                    items.remove(item);
                    //跳出循环
                    break;
                }
            }
            //把购车商品列表写入cookie。
            CookieUtils.setCookie(request, response, ITEM_CART_COOKIE, JsonUtils.objectToJson(items), ITEM_CART_COOKIE_EXPIRE, true);
        }

        //重定向到购物车列表页面
        return "redirect:/cart/cart.html";
    }


    /**
     * 从cookie中获取购物车列表
     *
     * @return
     */
    private List<TbItem> getItemCartListFromCookie(HttpServletRequest request) {

        String item_cart = CookieUtils.getCookieValue(request, "item_cart", true);

        //判断是否存在购物车列表
        if (StringUtils.isNotBlank(item_cart)) {
            //存在，把json格式数据转换为pojo，需要添加对pojo的依赖
            return JsonUtils.jsonToList(item_cart, TbItem.class);
        }

        return new ArrayList<>();
    }
}