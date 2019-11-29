package cn.zzrfdsn.cloudlandShop.content.service.impl;

import cn.zzrfdsn.cloudlandShop.mapper.TbContentCategoryMapper;
import cn.zzrfdsn.cloudlandShop.pojo.TbContentCategory;
import cn.zzrfdsn.cloudlandShop.pojo.TbContentCategoryExample;
import cn.zzrfdsn.cloudlandShop.util.CloudlandShopResult;
import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;
import cn.zzrfdsn.cloudlandshop.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/9/28 下午5:47
 * @Since 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyUiTreeData> findCategoryForTree(Long pid) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(pid);
        List<TbContentCategory> ContentCategories = contentCategoryMapper.selectByExample(example);

        List<EasyUiTreeData> data = new ArrayList<>();
        if (contentCategoryMapper != null) {
            for (TbContentCategory contentCategory : ContentCategories) {
                EasyUiTreeData easyUiTreeData = new EasyUiTreeData();
                easyUiTreeData.setId(contentCategory.getId());
                easyUiTreeData.setText(contentCategory.getName());
                //当设置为 'closed' 时，该节点有子节点，并且将从远程站点加载它们。
                easyUiTreeData.setState(contentCategory.getIsParent() ? "closed" : "open");
                data.add(easyUiTreeData);
            }
        }

        return data;
    }

    @Override
    public CloudlandShopResult addContentCategoty(TbContentCategory contentCategory) {
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        if (!parent.getIsParent()){
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        contentCategory.setIsParent(false);

        Date date=new Date();
        contentCategory.setCreated(date);
        contentCategory.setUpdated(date);
        //状态。可选值:1(正常),2(删除)
        contentCategory.setStatus(1);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        contentCategory.setSortOrder(1);
        contentCategoryMapper.insert(contentCategory);

        return CloudlandShopResult.ok(contentCategory);

    }

    @Override
    public CloudlandShopResult updateContentCategoty(TbContentCategory contentCategory) {
        contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
        return CloudlandShopResult.ok();
    }

    @Override
    public CloudlandShopResult deleteContentCategoty(TbContentCategory contentCategory) {
        //查询是否为父节点，不允许直接删除父节点
        contentCategory= contentCategoryMapper.selectByPrimaryKey(contentCategory.getId());
        if(contentCategory.getIsParent()){
            return CloudlandShopResult.build(403,"不允许的操作，请先删除子节点！");
        }
        contentCategory.setStatus(2);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);

        //查询父节点是否还有子节点，没有把isParent设置为false
        TbContentCategoryExample contentCategoryExample=new TbContentCategoryExample();
        contentCategoryExample.createCriteria().andParentIdEqualTo(contentCategory.getParentId()).andStatusEqualTo(1);

        long count = contentCategoryMapper.countByExample(contentCategoryExample);

        if (count<1){
            TbContentCategory parent=new TbContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }

        return CloudlandShopResult.ok();
    }
}