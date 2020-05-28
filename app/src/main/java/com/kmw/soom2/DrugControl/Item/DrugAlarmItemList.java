package com.kmw.soom2.DrugControl.Item;

public class DrugAlarmItemList {

    public DrugAlarmItemList() {

    }

    int idx;
    String selectDay;
    long selectTime;
    int pushCheck;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getSelectDay() {
        return selectDay;
    }

    public void setSelectDay(String selectDay) {
        this.selectDay = selectDay;
    }

    public long getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(long selectTime) {
        this.selectTime = selectTime;
    }

    public int getPushCheck() {
        return pushCheck;
    }

    public void setPushCheck(int pushCheck) {
        this.pushCheck = pushCheck;
    }
}
