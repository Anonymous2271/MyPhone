package com.example.leosunzh.myphone.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.MyResolver;

/**
 * 增删改
 * 联系人编辑界面
 * Created by leosunzh on 2015/12/12.
 */
public class NewPerson extends Activity implements View.OnClickListener {
    EditText new_name;
    EditText new_number;
    ImageView new_photo;
    String id;

    ImageButton delete;

    MyResolver resolver;
    Boolean flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_person_layout);

        this.resolver = new MyResolver(this);
        new_name = (EditText) findViewById(R.id.new_name);
        new_number = (EditText) findViewById(R.id.new_number);
        new_photo = (ImageView) findViewById(R.id.new_photo);
        delete = (ImageButton) findViewById(R.id.delete_info);

        Intent intent = getIntent();
        if (flag = intent.getBooleanExtra("flag", false)) {
            new_name.setHint(intent.getStringExtra("name"));
            new_number.setHint(intent.getStringExtra("number"));
            id = intent.getStringExtra("id");
            if(resolver.getHighPhoto(id)==null){
                new_photo.setImageResource(R.drawable.dephoto);
            }else {
                new_photo.setImageBitmap(resolver.getHighPhoto(id));
            }
            delete.setVisibility(View.VISIBLE);
        } else {
            new_name.setHint("姓名");
            new_number.setHint("号码");
            new_photo.setImageResource(R.drawable.dephoto);
            delete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_info:
                onBackPressed();
                break;
            case R.id.delete_info:
                deletePerson();
                break;
            case R.id.save_info:
                revisePerson();
                break;
            case R.id.new_photo:
                resolver.updateOrInsertPhoto(id,resolver.Bitmap2Bytes( BitmapFactory.decodeResource(this.getResources(), R.drawable.lan)));
                Toast.makeText(this,"00",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(flag) {
            Intent intent = new Intent();
            intent.putExtra("name", new_name.getHint().toString());
            intent.putExtra("number", new_number.getHint().toString());
            setResult(1, intent);
            finish();
        }else {
            finish();
        }
    }

    /**
     * 删除联系人
     */
    public void deletePerson() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("是否删除 " + new_name.getHint() + " ？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                editor.delete(Long.parseLong(number_info_ed.getHint().toString()));
                resolver.deleteContact(new_name.getHint().toString());
                Toast.makeText(NewPerson.this,"已删除",Toast.LENGTH_SHORT).show();
                MyPhone.actionStart(NewPerson.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    /**
     * 添加修改联系人
     */
    public void revisePerson() {
        if (delete.getVisibility() == View.GONE) {//添加
            if (new_name.getText().toString().length() == 0 || new_number.getText().toString().length() == 0) {
                Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            } else {
//                editor.add(number_info_ed.getText().toString(), name_info_ed.getText().toString());
                resolver.addContact(new_number.getText().toString(),new_name.getText().toString());
                Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {//修改
            //名字和号码都未修改
            if (new_name.getText().toString().length() == 0 & new_number.getText().toString().length() == 0) {
                Toast.makeText(this, "未修改", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("name",new_name.getHint().toString());
                intent.putExtra("number",new_number.getHint().toString());
                setResult(1,intent);
                finish();
            } else {
                if (new_name.getText().toString().length() == 0) {//仅名字未修改
//                    editor.edit(Long.parseLong(number_info_ed.getHint().toString()),
//                            Long.parseLong(number_info_ed.getText().toString()),
//                            name_info_ed.getHint().toString());
                    resolver.upDateContact(new_name.getHint().toString(),
                            new_number.getText().toString(),
                            new_name.getHint().toString());
//                    PersonInfo.actionStart(this,Long.parseLong(number_info_ed.getText().toString()),name_info_ed.getHint().toString());
                    Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("name",new_name.getHint().toString());
                    intent.putExtra("number",new_number.getText().toString());
                    setResult(1,intent);
                    finish();
                } else {
                    if (new_number.getText().toString().length() == 0) {//仅号码未修改
//                        editor.edit(Long.parseLong(number_info_ed.getHint().toString()),
//                                Long.parseLong(number_info_ed.getHint().toString()),
//                                name_info_ed.getText().toString());
                        resolver.upDateContact(new_name.getHint().toString(),
                                new_number.getHint().toString(),
                                new_name.getText().toString());
//                        PersonInfo.actionStart(this,Long.parseLong(number_info_ed.getHint().toString()),name_info_ed.getText().toString());
                        Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("name",new_name.getText().toString());
                        intent.putExtra("number",new_number.getHint().toString());
                        setResult(1,intent);
                        finish();
                    } else {//都修改了
//                        editor.edit(Long.parseLong(number_info_ed.getHint().toString()),
//                                Long.parseLong(number_info_ed.getText().toString()),
//                                name_info_ed.getText().toString());
                        resolver.upDateContact(new_name.getHint().toString(),
                                new_number.getText().toString(),
                                new_name.getText().toString());
//                        PersonInfo.actionStart(this,Long.parseLong(number_info_ed.getText().toString()),name_info_ed.getText().toString());
                        Toast.makeText(this, "联系人已保存", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("name",new_name.getText().toString());
                        intent.putExtra("number",new_number.getText().toString());
                        setResult(1,intent);
                        finish();
                    }
                }
            }
        }
    }

//    /**
//     * 从PersonInfo点击编辑启动
//     * 回传修改后的数据
//     *
//     * @param context
//     * @param number
//     * @param name
//     * @param flag
//     */
//    public static void actionStart(Context context, long number, String name, Boolean flag) {
//        Intent intent = new Intent(context, NewPerson.class);
//        intent.putExtra("number", number);
//        intent.putExtra("name", name);
//        intent.putExtra("flag", flag);
//        context.startActivity(intent);
//    }

    /**
     * 从Myphone点击添加按钮启动
     *
     * @param context
     * @param flag
     */
    public static void actionStart(Context context, Boolean flag) {
        Intent intent = new Intent(context, NewPerson.class);
        intent.putExtra("flag", flag);
        context.startActivity(intent);
    }
}
