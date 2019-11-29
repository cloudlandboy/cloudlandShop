package cn.zzrfdsn.cloudlanshop.sso.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.CookieUtils;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandshop.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author cloudlandboy
 * @Date 2019/10/25 下午3:41
 * @Since 1.0.0
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/login")
    public String toLogin(String redirectUrl, Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "login";
    }

    @RequestMapping("/user/register")
    public String toRegister() {
        return "register";
    }


    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public CloudlandShopResult registerCheck(@PathVariable String param, @PathVariable Integer type) {

        return userService.registerCheck(param, type);
    }

    @RequestMapping(value = "/user/registerService", method = RequestMethod.POST)
    @ResponseBody
    public CloudlandShopResult register(TbUser user) {
        CloudlandShopResult cloudlandShopResult = userService.addUser(user);
        return cloudlandShopResult;
    }

    @RequestMapping(value = "/user/loginService", method = RequestMethod.POST)
    @ResponseBody
    public CloudlandShopResult login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        CloudlandShopResult cloudlandShopResult = userService.login(username, password);

        if (cloudlandShopResult.getStatus() == 200) {
            //把token写进cookie
            CookieUtils.setCookie(request, response, "token", (String) cloudlandShopResult.getData());
        }
        return cloudlandShopResult;
    }

    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String userInfoByToken(@PathVariable String token, String callback) {

        CloudlandShopResult result = userService.getUserInfoByToken(token);

        if (StringUtils.isNotBlank(callback)) {
            return callback + "(" + JsonUtils.objectToJson(result) + ")";
        }

        return JsonUtils.objectToJson(result);
    }
}