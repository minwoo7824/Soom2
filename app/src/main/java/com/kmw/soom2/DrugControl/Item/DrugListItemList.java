package com.kmw.soom2.DrugControl.Item;

public class DrugListItemList {

    int bannerImage;
    int viewType;
    String title;


    public final static int BANNER = 0;
    public final static int COMPLETE = 1;
    public final static int HEADER = 2;
    public final static int ITEM = 3;

    public DrugListItemList(int bannerImage, int viewType) {
        this.bannerImage = bannerImage;
        this.viewType = viewType;
    }

    public DrugListItemList(String title, int viewType) {
        this.viewType = viewType;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    public int getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(int bannerImage) {
        this.bannerImage = bannerImage;
    }


}
