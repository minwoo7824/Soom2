package com.kmw.soom2.Home.HomeItem;

public class TakingRecordItemList {

    int imageView;
    String drugName;
    String stateButton;

    String title;
    int normalButton,emergencyButton,passButton;
    String normalTextView,emergencyTextView,passTextView;
    int viewType;

    public static final int HEADER_TYPE = 0;
    public static final int BODY_TYPE = 1;


    public TakingRecordItemList(String title, int viewType) {

        this.title = title;

        this.viewType = viewType;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getStateButton() {
        return stateButton;
    }

    public void setStateButton(String stateButton) {
        this.stateButton = stateButton;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNormalButton() {
        return normalButton;
    }

    public void setNormalButton(int normalButton) {
        this.normalButton = normalButton;
    }

    public int getEmergencyButton() {
        return emergencyButton;
    }

    public void setEmergencyButton(int emergencyButton) {
        this.emergencyButton = emergencyButton;
    }

    public int getPassButton() {
        return passButton;
    }

    public void setPassButton(int passButton) {
        this.passButton = passButton;
    }

    public String getNormalTextView() {
        return normalTextView;
    }

    public void setNormalTextView(String normalTextView) {
        this.normalTextView = normalTextView;
    }

    public String getEmergencyTextView() {
        return emergencyTextView;
    }

    public void setEmergencyTextView(String emergencyTextView) {
        this.emergencyTextView = emergencyTextView;
    }

    public String getPassTextView() {
        return passTextView;
    }

    public void setPassTextView(String passTextView) {
        this.passTextView = passTextView;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }





}
