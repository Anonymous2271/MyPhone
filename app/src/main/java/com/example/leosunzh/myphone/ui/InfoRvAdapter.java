package com.example.leosunzh.myphone.ui;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.CallLog;
import com.example.leosunzh.myphone.logic.MyResolver;

import java.util.List;

/**
 * 联系人详细界面RecyclerView适配器
 * Created by leosunzh on 2015/12/18.
 */
public class InfoRvAdapter extends RecyclerView.Adapter<InfoRvAdapter.ViewHolder>{

    final int HEADER = 0;
    final int CALL = 1;
    final int MESSAGE = 2;
    final int ITEM = 3;
    public int count;
    public int width;

    MyResolver myResolver;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;//传入的点击监听对象

    ContentValues values;
    String id;
    String color;
    String name;
    String number;
    List<CallLog> logs;

    public InfoRvAdapter(ContentValues values,Context context,int width){
        this.values = values;
        myResolver = new MyResolver(context);
        this.width = width;

        id = values.getAsString("id");
        name = values.getAsString("name");
        number = values.getAsString("number");
        color = values.getAsString("color");

        logs = myResolver.readCallLog(android.provider.CallLog.Calls.CACHED_NAME+"='"+name+"'");
        count = values.size() + logs.size();
    }

    @Override
    public InfoRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        switch (viewType){
            case HEADER:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reclyer_person_info_header,parent,false);
                viewHolder = new ViewHolder(view,onRecyclerViewItemClickListener);
                break;
            default:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.reclyer_person_info_item,parent,false);
                viewHolder = new ViewHolder(view1,onRecyclerViewItemClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InfoRvAdapter.ViewHolder holder, int position) {
        switch (position){
            case HEADER:
                holder.imageView = (ImageView) holder.itemView.findViewById(R.id.page_header_iv);
                holder.imageView.getLayoutParams().height = width;//设置高等于宽
                holder.imageView.getLayoutParams().width = width;
                if(myResolver.getHighPhoto(id)==null){
                    holder.imageView.setImageResource(R.drawable.dephoto);
                    holder.imageView.setBackgroundColor(Color.parseColor(color));
                }else {
                    holder.imageView.setImageBitmap(myResolver.getHighPhoto(id));
                }
                break;
            case CALL:
                holder.imageView.setImageResource(R.drawable.icon_call);
                holder.textView.setText(number);
                break;
            case MESSAGE:
                holder.imageView.setImageResource(R.drawable.icon_message);
                holder.textView.setText(number);
                break;
            case ITEM:
                holder.imageView.setImageResource(R.drawable.icon_zuijin);
                holder.textView.setText("通话记录");
                break;
            default:
                switch (logs.get(position-values.size()).getType()){
                    case "呼入":holder.imageView.setImageResource(R.drawable.log_huru);break;
                    case "呼出":holder.imageView.setImageResource(R.drawable.log_bochu);break;
                    case "未接":holder.imageView.setImageResource(R.drawable.log_weijie);break;
                    case "挂断":holder.imageView.setImageResource(R.drawable.log_guaduan);break;
                }
                holder.textView.setText(logs.get(position-values.size()).getTime()+"    时长:"+logs.get(position-values.size()).getDuration()+"s");
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:return HEADER;
            default:return ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        public ViewHolder(View itemView,OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView);
            //传递给适配器的监听对象再传递给当前ViewHolder
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
            //给根View设置OnClickListener
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.page_item_iv);
            textView = (TextView) itemView.findViewById(R.id.page_item_tv);
        }
        //调用接口方法，双重观察者模式
        @Override
        public void onClick(View v) {
            onRecyclerViewItemClickListener.onRvItemClick(v,getPosition());
        }
    }

    //定义监听器外部实现接口，
    public static interface OnRecyclerViewItemClickListener {
        void onRvItemClick(View view , int position);
    }

    //模仿ListView的设置监听对象方法
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

}
