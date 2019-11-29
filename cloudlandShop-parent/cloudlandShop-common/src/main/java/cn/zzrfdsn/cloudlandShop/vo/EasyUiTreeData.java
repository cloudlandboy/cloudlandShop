package cn.zzrfdsn.cloudlandShop.vo;

import java.io.Serializable;

public class EasyUiTreeData implements Serializable {

    private long id;
    private String text;
    //   closed代表还有子节点，open代表叶子节点
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
