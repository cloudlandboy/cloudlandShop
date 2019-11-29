package cn.zzrfdsn.cloudlandShop.content.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbContentMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbContent;
import cn.zzrfdsn.cloudlandShop.pojo.TbContentExample;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.util.JsonUtils;
import cn.zzrfdsn.cloudlandShop.util.jedis.JedisClient;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;
import cn.zzrfdsn.cloudlandshop.content.service.ContentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/29 下午2:22
 * @Since 1.0.0
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ContentServiceImpl implements ContentService {

    @Value("${CONTENT_LIST_KEY}")
    private String CONTENT_LIST_KEY;

    @Autowired
    private TbContentMapper contentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Override
    @Transactional(readOnly = true)
    public EasyUIDataGridResult findContentForPage(long cid, int page, int size) {
        PageHelper.startPage(page,size);

        TbContentExample contentExample=new TbContentExample();
        contentExample.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(contentExample);

        PageInfo pageInfo=new PageInfo(tbContents);

        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setRows(tbContents);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public CloudlandShopResult addContent(TbContent content) {

        //删除缓存
        jedisClient.hdel(CONTENT_LIST_KEY,content.getCategoryId()+"");

        Date date=new Date();
        content.setCreated(date);
        content.setUpdated(date);
        contentMapper.insert(content);
        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult updateContent(TbContent content) {
        //删除缓存
        Long cid = contentMapper.selectByPrimaryKey(content.getId()).getCategoryId();
        jedisClient.hdel(CONTENT_LIST_KEY,cid+"");

        content.setUpdated(new Date());
        contentMapper.updateByPrimaryKeySelective(content);
        return CloudlandShopResult.ok();
    }


    @Override
    public List<TbContent> findContentByCid(long cid) {
        //获取缓存数据
        try {
            String data = jedisClient.hget(CONTENT_LIST_KEY,cid+"");
            if (StringUtils.isNotBlank(data)){
                return JsonUtils.jsonToList(data,TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //获取失败查询数据库
        TbContentExample contentExample=new TbContentExample();
        contentExample.createCriteria().andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExample(contentExample);
        //把查询到的结果放到缓存中
        try {
            jedisClient.hset(CONTENT_LIST_KEY,cid+"",JsonUtils.objectToJson(tbContents));
        }catch (Exception e){
            e.printStackTrace();
        }
        return tbContents;
    }

    @Override
    public CloudlandShopResult deleteContent(Long[] ids) {
        if (ids!=null&&ids.length>0){
            //删除缓存
            Long cid = contentMapper.selectByPrimaryKey(ids[0]).getCategoryId();
            jedisClient.hdel(CONTENT_LIST_KEY,cid+"");
        }
        TbContentExample contentExample=new TbContentExample();
        contentExample.createCriteria().andIdIn(Arrays.asList(ids));
        contentMapper.deleteByExample(contentExample);
        return CloudlandShopResult.ok();
    }
}