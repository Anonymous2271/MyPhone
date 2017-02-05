package com.example.leosunzh.myphone.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.leosunzh.myphone.R;

/**
 * 主活动
 */
public class MyPhone extends FragmentActivity implements View.OnClickListener{
    TabLayout tabLayout;
    //标题栏
    RelativeLayout title_layout;
    ImageButton search_imb_title;
    //碎片
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_phone_layout);
        //标题栏
        title_layout = (RelativeLayout) findViewById(R.id.title_layout);
        search_imb_title = (ImageButton) findViewById(R.id.search_imb_title);
        search_imb_title.setOnClickListener(this);
        //TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter= new ViewPagerAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 启动方式
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,MyPhone.class);
        context.startActivity(intent);
    }

    /**
     * 点击搜索
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_imb_title:
                SearchActivity.actionStart(this);break;
        }
    }
}
