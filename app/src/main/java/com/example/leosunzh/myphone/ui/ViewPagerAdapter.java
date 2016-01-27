package com.example.leosunzh.myphone.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 主活动ViewPage适配器，和Tablayout配套使用
 * Created by leosunzh on 2015/12/12.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT =3;
    private String titles[]={"电话","收藏","联系人"} ;
//    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
//        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                return CallFragment.newInstance(position);
            case 1:
                return FavoriteFragment.newInstance(position);
            case 2:
                return PersonFragment.newInstance(position);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}