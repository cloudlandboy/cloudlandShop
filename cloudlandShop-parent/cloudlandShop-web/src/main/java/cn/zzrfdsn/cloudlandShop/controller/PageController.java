package cn.zzrfdsn.cloudlandShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {


    @RequestMapping("/")
    public String index() {
        return "/index";
    }

    @RequestMapping("/{page}")
    public String page(@PathVariable String page) {
//        return "/"+page;  修改springmvc.xml 添加/
        return page;
    }
}
