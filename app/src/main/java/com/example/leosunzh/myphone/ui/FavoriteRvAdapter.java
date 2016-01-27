package com.example.leosunzh.myphone.ui;

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
import com.example.leosunzh.myphone.logic.Person;

import java.util.List;
import java.util.Random;

/**
 * Created by leosunzh on 2015/12/22.
 */
public class FavoriteRvAdapter extends RecyclerView.Adapter<FavoriteRvAdapter.ViewHolder>{
    String[] colors = {"#FFDF2323","#FFCE591B","#FF1746E1","#FF1D8138","#FF0FB66B","#FF6660C1","#FF6615A4","#FFA91245"};
    final static int NORMAL = 1;
    int count;

    List<Person> list;
    MyResolver myResolver;
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;//传入的点击监听对象

    public FavoriteRvAdapter(List<Person> list, Context context){
        this.list = list;
        myResolver = new MyResolver(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        switch (viewType){
            case NORMAL:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_layout,parent,false);
                viewHolder = new ViewHolder(view,onRecyclerViewItemClickListener);
                break;
            default:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item_layout,parent,false);
                viewHolder = new ViewHolder(view1,onRecyclerViewItemClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = list.get(position);
        List<CallLog> logs = myResolver.readCallLog(android.provider.CallLog.Calls.CACHED_NAME + "='" + person.getName() + "'");

        holder.textView_name.setText(person.getName());
        holder.textView_number.setText(person.getNumber());
        if (myResolver.getHighPhoto(person.getId()) == null) {//没有头像时
            holder.imageView.setImageResource(R.drawable.icon_dephoto);
            Random random = new Random();//设置默认头像，并给定随机的背景颜色
            person.setColor(colors[random.nextInt(8)]);
            holder.imageView.setBackgroundColor(Color.parseColor(person.getColor()));
        } else {
            holder.imageView.setImageBitmap(myResolver.getHighPhoto(person.getId()));
        }
        if (logs.size() == 0) {//没有通话记录
            holder.imageView_log_type.setImageResource(R.drawable.icon_zuijin);
            holder.textView_log.setText("最近未联系");
        } else {
            holder.textView_log.setText(logs.get(0).getTime());
            switch (logs.get(0).getType()) {
                case "呼入":
                    holder.imageView_log_type.setImageResource(R.drawable.log_huru);
                    break;
                case "呼出":
                    holder.imageView_log_type.setImageResource(R.drawable.log_bochu);
                    break;
                case "未接":
                    holder.imageView_log_type.setImageResource(R.drawable.log_weijie);
                    break;
                case "挂断":
                    holder.imageView_log_type.setImageResource(R.drawable.log_guaduan);
                    break;
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (list.size()){
            default:return NORMAL;
        }
    }


    @Override
    public int getItemCount() {
        return list.size();//getItemCount()==0时适配器不会绘制itemView
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView_name;
        TextView textView_number;
        ImageView imageView_log_type;
        TextView textView_log;
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        public ViewHolder(View itemView,OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView);
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.favorite_item_iv);
            textView_name = (TextView) itemView.findViewById(R.id.favorite_name_tv);
            textView_number = (TextView) itemView.findViewById(R.id.favorite_number_tv);
            imageView_log_type = (ImageView) itemView.findViewById(R.id.favorite_type_iv);
            textView_log = (TextView) itemView.findViewById(R.id.favorite_log_tv);
        }

        @Override
        public void onClick(View v) {
            Person person = list.get(getPosition());
            onRecyclerViewItemClickListener.onRvItemClick(person.getId(),person.getName(),person.getNumber(),person.getColor());
        }
    }

    //定义监听器外部实现接口，
    public static interface OnRecyclerViewItemClickListener {
        void onRvItemClick(String id,String name,String number,String color);
    }

    //模仿ListView的设置监听对象方法
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener){
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
