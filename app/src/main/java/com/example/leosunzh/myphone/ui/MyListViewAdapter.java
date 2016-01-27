package com.example.leosunzh.myphone.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.MyResolver;
import com.example.leosunzh.myphone.logic.Person;

import java.util.List;
import java.util.Random;

/**
 * 联系人列表适配器
 * Created by leosunzh on 2015/12/12.
 */
public class MyListViewAdapter extends ArrayAdapter<Person>{
    public int  resourceId;//布局ID
    MyResolver myResolver;
    String[] colors = {"#FFDF2323","#FFCE591B","#FF1746E1","#FF1D8138","#FF0FB66B","#FF6660C1","#FF6615A4","#FFA91245"};

    public MyListViewAdapter(Context context, int resource, List<Person> list) {
        super(context, resource,list);
        this.resourceId = resource;
        myResolver = new MyResolver(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = getItem(position);
        ViewHolder viewHolder;
        View view;

        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.name_item = (TextView) view.findViewById(R.id.name_item);
            viewHolder.photo_item = (ImageView) view.findViewById(R.id.photo_item);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name_item.setText(person.getName());
        if(myResolver.getHighPhoto(person.getId())==null){//没有头像时
            viewHolder.photo_item.setImageResource(R.drawable.icon_dephoto);
            Random random = new Random();//设置默认头像，并给定随机的背景颜色
            person.setColor(colors[random.nextInt(8)]);
            viewHolder.photo_item.setBackgroundColor(Color.parseColor(person.getColor()));
        }else {
            viewHolder.photo_item.setImageBitmap(myResolver.getHighPhoto(person.getId()));
        }
        return view;
    }

//    @Override
//    public int getItemViewType(int position) {
//        switch (position){
//            case 0:re
//        }
//    }

    class ViewHolder{
        ImageView photo_item;
        TextView name_item;
    }

}
