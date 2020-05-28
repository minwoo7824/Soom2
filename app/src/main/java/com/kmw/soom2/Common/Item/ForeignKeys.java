package com.kmw.soom2.Common.Item;

public class ForeignKeys {
    public ForeignKeys() {

    }
    int linkNo;
    String title;
    String linkUrl;
    int aliveFlag;
    String createDt;

    public int getLinkNo() {
        return linkNo;
    }

    public void setLinkNo(int linkNo) {
        this.linkNo = linkNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getAliveFlag() {
        return aliveFlag;
    }

    public void setAliveFlag(int aliveFlag) {
        this.aliveFlag = aliveFlag;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
