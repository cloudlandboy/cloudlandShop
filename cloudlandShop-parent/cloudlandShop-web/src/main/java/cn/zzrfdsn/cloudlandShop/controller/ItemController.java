package cn.zzrfdsn.cloudlandShop.controller;


import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.service.ItemService;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;
import cn.zzrfdsn.cloudlandshop.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemSearchService itemSearchService;

    @ResponseBody
    @RequestMapping("/item/{id}")
    public TbItem info(@PathVariable Long id) {
        TbItem item = itemService.findItemById(id);
        return item;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult<TbItem> list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "30") Integer rows) {
        EasyUIDataGridResult<TbItem> data = itemService.findItemForPage(page, rows);
        return data;
    }

    @RequestMapping("/item/save")
    @ResponseBody
    public CloudlandShopResult add(TbItem item, String desc) {
        try {
            CloudlandShopResult cloudlandShopResult = itemService.addItem(item, desc);

            //商品添加成功发送消息
            long id = (long) cloudlandShopResult.getData();
            itemService.itemChangeSendMessage(id);
            return cloudlandShopResult;
        } catch (Exception e) {
            return CloudlandShopResult.error();
        }
    }

    @ResponseBody
    @RequestMapping("/item/desc/{id}")
    public CloudlandShopResult desc(@PathVariable Long id) {
        return itemService.queryItemDesc(id);
    }

    @RequestMapping("/item/update")
    @ResponseBody
    public CloudlandShopResult update(TbItem item, String desc) {
        try {
            CloudlandShopResult cloudlandShopResult = itemService.updateItem(item, desc);
            return cloudlandShopResult;
        } catch (Exception e) {
            return CloudlandShopResult.error();
        }
    }

    @RequestMapping("/item/delete")
    @ResponseBody
    public CloudlandShopResult delete(long[] ids) {
        try {
            if (ids != null) {
                CloudlandShopResult cloudlandShopResult = itemService.deleteItem(ids);
                return cloudlandShopResult;
            } else {
                return CloudlandShopResult.build(403, "参数有误");
            }

        } catch (Exception e) {
            return CloudlandShopResult.error();
        }
    }

    /**
     * 下架商品
     *
     * @param ids
     * @return
     */
    @RequestMapping("/item/instock")
    @ResponseBody
    public CloudlandShopResult instock(long[] ids) {
        try {
            if (ids != null) {
                CloudlandShopResult cloudlandShopResult = itemService.instockItem(ids);
                return cloudlandShopResult;
            } else {
                return CloudlandShopResult.build(403, "参数有误");
            }

        } catch (Exception e) {
            return CloudlandShopResult.error();
        }
    }

    /**
     * 上架商品
     *
     * @param ids
     * @return
     */
    @RequestMapping("/item/reshelf")
    @ResponseBody
    public CloudlandShopResult reshelf(long[] ids) {
        try {
            if (ids != null) {
                CloudlandShopResult cloudlandShopResult = itemService.reshelfItem(ids);
                return cloudlandShopResult;
            } else {
                return CloudlandShopResult.build(403, "参数有误");
            }

        } catch (Exception e) {
            return CloudlandShopResult.error();
        }
    }

    @RequestMapping("/item/importIndex")
    @ResponseBody
    public CloudlandShopResult importIndex() {
        return itemSearchService.importIndex();
    }
}
