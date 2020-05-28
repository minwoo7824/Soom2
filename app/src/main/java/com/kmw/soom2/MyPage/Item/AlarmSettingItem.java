package com.kmw.soom2.MyPage.Item;

public class AlarmSettingItem {
    int symptomFlag;
    int dosingFlag;
    int noticeFlag;
    int communityLikeFlag;
    int communityCommentFlag;
    int communityCommentReplyFlag;

    int allFlag;

    public int checkSettingFlag() {
        if ((symptomFlag == 1) && (dosingFlag == 1) && (noticeFlag == 1) && (communityLikeFlag == 1) && (communityCommentFlag == 1) && (communityCommentReplyFlag == 1)) {
            allFlag = 1;
        }else {
            allFlag = -1;
        }

        return allFlag;
    }

    public void setAllCheck(boolean flag) {
        if (flag) {
            symptomFlag = 1;
            dosingFlag = 1;
            noticeFlag = 1;
            communityLikeFlag = 1;
            communityCommentFlag = 1;
            communityCommentReplyFlag = 1;


        }else {
            symptomFlag = -1;
            dosingFlag = -1;
            noticeFlag = -1;
            communityLikeFlag = -1;
            communityCommentFlag = -1;
            communityCommentReplyFlag = -1;
        }
    }

    public int getSymptomFlag() {
        return symptomFlag;
    }

    public void setSymptomFlag(int symptomFlag) {
        this.symptomFlag = symptomFlag;
    }

    public int getDosingFlag() {
        return dosingFlag;
    }

    public void setDosingFlag(int dosingFlag) {
        this.dosingFlag = dosingFlag;
    }

    public int getNoticeFlag() {
        return noticeFlag;
    }

    public void setNoticeFlag(int noticeFlag) {
        this.noticeFlag = noticeFlag;
    }

    public int getCommunityLikeFlag() {
        return communityLikeFlag;
    }

    public void setCommunityLikeFlag(int communityLikeFlag) {
        this.communityLikeFlag = communityLikeFlag;
    }

    public int getCommunityCommentFlag() {
        return communityCommentFlag;
    }

    public void setCommunityCommentFlag(int communityCommentFlag) {
        this.communityCommentFlag = communityCommentFlag;
    }

    public int getCommunityCommentReplyFlag() {
        return communityCommentReplyFlag;
    }

    public void setCommunityCommentReplyFlag(int communityCommentReplyFlag) {
        this.communityCommentReplyFlag = communityCommentReplyFlag;
    }

    @Override
    public String toString() {
        return "AlarmSettingItem{" +
                "symptomFlag=" + symptomFlag +
                ", dosingFlag=" + dosingFlag +
                ", noticeFlag=" + noticeFlag +
                ", communityLikeFlag=" + communityLikeFlag +
                ", communityCommentFlag=" + communityCommentFlag +
                ", communityCommentReplyFlag=" + communityCommentReplyFlag +
                '}';
    }
}
