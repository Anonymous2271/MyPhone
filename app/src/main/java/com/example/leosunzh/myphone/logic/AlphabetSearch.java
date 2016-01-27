package com.example.leosunzh.myphone.logic;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表左侧快速索引Touch处理类
 * Created by leosunzh on 2015/12/20.
 */
public class AlphabetSearch implements View.OnTouchListener{
    LinearLayout alphabetLayout;//包含字母的线性布局
    ListView listView;
    TextView current; //屏幕居中显示当前选中的字母，手指离开时隐藏
    List<String> firstKey; //联系人数据list<Persen>的首字母集合

    String alpabet[]={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R",
                       "S","T","U","V","W","X","Y","Z","#"};
    float height;//alphabetLayout的高度

    public AlphabetSearch(LinearLayout alphabetLayout, ListView listView, TextView current){
        this.alphabetLayout = alphabetLayout;
        this.listView = listView;
        this.current = current;

        ViewTreeObserver vto = this.alphabetLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = AlphabetSearch.this.alphabetLayout.getHeight();
                AlphabetSearch.this.alphabetLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        alphabetLayout.setOnTouchListener(this);
    }

    public void whenListChange(List<Person>list){
        this.firstKey = getFirstKey(list);
    }


    /**
     * 触摸事件
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        current.setVisibility(View.VISIBLE);
        float Y = (event.getY()/height)*27;
        int index = (int)Y;
        if(index > -1 && index < 28) {//防止越界
            String key = alpabet[index];
            current.setText(key);
            if(firstKey.contains(key)){
                int position = firstKey.indexOf(key);
                listView.setSelectionFromTop(position,0);
            }
        }

        //手指抬起或者离开UI边界
        if (event.getAction() == MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_OUTSIDE) {
            current.setVisibility(View.GONE);
        }
        return true;
    }

    /**
     * 获取联系人拼音首字母字符数组
     */
    public List<String> getFirstKey(List<Person>list){
        List<String> firstKey = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            String key = list.get(i).getPinYin().substring(0,1);
            if(key.matches("[A-Z]")){
                firstKey.add(key);
            }else {
                firstKey.add("#");
            }
        }
        return firstKey;
    }


}
