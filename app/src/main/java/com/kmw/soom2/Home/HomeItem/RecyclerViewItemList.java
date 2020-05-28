package com.kmw.soom2.Home.HomeItem;


import com.kmw.soom2.StaticFunc.Activitys.Item.HistoryItems;

import java.util.ArrayList;
import java.util.TreeMap;

public class RecyclerViewItemList {
    String title;
    int viewType;
    ArrayList<String> medicineKo;
    ArrayList<EtcItem> etcItemArrayList = new ArrayList<>();
    EtcItem etcItem;
    MedicineTakingItem medicineTakingItem;

    TreeMap<String, ArrayList<HistoryItems>> historyItemList = new TreeMap<>();

    public final static int HEADER_TYPE = 0;
    public final static int ITEM_TYPE = 1;

    public RecyclerViewItemList(String title, int viewType, TreeMap<String, ArrayList<HistoryItems>> historyItemList){
        this.title = title;
        this.viewType = viewType;

        this.historyItemList = historyItemList;
    }

    public RecyclerViewItemList(String title, int viewType, ArrayList<EtcItem> etcItems){
        this.title = title;
        this.viewType = viewType;
        this.etcItemArrayList = etcItems;
    }
    public RecyclerViewItemList(MedicineTakingItem medicineTakingItem,int viewType){
        this.medicineTakingItem = medicineTakingItem;
        this.viewType = viewType;
    }
    public RecyclerViewItemList(String title, int viewType){
        this.title = title;
        this.viewType = viewType;
    }
    public MedicineTakingItem getMedicineTakingItem() {
        return medicineTakingItem;
    }


    public void setMedicineTakingItem(MedicineTakingItem medicineTakingItem) {
        this.medicineTakingItem = medicineTakingItem;
    }


    public RecyclerViewItemList(ArrayList<String> arrayList, EtcItem etcItem ,int viewType){
        this.medicineKo = arrayList;
        this.viewType = viewType;
        this.etcItem = etcItem;
//        this.etcItemArrayList.add(etcItem);
    }

    public EtcItem getEtcItem() {
        return etcItem;
    }

    public void setEtcItem(EtcItem etcItem) {
        this.etcItem = etcItem;
    }

    public ArrayList<String> getMedicineKo() {
        return medicineKo;
    }

    public void setMedicineKo(ArrayList<String> medicineKo) {
        this.medicineKo = medicineKo;
    }

    public ArrayList<EtcItem> getEtcItemArrayList() {
        return etcItemArrayList;
    }

    public void setEtcItemArrayList(ArrayList<EtcItem> etcItemArrayList) {
        this.etcItemArrayList = etcItemArrayList;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "RecyclerViewItemList{" +
                "title='" + title + '\'' +
                ", viewType=" + viewType +
                ", medicineKo=" + medicineKo +
                ", etcItemArrayList=" + etcItemArrayList +
                ", etcItem=" + etcItem +
                ", medicineTakingItem=" + medicineTakingItem +
                ", historyItemList=" + historyItemList +
                '}';
    }

    public TreeMap<String, ArrayList<HistoryItems>> getHistoryItemList() {
        return historyItemList;
    }

    public void setHistoryItemList(TreeMap<String, ArrayList<HistoryItems>> historyItemList) {
        this.historyItemList = historyItemList;
    }
}




