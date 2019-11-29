package cn.zzrfdsn.cloudlandshop.search.service;

import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.ItemSearchData;

/**
 * @Author cloudlandboy
 * @Date 2019/10/17 上午9:35
 * @Since 1.0.0
 */

public interface ItemSearchService {

    /**
     * 导入商品列表索引
     *
     * @return
     */
    CloudlandShopResult importIndex();

    /**
     * 搜索商品
     * @param keyword
     * @param page
     * @param rows
     * @return
     */
    ItemSearchData search(String keyword, int page, int rows) throws Exception;
}