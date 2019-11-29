package cn.zzrfdsn.cloudlandShop.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbContent;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUIDataGridResult;
import cn.zzrfdsn.cloudlandshop.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author cloudlandboy
 * @Date 2019/9/29 下午2:32
 * @Since 1.0.0
 */

@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult list(long categoryId, @RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam(name="size",defaultValue = "20") Integer size){

        EasyUIDataGridResult data = contentService.findContentForPage(categoryId, page, size);

        return data;
    }

    @RequestMapping("/content/save")
    @ResponseBody
    public CloudlandShopResult save(TbContent content){
        CloudlandShopResult cloudlandShopResult = contentService.addContent(content);
        return cloudlandShopResult;
    }

    @RequestMapping("/content/update")
    @ResponseBody
    public CloudlandShopResult update(TbContent content){
        CloudlandShopResult cloudlandShopResult = contentService.updateContent(content);
        return cloudlandShopResult;
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public CloudlandShopResult delete(Long[] ids){
        CloudlandShopResult cloudlandShopResult = contentService.deleteContent(ids);
        return cloudlandShopResult;
    }
}