package cn.zzrfdsn.cloudlandShop.protal.controller;

import cn.zzrfdsn.cloudlandShop.pojo.TbContent;
import cn.zzrfdsn.cloudlandshop.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/28 上午11:06
 * @Since 1.0.0
 */

@Controller
public class PageController {

    /**
     * 轮播图id
     */
    @Value("${content_slideshow_id}")
    private long slideshowId;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String index(Model model){
        List<TbContent> slideshow = contentService.findContentByCid(slideshowId);
        model.addAttribute("ad1List",slideshow);
        return "index";
    }

    @RequestMapping("/{page}")
    public String index(@PathVariable  String page){
        return page;
    }
}