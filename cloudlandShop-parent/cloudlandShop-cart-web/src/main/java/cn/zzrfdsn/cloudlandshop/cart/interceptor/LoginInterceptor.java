package cn.zzrfdsn.cloudlandshop.cart.interceptor;

import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.CookieUtils;
import cn.zzrfdsn.cloudlandshop.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author cloudlandboy
 * @Date 2019/10/27 上午10:37
 * @Since 1.0.0
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //尝试从cookie中获取token
        String token = CookieUtils.getCookieValue(request, "token");

        //没有token，说明没有登录，直接放行
        if (StringUtils.isBlank(token)) {
            return true;
        }
        //有token，判断token是否过期，引用单点登录系统的服务
        CloudlandShopResult result = userService.getUserInfoByToken(token);
        //不是200，说明cookie已经过期，直接放行
        if (result.getStatus() != 200) {
            return true;
        }

        //获取用户信息放入request域中,在添加购物车时可判断user是否存在，存在操作redis，不存在操作cookie
        request.setAttribute("user", result.getData());

        return true;
    }
}