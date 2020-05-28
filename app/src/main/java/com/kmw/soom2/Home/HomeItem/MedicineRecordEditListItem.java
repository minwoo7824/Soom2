package com.kmw.soom2.Home.HomeItem;

public class MedicineRecordEditListItem {

    public MedicineRecordEditListItem() {

    }

    String flag;
    String no;
    String historyNo;
    String name;
    String contents;
    int medicineTypeNo;
    int medicineNo;

    public int getMedicineNo() {
        return medicineNo;
    }

    public void setMedicineNo(int medicineNo) {
        this.medicineNo = medicineNo;
    }

    public int getMedicineTypeNo() {
        return medicineTypeNo;
    }

    public void setMedicineTypeNo(int medicineTypeNo) {
        this.medicineTypeNo = medicineTypeNo;
    }

    public String getHistoryNo() {
        return historyNo;
    }

    public void setHistoryNo(String historyNo) {
        this.historyNo = historyNo;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
