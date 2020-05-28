package com.kmw.soom2.Home.HomeItem;

public class BannerItem {

    public BannerItem() {

    }

    String bannerNo;
    String bannerType;
    String bannerLink;
    String imageFile;
    int priority;
    int aliveFlag;

    public String getBannerNo() {
        return bannerNo;
    }

    public void setBannerNo(String bannerNo) {
        this.bannerNo = bannerNo;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public String getBannerLink() {
        return bannerLink;
    }

    public void setBannerLink(String bannerLink) {
        this.bannerLink = bannerLink;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getAliveFlag() {
        return aliveFlag;
    }

    public void setAliveFlag(int aliveFlag) {
        this.aliveFlag = aliveFlag;
    }
}
