package com.kmw.soom2.Common.Item;

public class PushItems {
    public PushItems() {

    }
    int pushNo;
    int userNo;
    String title;
    String contents;
    String createDt;

    public int getPushNo() {
        return pushNo;
    }

    public void setPushNo(int pushNo) {
        this.pushNo = pushNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
