package cn.zzrfdsn.cloudlandShop.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbItemCatMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemCat;
import cn.zzrfdsn.cloudlandShop.pojo.TbItemCatExample;
import cn.zzrfdsn.cloudlandShop.service.ItemCatService;
import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUiTreeData> findCatagotyForTree(long pid) {

        TbItemCatExample itemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = itemCatExample.createCriteria();
        criteria.andParentIdEqualTo(pid);


        List<TbItemCat> itemCats = itemCatMapper.selectByExample(itemCatExample);

        List<EasyUiTreeData> easyUiTreeDatas = null;

        if (itemCats != null && itemCats.size() > 0) {
            easyUiTreeDatas = new ArrayList<>();
            for (TbItemCat itemCat : itemCats) {
                EasyUiTreeData treeData = new EasyUiTreeData();
                treeData.setId(itemCat.getId());
                treeData.setText(itemCat.getName());
                //通过逆向工程生成代码，如果字段是 int(1) 或者 tinyint(1) 会自动生成Boolean类型
                treeData.setState(itemCat.getIsParent() ? "closed" : "open");
                easyUiTreeDatas.add(treeData);
            }
        }


        return easyUiTreeDatas;
    }
}
