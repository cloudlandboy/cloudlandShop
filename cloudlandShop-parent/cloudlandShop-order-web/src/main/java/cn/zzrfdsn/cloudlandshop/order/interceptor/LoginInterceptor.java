package cn.zzrfdsn.cloudlandshop.order.interceptor;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.CookieUtils;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandshop.cart.service.CartService;
import cn.zzrfdsn.cloudlandshop.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 下午6:42
 * @Since 1.0.0
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;

    @Value("${sso_login_url}")
    private String SSO_LOGIN_URL;
    @Value("${item_cart_cookie}")
    private String ITEM_CART_COOKIE;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request, "token");

        //判断是否登录
        if (StringUtils.isBlank(token)) {
            //重定向到登录页面,登录过后还要返回到结算页面
            response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl=" + request.getRequestURL());
            return false;
        }

        //判断登录是否过期
        CloudlandShopResult result = userService.getUserInfoByToken(token);
        if (result.getStatus() != 200) {
            //重定向到登录页面,登录过后还要返回到结算页面
            response.sendRedirect(SSO_LOGIN_URL + "?redirectUrl" + request.getRequestURL());
            return false;
        }
        TbUser user = (TbUser) result.getData();
        //如果之前用户没有登录，点击去结算后登录又被重定向回这里，此时的购物车数据是存在于cookie中的，需要判断cookie中是否有数据，然后进行合并
        String item_cart = CookieUtils.getCookieValue(request, "item_cart", true);
        if (StringUtils.isNotBlank(item_cart)) {
            result = cartService.merge(user.getId(), JsonUtils.jsonToList(item_cart, TbItem.class));
            if (result.getStatus() == 200) {
                //清除cookie
                CookieUtils.deleteCookie(request, response, ITEM_CART_COOKIE);
            }

        }
        //查询到用户信息，放行
        request.setAttribute("user", user);
        return true;
    }
}