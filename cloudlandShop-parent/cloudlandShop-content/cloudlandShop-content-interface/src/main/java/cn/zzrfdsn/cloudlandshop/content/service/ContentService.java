package cn.zzrfdsn.cloudlandshop.content.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbContent;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/29 下午2:18
 * @Since 1.0.0
 */

public interface ContentService {

    /**
     * 根据分类id查询gaifenlei下的内容返回easyui分页数据
     *
     * @param cid
     * @param page
     * @param size
     * @return
     */
    EasyUIDataGridResult findContentForPage(long cid, int page, int size);

    /**
     * 添加内容
     *
     * @param content
     * @return
     */
    CloudlandShopResult addContent(TbContent content);

    /**
     * 修改内容
     *
     * @param content
     * @return
     */
    CloudlandShopResult updateContent(TbContent content);

    /**
     * 删除内容
     *
     * @param ids
     * @return
     */
    CloudlandShopResult deleteContent(Long[] ids);

    /**
     * 根据分类id查询对应内容列表
     *
     * @param cid
     * @return
     */
    List<TbContent> findContentByCid(long cid);
}