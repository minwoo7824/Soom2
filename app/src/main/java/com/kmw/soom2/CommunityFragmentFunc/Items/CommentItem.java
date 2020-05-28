package com.kmw.soom2.CommunityFragmentFunc.Items;

public class CommentItem {

    public CommentItem() {

    }

    String commentNo;
    String profilePath;
    String nickname;
    String contents;
    String date;
    String userNo;
    String commentReplyNo;

    public String getCommentReplyNo() {
        return commentReplyNo;
    }

    public void setCommentReplyNo(String commentReplyNo) {
        this.commentReplyNo = commentReplyNo;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(String commentNo) {
        this.commentNo = commentNo;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
