package com.mychauffeurapp.navdrawer;

public class NavDrawerItem {


    private int icon;
    private String title;
    private String count = "0";
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;

    public NavDrawerItem() {
    }

    public NavDrawerItem(String title, int icon) {

        this.icon = icon;
        this.title = title;
    }

    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count) {

        this.icon = icon;
        this.title = title;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }


    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean getCounterVisibility() {
        return this.isCounterVisible;
    }

    public void setCounterVisibility(boolean isCounterVisible) {
        this.isCounterVisible = isCounterVisible;
    }
}
