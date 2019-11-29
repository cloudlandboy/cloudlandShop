package cn.zzrfdsn.cloudlandShop.service;

import cn.zzrfdsn.cloudlandShop.vo.EasyUiTreeData;

import java.util.List;

public interface ItemCatService {
    
    List<EasyUiTreeData> findCatagotyForTree(long pid);
}
