package cn.zzrfdsn.cloudlanshop.search.controller;

import cn.zzrfdsn.cloudlandShop.vo.ItemSearchData;
import cn.zzrfdsn.cloudlandshop.search.service.ItemSearchService;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author cloudlandboy
 * @Date 2019/10/17 下午4:35
 * @Since 1.0.0
 */

@Controller
public class ItemSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemSearchService itemSearchService;

    @Value("${item_search_defaultRows}")
    private int defaut_rows;

    @RequestMapping("/search")
    public String search(String keyword, @RequestParam(defaultValue = "1") Integer page, Integer row, Model model) {

        if (StringUtils.isNotBlank(keyword)) {

            keyword = new String(keyword.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8);
            if (row == null || row < 1) {
                row = defaut_rows;
            }

            ItemSearchData itemSearchData = null;
            try {
                itemSearchData = itemSearchService.search(keyword, page, row);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            model.addAttribute("itemSearchData", itemSearchData);
        }

        return "search";
    }
}