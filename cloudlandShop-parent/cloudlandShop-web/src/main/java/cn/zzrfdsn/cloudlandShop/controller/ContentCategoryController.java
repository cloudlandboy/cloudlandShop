package cn.zzrfdsn.cloudlandShop.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbContentCategory;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;
import cn.zzrfdsn.cloudlandshop.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/28 下午5:57
 * @Since 1.0.0
 */

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUiTreeData> list(@RequestParam(name = "id", defaultValue = "0") long pid) {
        List<EasyUiTreeData> data = contentCategoryService.findCategoryForTree(pid);
        return data;
    }

    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public CloudlandShopResult create(TbContentCategory contentCategory) {
        try {
            CloudlandShopResult data = contentCategoryService.addContentCategoty(contentCategory);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return CloudlandShopResult.error();
        }
    }

    @RequestMapping(value = "/content/category/update", method = RequestMethod.POST)
    @ResponseBody
    public CloudlandShopResult update(TbContentCategory contentCategory) {
        try {
            CloudlandShopResult data = contentCategoryService.updateContentCategoty(contentCategory);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return CloudlandShopResult.error();
        }
    }

    @RequestMapping(value = "/content/category/delete", method = RequestMethod.POST)
    @ResponseBody
    public CloudlandShopResult delete(TbContentCategory contentCategory) {
        try {
            CloudlandShopResult data = contentCategoryService.deleteContentCategoty(contentCategory);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return CloudlandShopResult.error();
        }
    }
}