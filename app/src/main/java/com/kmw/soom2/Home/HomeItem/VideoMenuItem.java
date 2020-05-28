package com.kmw.soom2.Home.HomeItem;

import android.graphics.Bitmap;
import android.net.Uri;

public class VideoMenuItem {

    private String title;
    private Bitmap img;
    private Uri uri;

    public VideoMenuItem(String title, Bitmap img, Uri uri) {
        this.title = title;
        this.img = img;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImg() {
        return img;
    }

    public Uri getUri() {
        return uri;
    }
}
