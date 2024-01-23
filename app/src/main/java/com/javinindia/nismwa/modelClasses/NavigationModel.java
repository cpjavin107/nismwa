package com.javinindia.nismwa.modelClasses;

import android.graphics.drawable.Drawable;

/**
 * Created by Ashish Tiwari on 12/27/2016.
 */
public class NavigationModel {

    private String navTitle;
    private Drawable navIcons;

    public String getNavTitle() {
        return navTitle;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    public Drawable getNavIcons() {
        return navIcons;
    }

    public void setNavIcons(Drawable navIcons) {
        this.navIcons = navIcons;
    }
}
