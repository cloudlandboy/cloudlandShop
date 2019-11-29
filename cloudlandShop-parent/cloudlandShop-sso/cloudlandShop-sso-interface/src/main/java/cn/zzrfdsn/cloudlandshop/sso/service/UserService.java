package cn.zzrfdsn.cloudlandshop.sso.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;

/**
 * @Author cloudlandboy
 * @Date 2019/10/25 下午4:09
 * @Since 1.0.0
 */

public interface UserService {

    /**
     *
     * @param param 手机号/账户名/邮箱的值
     * @param type  对应手机号/账户名/邮箱
     * @return
     */
    CloudlandShopResult registerCheck(String param,int type);

    /**
     * 添加用户
     * @param user
     * @return
     */
    CloudlandShopResult addUser(TbUser user);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    CloudlandShopResult login(String username,String password);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    CloudlandShopResult getUserInfoByToken(String token);
}