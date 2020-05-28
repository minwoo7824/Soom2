package com.kmw.soom2.StaticFunc.Activitys.Item;

public class HistoryItems {

    public HistoryItems() {

    }

    int userHistoryNo;
    int userNo;
    int category;   // 1:복약,  11: 기침, 12: 호흡곤란, 13: 천명음, 14: 가슴답답함, 21: ACT, 22: PEF, 23: 미세먼지, 30: 메모
    String nickname;
    int gender;
    int birth;
    int age;
    String createDt;
    String updateDt;
    String registerDt;
    int aliveFlag;

    /// 복약 관련
    int medicineNo;
    int frequency;
    String volume;
    String unit;
    int emergencyFlag;
    String startDt;
    String endDt;
    String ko;

    /// 증상
    String cause;
    /// 메모
    String memo;
    /// 미세먼지
    String latitude;
    String longitute;
    String location;
    int dust;
    int dustState;
    int ultraDust;
    int ultraDustState;
    /// PEF
    int pefScore;
    int inspiratorFlag;
    /// ACT
    int actScore;
    int actType;
    int actState;
    String actSelected;


    public int getUserHistoryNo() {
        return userHistoryNo;
    }

    public void setUserHistoryNo(int userHistoryNo) {
        this.userHistoryNo = userHistoryNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getRegisterDt() {
        return registerDt;
    }

    public void setRegisterDt(String registerDt) {
        this.registerDt = registerDt;
    }

    public int getAliveFlag() {
        return aliveFlag;
    }

    public void setAliveFlag(int aliveFlag) {
        this.aliveFlag = aliveFlag;
    }

    public int getMedicineNo() {
        return medicineNo;
    }

    public void setMedicineNo(int medicineNo) {
        this.medicineNo = medicineNo;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getEmergencyFlag() {
        return emergencyFlag;
    }

    public void setEmergencyFlag(int emergencyFlag) {
        this.emergencyFlag = emergencyFlag;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDust() {
        return dust;
    }

    public void setDust(int dust) {
        this.dust = dust;
    }

    public int getDustState() {
        return dustState;
    }

    public void setDustState(int dustState) {
        this.dustState = dustState;
    }

    public int getUltraDust() {
        return ultraDust;
    }

    public void setUltraDust(int ultraDust) {
        this.ultraDust = ultraDust;
    }

    public int getUltraDustState() {
        return ultraDustState;
    }

    public void setUltraDustState(int ultraDustState) {
        this.ultraDustState = ultraDustState;
    }

    public int getPefScore() {
        return pefScore;
    }

    public void setPefScore(int pefScore) {
        this.pefScore = pefScore;
    }

    public int getInspiratorFlag() {
        return inspiratorFlag;
    }

    public void setInspiratorFlag(int inspiratorFlag) {
        this.inspiratorFlag = inspiratorFlag;
    }

    public int getActScore() {
        return actScore;
    }

    public void setActScore(int actScore) {
        this.actScore = actScore;
    }

    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }

    public int getActState() {
        return actState;
    }

    public void setActState(int actState) {
        this.actState = actState;
    }

    public String getActSelected() {
        return actSelected;
    }

    public void setActSelected(String actSelected) {
        this.actSelected = actSelected;
    }

    @Override
    public String toString() {
        return "HistoryItems{" +
                "userHistoryNo=" + userHistoryNo +
                ", userNo=" + userNo +
                ", category=" + category +
                ", nickname='" + nickname + '\'' +
                ", gender=" + gender +
                ", birth=" + birth +
                ", age=" + age +
                ", createDt='" + createDt + '\'' +
                ", updateDt='" + updateDt + '\'' +
                ", registerDt='" + registerDt + '\'' +
                ", aliveFlag=" + aliveFlag +
                ", medicineNo=" + medicineNo +
                ", frequency=" + frequency +
                ", volume='" + volume + '\'' +
                ", unit='" + unit + '\'' +
                ", emergencyFlag=" + emergencyFlag +
                ", startDt='" + startDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", ko='" + ko + '\'' +
                ", cause='" + cause + '\'' +
                ", memo='" + memo + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitute='" + longitute + '\'' +
                ", location='" + location + '\'' +
                ", dust=" + dust +
                ", dustState=" + dustState +
                ", ultraDust=" + ultraDust +
                ", ultraDustState=" + ultraDustState +
                ", pefScore=" + pefScore +
                ", inspiratorFlag=" + inspiratorFlag +
                ", actScore=" + actScore +
                ", actType=" + actType +
                ", actState=" + actState +
                ", actSelected='" + actSelected + '\'' +
                '}';
    }
}
