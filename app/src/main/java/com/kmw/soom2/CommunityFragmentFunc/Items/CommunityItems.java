package com.kmw.soom2.CommunityFragmentFunc.Items;

public class CommunityItems implements Comparable{

    public CommunityItems() {

    }

    String no;
    String lv;
    String profile;
    String name;
    String date;
    String imgListPath;
    String contents;
    String hashTag;
    int likeCnt;
    int commentCnt;
    String userNo;

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getLv() {
        return lv;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgListPath() {
        return imgListPath;
    }

    public void setImgListPath(String imgListPath) {
        this.imgListPath = imgListPath;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
