package cn.zzrfdsn.cloudlandShop.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author cloudlandboy
 * @Date 2019/10/17 下午4:40
 * @Since 1.0.0
 */

public class ItemSearchData implements Serializable {


    /**
     * 封装查询结果
     */
    private List<ItemSearchResult> itemList;

    /**
     * 查询出的总页数
     */
    private int totalPages;

    /**
     * 查询关键字
     */
    private String query;

    /**
     * 当前页
     */
    private int page;

    /**
     * 总记录数
     */
    private int recourdCount;

    public List<ItemSearchResult> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemSearchResult> itemList) {
        this.itemList = itemList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRecourdCount() {
        return recourdCount;
    }

    public void setRecourdCount(int recourdCount) {
        this.recourdCount = recourdCount;
    }
}