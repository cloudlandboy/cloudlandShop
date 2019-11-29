package cn.zzrfdsn.cloudlandshop.content.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbContentCategory;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/28 下午5:43
 * @Since 1.0.0
 */

public interface ContentCategoryService {

    /**
     * 查询首页内容分类
     *
     * @param pid 父节点id
     * @return EasyUiTree需要的数据格式
     */
    List<EasyUiTreeData> findCategoryForTree(Long pid);

    /**
     * 添加内容分类
     *
     * @param contentCategory
     * @return
     */
    CloudlandShopResult addContentCategoty(TbContentCategory contentCategory);

    /**
     * 修改内容分类
     *
     * @param contentCategory
     * @return
     */
    CloudlandShopResult updateContentCategoty(TbContentCategory contentCategory);

    /**
     * 删除内容分类
     *
     * @param contentCategory
     * @return
     */
    CloudlandShopResult deleteContentCategoty(TbContentCategory contentCategory);
}