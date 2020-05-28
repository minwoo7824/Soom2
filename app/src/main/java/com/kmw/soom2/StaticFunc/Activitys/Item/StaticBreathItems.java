package com.kmw.soom2.StaticFunc.Activitys.Item;

import java.util.ArrayList;

public class StaticBreathItems {
    public StaticBreathItems() {

    }
    ArrayList<HistoryItems> historyItems;
    String stDate;
    String stStatus;
    String stMeasureCnt;
    float pefRate;

    public ArrayList<HistoryItems> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItems(ArrayList<HistoryItems> historyItems) {
        this.historyItems = historyItems;
    }

    public String getStDate() {
        return stDate;
    }

    public void setStDate(String stDate) {
        this.stDate = stDate;
    }

    public String getStStatus() {
        return stStatus;
    }

    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public String getStMeasureCnt() {
        return stMeasureCnt;
    }

    public void setStMeasureCnt(String stMeasureCnt) {
        this.stMeasureCnt = stMeasureCnt;
    }

    public float getPefRate() {
        return pefRate;
    }

    public void setPefRate(float pefRate) {
        this.pefRate = pefRate;
    }
}
