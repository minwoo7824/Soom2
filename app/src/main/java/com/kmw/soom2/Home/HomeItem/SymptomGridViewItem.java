package com.kmw.soom2.Home.HomeItem;

public class SymptomGridViewItem {
    int image;
    String title;
    boolean checked;

    public SymptomGridViewItem(int gridview_image, String title, boolean checked) {
        this.image = gridview_image;
        this.title = title;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
