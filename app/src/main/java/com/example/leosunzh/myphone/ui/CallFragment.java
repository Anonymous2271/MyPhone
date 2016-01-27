package com.example.leosunzh.myphone.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.CallLog;
import com.example.leosunzh.myphone.logic.MyResolver;
import com.example.leosunzh.myphone.logic.Person;
import com.example.leosunzh.myphone.logic.PinYinAndNumSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 拨号界面
 * Created by leosunzh on 2015/12/12.
 */
public class CallFragment extends Fragment implements View.OnClickListener,View.OnLongClickListener,
        AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener,TextWatcher{
    private static final String ARG_POSITION = "position";

    List<CallLog> list;
    List<Person> consult;//拨号查找参考
    ListView logs_lv;
    CallLogListViewAdapter callLogListViewAdapter;
    //拨号盘
    RelativeLayout call_layout;
    FloatingActionButton up_layout_fab;
    //输入栏
    EditText input_ed_bar;
    //MyPhone
    MyPhone myPhone;

    MyResolver myResolver;
    PinYinAndNumSearch pinYinAndNumSearch;

    public static CallFragment newInstance(int position) {
        CallFragment f = new CallFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initData
        list = new ArrayList<>();
        consult = new ArrayList<>();
        myResolver = new MyResolver(getActivity());
        pinYinAndNumSearch = new PinYinAndNumSearch();
        callLogListViewAdapter = new CallLogListViewAdapter(getActivity(), R.layout.call_logs_item_layout,list);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_call_layout,container,false);
        //拨号盘
        rootView.findViewById(R.id.b1).setOnClickListener(this);
        rootView.findViewById(R.id.b2).setOnClickListener(this);
        rootView.findViewById(R.id.b3).setOnClickListener(this);
        rootView.findViewById(R.id.b4).setOnClickListener(this);
        rootView.findViewById(R.id.b5).setOnClickListener(this);
        rootView.findViewById(R.id.b6).setOnClickListener(this);
        rootView.findViewById(R.id.b7).setOnClickListener(this);
        rootView.findViewById(R.id.b8).setOnClickListener(this);
        rootView.findViewById(R.id.b9).setOnClickListener(this);
        rootView.findViewById(R.id.b0).setOnClickListener(this);
        rootView.findViewById(R.id.b_xing).setOnClickListener(this);
        rootView.findViewById(R.id.b_jing).setOnClickListener(this);
        rootView.findViewById(R.id.call_b).setOnClickListener(this);
        rootView.findViewById(R.id.back_b).setOnClickListener(this);
        rootView.findViewById(R.id.back_b).setOnLongClickListener(this);
        rootView.findViewById(R.id.down_b).setOnClickListener(this);
        up_layout_fab = (FloatingActionButton) rootView.findViewById(R.id.up_fab);
        up_layout_fab.setOnClickListener(this);
        call_layout = (RelativeLayout) rootView.findViewById(R.id.call_layout);
        //输入栏
        input_ed_bar = (EditText) rootView.findViewById(R.id.input_ed_bar);
        input_ed_bar.addTextChangedListener(this);
        input_ed_bar.setEnabled(false);
        //通话记录
        logs_lv = (ListView)rootView.findViewById(R.id.call_jilu_LV);
        logs_lv.setOnItemClickListener(this);
        logs_lv.setOnItemLongClickListener(this);
        logs_lv.setAdapter(callLogListViewAdapter);
        //fab动画
        new FabAnimatorSet().FabAnimatorSet(logs_lv,up_layout_fab);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myPhone = (MyPhone) getActivity();
    }

    /**
     * 可见时刷新
     */
    public void onStart() {
        super.onStart();
        call_layout.setVisibility(View.GONE);
        up_layout_fab.setVisibility(View.VISIBLE);
        input_ed_bar.setText(null);
        consult.clear();
        consult.addAll(myResolver.readContacts(null));
        reFlesh(myResolver.readCallLog(null));
    }

    /**
     * 刷新listView
     */
    public void reFlesh(List<CallLog> newList){
        this.list.clear();
        this.list.addAll(newList);
        callLogListViewAdapter.notifyDataSetChanged();
    }

    /**
     * 点击
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b1:input_ed_bar.setText(input_ed_bar.getText()+"1");
                new ToneTools(getActivity()).playTone(1);
                 break;
            case R.id.b2:input_ed_bar.setText(input_ed_bar.getText()+"2");
                new ToneTools(getActivity()).playTone(2);
                break;
            case R.id.b3:input_ed_bar.setText(input_ed_bar.getText()+"3");
                new ToneTools(getActivity()).playTone(3);
                break;
            case R.id.b4:input_ed_bar.setText(input_ed_bar.getText()+"4");
                new ToneTools(getActivity()).playTone(4);
                break;
            case R.id.b5:input_ed_bar.setText(input_ed_bar.getText()+"5");
                new ToneTools(getActivity()).playTone(5);
                break;
            case R.id.b6:input_ed_bar.setText(input_ed_bar.getText()+"6");
                new ToneTools(getActivity()).playTone(6);
                break;
            case R.id.b7:input_ed_bar.setText(input_ed_bar.getText()+"7");
                new ToneTools(getActivity()).playTone(7);
                break;
            case R.id.b8:input_ed_bar.setText(input_ed_bar.getText()+"8");
                new ToneTools(getActivity()).playTone(8);
                break;
            case R.id.b9:input_ed_bar.setText(input_ed_bar.getText()+"9");
                new ToneTools(getActivity()).playTone(9);
                break;
            case R.id.b0:input_ed_bar.setText(input_ed_bar.getText()+"0");
                new ToneTools(getActivity()).playTone(0);
                break;
            case R.id.b_xing:input_ed_bar.setText(input_ed_bar.getText()+"*");
                new ToneTools(getActivity()).playTone(10);
                break;
            case R.id.b_jing:input_ed_bar.setText(input_ed_bar.getText()+"#");
                new ToneTools(getActivity()).playTone(11);
                break;
            case R.id.back_b:
                try {
                    input_ed_bar.setText(input_ed_bar.getText().toString().substring(0, input_ed_bar.getText().toString().length() - 1));
                }catch (Exception e){
                    input_ed_bar.setText(null);
                }
                break;
            case R.id.call_b:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+input_ed_bar.getText()));
                startActivity(intent);
                break;
            case R.id.down_b:
                input_ed_bar.setVisibility(View.GONE);
                call_layout.setVisibility(View.GONE);
                up_layout_fab.setVisibility(View.VISIBLE);
                myPhone.title_layout.setVisibility(View.VISIBLE);
                myPhone.tabLayout.setVisibility(View.VISIBLE);
                input_ed_bar.setText(null);//刷新列表
                reFlesh(myResolver.readCallLog(null));
                break;
            case R.id.up_fab:
                input_ed_bar.setVisibility(View.VISIBLE);
                call_layout.setVisibility(View.VISIBLE);
                up_layout_fab.setVisibility(View.GONE);
                myPhone.title_layout.setVisibility(View.GONE);
                myPhone.tabLayout.setVisibility(View.GONE);
                reFlesh(changeListUI(true));
                break;
        }
        //将光标移到最后
        input_ed_bar.setSelection(input_ed_bar.getText().length());
    }

    /**
     * 按下返回键时的操作，在主activity里调用
     * @return
     */
    public boolean onBackClick(){
        if(call_layout.getVisibility()==View.VISIBLE){
            input_ed_bar.setVisibility(View.GONE);
            call_layout.setVisibility(View.GONE);
            up_layout_fab.setVisibility(View.VISIBLE);
            myPhone.title_layout.setVisibility(View.VISIBLE);
            myPhone.tabLayout.setVisibility(View.VISIBLE);
            input_ed_bar.setText(null);//刷新列表
            reFlesh(myResolver.readCallLog(null));
            return false;
        }else {
            return true;
        }
    }

    /**
     * 长按拨号盘回退键
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()){
            case R.id.back_b:
                input_ed_bar.setText(null);break;
        }
        return false;
    }


    /**
     * 点击单条通话记录
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final CallLog callLog = (CallLog) logs_lv.getItemAtPosition(position);
        if(input_ed_bar.getVisibility()==View.GONE) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(myPhone);
            dialog.setTitle(callLog.getType());
            dialog.setMessage("姓名："+callLog.getName()+"\n"+"号码："+ callLog.getNumber()+"\n"
                    +"时长："+callLog.getDuration()+"s"+"\n"+"时间："+callLog.getTime());
            dialog.setPositiveButton("短信", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent_message = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + callLog.getNumber()));
                    startActivity(intent_message);
                }
            });
            dialog.setNegativeButton("电话", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent_call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callLog.getNumber()));
                    startActivity(intent_call);
                }
            });
            dialog.show();
        }else {
            input_ed_bar.setText(callLog.getNumber());
        }
    }

    /**
     *点击拨号盘由显示通话记录变为显示联系人
     * @return List<CallLog>
     */
    public List<CallLog> changeListUI(Boolean is){
        if(is){
            List<CallLog> newList = new ArrayList<>();
            for (int i=0;i<consult.size();i++){
                Person person = consult.get(i);
                newList.add(new CallLog(person.getName(),person.getNumber(),"person","",""));
            }
            return newList;
        }else {
            return list;
        }
    }


    /**
     * 输入框改变
     * @param s
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
            reFlesh(pinYinAndNumSearch.searchForCall(consult,s));
    }

    /**
     * 删除指定通话记录
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示:");
        builder.setMessage("删除此条通话记录?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CallLog callLog = (CallLog) logs_lv.getItemAtPosition(position);
                myResolver.deleteCallLog(callLog.getId());
                reFlesh(myResolver.readCallLog(null));
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
        return false;
    }
}
