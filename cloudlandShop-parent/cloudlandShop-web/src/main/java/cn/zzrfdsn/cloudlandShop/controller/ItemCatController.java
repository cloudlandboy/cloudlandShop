package cn.zzrfdsn.cloudlandShop.controller;

import cn.zzrfdsn.cloudlandShop.service.ItemCatService;
import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUiTreeData> list(@RequestParam(name = "id",defaultValue = "0") long pid){

        List<EasyUiTreeData> easyUiTreeDatas = itemCatService.findCatagotyForTree(pid);
        return easyUiTreeDatas;
    }
}
