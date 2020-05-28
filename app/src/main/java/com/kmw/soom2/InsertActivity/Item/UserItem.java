package com.kmw.soom2.InsertActivity.Item;

public class UserItem {
    public  UserItem() {

    }
    int userNo;
    int lv;
    String id;
    String email;
    String password;
    String nickname;
    String name;
    int birth;
    int gender;
    int diagnosisFlag;  // 확진 여부
    String outbreakDt; // 발병일
    String profileImg;  // 프로필 사진
    String deviceCode;  // fcm토큰
    int loginType;  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
    int osType; // 1 : android, 2 : ios
    String createDt;
    String updateDt;
    String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getDiagnosisFlag() {
        return diagnosisFlag;
    }

    public void setDiagnosisFlag(int diagnosisFlag) {
        this.diagnosisFlag = diagnosisFlag;
    }

    public String getOutbreakDt() {
        return outbreakDt;
    }

    public void setOutbreakDt(String outbreakDt) {
        this.outbreakDt = outbreakDt;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public int getOsType() {
        return osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
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
}
