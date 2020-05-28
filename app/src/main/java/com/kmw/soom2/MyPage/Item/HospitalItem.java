package com.kmw.soom2.MyPage.Item;

public class HospitalItem {
    public HospitalItem() {

    }
    int hospitalNo;     // db상에 저장된 병원 번호
    int userNo;         // 회원 번호
    String name;        // 병원 명
    String addr;        // 병원 주소
    String department;  // 담당 과
    String doctor;      // 의사 명
    String createDt;
    String updateDt;

    public int getHospitalNo() {
        return hospitalNo;
    }

    public void setHospitalNo(int hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public String toString() {
        return "HospitalItem{" +
                "hospitalNo=" + hospitalNo +
                ", userNo=" + userNo +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", department='" + department + '\'' +
                ", doctor='" + doctor + '\'' +
                ", createDt='" + createDt + '\'' +
                ", updateDt='" + updateDt + '\'' +
                '}';
    }
}
