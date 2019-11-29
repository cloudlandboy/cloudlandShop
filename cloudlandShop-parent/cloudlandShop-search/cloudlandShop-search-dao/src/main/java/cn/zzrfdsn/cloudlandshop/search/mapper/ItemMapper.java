package cn.zzrfdsn.cloudlandshop.search.mapper;

import cn.zzrfdsn.cloudlandShop.vo.ItemSearchResult;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/16 下午8:35
 * @Since 1.0.0
 */

public interface ItemMapper {

    /**
     * 查询所有可用商品
     *
     * @return
     */
    List<ItemSearchResult> getItemList();

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    ItemSearchResult getItemById(long id);
}