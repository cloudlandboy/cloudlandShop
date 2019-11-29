package cn.zzrfdsn.cloudlandShop.service;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemDesc;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;

/**
 * @author cloudlandboy
 */
public interface ItemService {

    /**
     * 根据ID查询商品
     *
     * @param id
     * @return
     */
    TbItem findItemById(Long id);

    /**
     * 根据ID查询商品描述
     *
     * @param id
     * @return
     */
    TbItemDesc findItemDescById(Long id);

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return
     */
    EasyUIDataGridResult<TbItem> findItemForPage(int page, int size);


    /**
     * 添加商品
     *
     * @param item
     * @param desc
     * @return
     */
    CloudlandShopResult addItem(TbItem item, String desc);

    /**
     * 根据商品商品id查询商品描述
     *
     * @param id
     * @return
     */
    CloudlandShopResult queryItemDesc(Long id);

    /**
     * 更新商品信息
     *
     * @param item
     * @param desc
     * @return
     */
    CloudlandShopResult updateItem(TbItem item, String desc);

    /**
     * 根据商品id批量删除商品
     *
     * @param ids
     * @return
     */
    CloudlandShopResult deleteItem(long[] ids);

    /**
     * 根据商品id批量下架商品
     *
     * @param ids
     * @return
     */
    CloudlandShopResult instockItem(long[] ids);

    /**
     * 根据商品id批量上架商品
     *
     * @param ids
     * @return
     */
    CloudlandShopResult reshelfItem(long[] ids);

    /**
     * 商品改变后发布消息
     *
     * @param id
     */
    void itemChangeSendMessage(long id);
}
