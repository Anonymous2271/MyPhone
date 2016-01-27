package com.example.leosunzh.myphone.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.CallLog;

import java.util.List;

/**
 * 通话记录list适配器
 * Created by leosunzh on 2015/12/15.
 */
public class CallLogListViewAdapter extends ArrayAdapter<CallLog>{
    public int  resourceId;//布局ID

    public CallLogListViewAdapter(Context context, int resource, List<CallLog> list) {
        super(context, resource,list);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CallLog callLog = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.name_log = (TextView) view.findViewById(R.id.name_log);
            viewHolder.number_log = (TextView) view.findViewById(R.id.number_log);
            viewHolder.type_log = (ImageView) view.findViewById(R.id.type_log);
            viewHolder.time_log = (TextView) view.findViewById(R.id.time_log);
            view.setTag(viewHolder);
        }else {
            view =convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        switch (callLog.getType()){
            case "呼入":viewHolder.type_log.setImageResource(R.drawable.log_huru);break;
            case "呼出":viewHolder.type_log.setImageResource(R.drawable.log_bochu);break;
            case "未接":viewHolder.type_log.setImageResource(R.drawable.log_weijie);break;
            case "挂断":viewHolder.type_log.setImageResource(R.drawable.log_guaduan);break;
            case "person":viewHolder.type_log.setImageResource(R.drawable.log_person);break;
        }
        viewHolder.name_log.setText(callLog.getName());
        viewHolder.number_log.setText(callLog.getNumber());
        viewHolder.time_log.setText(callLog.getTime());
        return view;
    }

    class ViewHolder{
        TextView name_log;
        TextView number_log;
        ImageView type_log;
        TextView time_log;
    }
}
