package cn.zzrfdsn.cloudlandShop.item.controller;

import cn.zzrfdsn.cloudlandShop.item.vo.ItemInfo;
import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemDesc;
import cn.zzrfdsn.cloudlandShop.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午8:12
 * @Since 1.0.0
 */

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String itemInfo(@PathVariable long itemId, Model model) throws Exception {
        TbItem item = itemService.findItemById(itemId);
        TbItemDesc itemDesc = itemService.findItemDescById(itemId);

        ItemInfo itemInfo = new ItemInfo();
        BeanUtils.copyProperties(item, itemInfo);

        model.addAttribute("item", itemInfo);
        model.addAttribute("itemDesc", itemDesc);

        return "item";
    }
}