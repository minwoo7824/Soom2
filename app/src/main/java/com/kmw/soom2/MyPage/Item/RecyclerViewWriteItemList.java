package com.kmw.soom2.MyPage.Item;


import java.util.ArrayList;

public class RecyclerViewWriteItemList {
    int userIcon;
    ArrayList<ViewPagerItemList> imageList;
    int viewType;
    public RecyclerViewWriteItemList(int userIcon , int viewType){
        this.userIcon = userIcon;

        this.viewType = viewType;
    }

    public ArrayList<ViewPagerItemList> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ViewPagerItemList> imageList) {
        this.imageList = imageList;
    }

    public int getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(int userIcon) {
        this.userIcon = userIcon;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


}
