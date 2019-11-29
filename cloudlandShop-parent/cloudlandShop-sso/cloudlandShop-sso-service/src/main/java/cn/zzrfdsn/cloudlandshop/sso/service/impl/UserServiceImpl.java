package cn.zzrfdsn.cloudlandshop.sso.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbUserMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbUser;
import cn.zzrfdsn.cloudlandShop.pojo.TbUserExample;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import cn.zzrfdsn.cloudlandshop.sso.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author cloudlandboy
 * @Date 2019/10/25 下午4:23
 * @Since 1.0.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public CloudlandShopResult registerCheck(String param, int type) {
        if (StringUtils.isBlank(param) || type == 0) {
            return CloudlandShopResult.build(400, "请求参数有误！");
        }

        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (type == 1) {
            criteria.andUsernameEqualTo(param);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(param);
        }

        List<TbUser> users = userMapper.selectByExample(example);

        boolean empty = CollectionUtils.isEmpty(users);

        return CloudlandShopResult.ok(empty);
    }

    @Override
    public CloudlandShopResult addUser(TbUser user) {
        //非空校验
        boolean anyBlank = StringUtils.isAnyBlank(user.getUsername(), user.getPassword(), user.getPhone());
        if (anyBlank) {
            return CloudlandShopResult.build(400, "请求参数有误！");
        }

        //用户，手机号唯一
        boolean uniqueUsername = (boolean) this.registerCheck(user.getUsername(), 1).getData();
        boolean uniquePhone = (boolean) this.registerCheck(user.getPhone(), 2).getData();

        if (!uniqueUsername) {
            return CloudlandShopResult.build(400, "用户名已经被占用！");
        } else if (!uniquePhone) {
            return CloudlandShopResult.build(400, "该手机号已经绑定其他账户！");
        }

        //校验数据通过，补充完整信息
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        //密码加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        //保存到数据库
        userMapper.insert(user);

        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult login(String username, String password) {

        //查询数据库
        TbUserExample example = new TbUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<TbUser> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)) {
            return CloudlandShopResult.build(400, "用户名或密码错误！");
        }
        ;

        //用户存在比对密码
        TbUser user = users.get(0);
        if (!user.getPassword().equals(DigestUtils.md5Hex(password))) {
            return CloudlandShopResult.build(400, "用户名或密码错误！");
        }

        //登录信息正确，生成token
        String token = UUID.randomUUID().toString();
        jedisClient.set("token:" + token, JsonUtils.objectToJson(user));
        //设置过期时间
        jedisClient.expire("token:" + token, 60 * 30);
        return CloudlandShopResult.ok(token);
    }

    @Override
    public CloudlandShopResult getUserInfoByToken(String token) {
        String data = jedisClient.get("token:" + token);

        if (data == null) {
            return CloudlandShopResult.build(400, "登录信息已经过期！");
        }

        //把json字符串转为pojo
        TbUser user = JsonUtils.jsonToPojo(data, TbUser.class);

        //需要重置token的过期时间。
        jedisClient.expire("token:" + token, 60 * 30);

        return CloudlandShopResult.ok(user);
    }
}