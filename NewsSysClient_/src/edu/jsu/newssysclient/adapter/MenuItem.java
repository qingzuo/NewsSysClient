package edu.jsu.newssysclient.adapter;

/**
 * MenuAdapter.java π”√
 * @author zuo
 *
 */
public class MenuItem {

    String mTitle;
    int mIconRes;
    int mId;

    public MenuItem(String title, int iconRes, int id) {
        mTitle = title;
        mIconRes = iconRes;
        mId = id;
    }
    
    public int getMId() {
    	return mId;
    }
}
