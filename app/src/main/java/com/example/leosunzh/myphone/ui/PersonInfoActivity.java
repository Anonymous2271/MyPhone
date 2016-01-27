package com.example.leosunzh.myphone.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.MyResolver;
import com.example.leosunzh.myphone.logic.Person;
import com.example.leosunzh.myphone.logic.SQLEditor;

import java.util.List;

/**
 * 联系人详细信息界面
 * Created by leosunzh on 2015/12/18.
 */
public class PersonInfoActivity extends Activity implements View.OnClickListener,InfoRvAdapter.OnRecyclerViewItemClickListener {
    String id;
    String name;
    String number;
    String color;
    ContentValues values = new ContentValues();

    RecyclerView recyclerView;
    Button name_info_b;
    ImageButton edit_info_imb;
    ImageButton favorite_info_imb;
    int []isShoucang = new int[]{0,1};

    MyResolver myResolver;
    SQLEditor sqlEditor;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info_layout);
        myResolver = new MyResolver(this);
        sqlEditor = new SQLEditor(this);

        name_info_b = (Button) findViewById(R.id.name_info_b);
        edit_info_imb = (ImageButton) findViewById(R.id.edit_info_imb);
        favorite_info_imb = (ImageButton) findViewById(R.id.favorite_info_imb);
        name_info_b.setOnClickListener(this);
        edit_info_imb.setOnClickListener(this);
        favorite_info_imb.setOnClickListener(this);

        //取出数据
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        color = intent.getStringExtra("color");
        inIt();

        recyclerView = (RecyclerView) findViewById(R.id.infos_rv);

        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //刷新界面
        checkIsFavorite(name);
        reFlesh();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 该联系人是否被收藏,该方法只执行一次
     * @return
     */
    public void checkIsFavorite(String name){
        List<Person> list = sqlEditor.query("name='"+name+"'");
        if(list.size()!=0){
            favorite_info_imb.setImageResource(R.drawable.icon_yishoucang);
            exChange();
        }else {
            favorite_info_imb.setImageResource(R.drawable.icon_shoucang);
        }
    }
    //用一个数组指示
    public void exChange(){
        int temp = isShoucang[0];
        isShoucang[0] = isShoucang[1];
        isShoucang[1] = temp;
    }

    /**
     * 从拨号，短信或者编辑界面返回时刷新
     */
    @Override
    protected void onStart() {
        super.onStart();
        inIt();
        reFlesh();
    }

    /**
     * 初始化数据
     */
    public void inIt() {
//        List<Person> temp;
//        temp = myResolver.readContacts(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+"='"+name+"'");
//        int l = temp.size();
//        Person person = temp.get(0);
        name_info_b.setText(" < "+name);
        values.clear();
        values.put("id",id);
        values.put("color",color);
        values.put("name",name);
        values.put("number",number);
    }

    /**
     * 刷新界面
     */
    public void reFlesh(){
        // 创建Adapter，并指定数据集
        InfoRvAdapter infoRvAdapter = new InfoRvAdapter(values,this,getWidth());
        //设置自定义的监听器
        infoRvAdapter.setOnRecyclerViewItemClickListener(this);
        // 设置Adapter
        recyclerView.setAdapter(infoRvAdapter);
    }

    /**
     * 点击顶部按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.name_info_b://点击返回
                MyPhone.actionStart(this);
                finish();
                break;
            case R.id.edit_info_imb://点击编辑
                Intent intent = new Intent(this,NewPerson.class);
                intent.putExtra("number",number);
                intent.putExtra("name", name);
                intent.putExtra("id",id);
                intent.putExtra("flag", true);
                this.startActivityForResult(intent,1);
                break;
            case R.id.favorite_info_imb://点击收藏/取消收藏
                if(isShoucang[0]==0){
                    sqlEditor.add(id,name,number);
                    favorite_info_imb.setImageResource(R.drawable.icon_yishoucang);
                    exChange();
                    Toast.makeText(this,"已收藏联系人",Toast.LENGTH_SHORT).show();
                }else {
                    sqlEditor.delete(name);
                    favorite_info_imb.setImageResource(R.drawable.icon_shoucang);
                    exChange();
                }
                break;
        }
    }

    /**
     * 点击item
     * @param view
     * @param position
     */
    @Override
    public void onRvItemClick(View view, int position) {
        switch(position){
            case 1://点击电话
                Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
                startActivity(intent_call);
                break;
            case 2://点击短信
                Intent intent_message = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+number));
                startActivity(intent_message);
                break;
            default:break;
        }
    }

    /**
     * 从编辑活动获取回传数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            //清空数据
            this.name_info_b.setText(null);
            //重置数据
            this.name = data.getStringExtra("name");
            this.number = data.getStringExtra("number");
            this.name_info_b.setText(" < "+name);
//            inIt();
//            reFlesh();
        }
    }

    /**
     * 启动方式
     * @param context
     * @param id
     * @param number
     * @param name
     * @param color
     */
    public static void actionStart(Context context, String id, String number, String name, String color) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra("number",number);
        intent.putExtra("name",name);
        intent.putExtra("id",id);
        intent.putExtra("color",color);
        context.startActivity(intent);
    }

    /**
     * 获取屏幕宽度
     * 设置大头像高等于宽
     */
    private int getWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

}
