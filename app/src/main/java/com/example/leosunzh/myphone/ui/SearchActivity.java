package com.example.leosunzh.myphone.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.MyResolver;
import com.example.leosunzh.myphone.logic.Person;
import com.example.leosunzh.myphone.logic.PinYinAndNumSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog搜索界面
 * Created by leosunzh on 2015/12/23.
 */
public class SearchActivity extends Activity implements TextWatcher,
        AdapterView.OnItemClickListener,View.OnClickListener{
    //搜索栏
    EditText editText;
    //搜索列表
    ListView listView;
    MyListViewAdapter myListViewAdapter;
    List<Person> list;

    List<Person> consult;//查找参考
    MyResolver myResolver;
    PinYinAndNumSearch pinYinAndNumSearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口的大小及透明度
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.TOP;
//        layoutParams.alpha = 0.5f;
        window.setAttributes(layoutParams);
        //控件
        findViewById(R.id.back_imb_dia).setOnClickListener(this);
        findViewById(R.id.clear_imb_dia).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.search_ed_dia);
        editText.addTextChangedListener(this);
        listView = (ListView) findViewById(R.id.result_lv_dia);
        listView.setOnItemClickListener(this);
        //初始化数据
        list = new ArrayList<>();
        myResolver = new MyResolver(this);
        consult = myResolver.readContacts(null);
        pinYinAndNumSearch = new PinYinAndNumSearch();

        myListViewAdapter = new MyListViewAdapter(this,R.layout.list_item_layout,list);
        listView.setAdapter(myListViewAdapter);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
            list.clear();
            list.addAll(pinYinAndNumSearch.searchForPerson(consult,s));
            myListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = (Person) listView.getItemAtPosition(position);
        PersonInfoActivity.actionStart(this,person.getId(),person.getNumber(),person.getName(),person.getColor());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_imb_dia:
                finish();break;
            case R.id.clear_imb_dia:
                editText.setText(null);break;
        }
    }

    /**
     * 启动方式
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context,SearchActivity.class);
        context.startActivity(intent);
    }
}
