package cn.zzrfdsn.cloudlandShop.item.vo;

import cn.zzrfdsn.cloudlandShop.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author cloudlandboy
 * @Date 2019/10/20 下午8:26
 * @Since 1.0.0
 */

public class ItemInfo extends TbItem {
    public String[] getImages() {
        if (StringUtils.isNotBlank(this.getImage())) {
            return this.getImage().split(",");
        }
        return null;
    }
}