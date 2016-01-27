package com.example.leosunzh.myphone.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leosunzh.myphone.R;
import com.example.leosunzh.myphone.logic.AlphabetSearch;
import com.example.leosunzh.myphone.logic.MyResolver;
import com.example.leosunzh.myphone.logic.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表界面
 * Created by leosunzh on 2015/12/12.
 */

public class PersonFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private static final String ARG_POSITION = "position";
//    private int position;//页标
    MyResolver myResolver;

    int index = 0;//记住listView当前的位置
    List<Person> list;
    ListView listView;
    MyListViewAdapter myListViewAdapter;
    FloatingActionButton add_fab;

    LinearLayout alphabetLayout;//字母索引侧边条
    TextView current;//显示当前触摸的字母
    AlphabetSearch alphabetSearch;//索引布局Touch处理类

    public static PersonFragment newInstance(int position) {
        PersonFragment f = new PersonFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }


    /**
     * 初始化页标和数据操作对象
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myResolver = new MyResolver(getActivity());
        list = new ArrayList<>();
        myListViewAdapter = new MyListViewAdapter(getActivity(), R.layout.list_item_layout,list);
//        position = getArguments().getInt(ARG_POSITION);
    }

    /**
     * 绘制联系人碎片View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_persons_layout, container, false);

        add_fab = (FloatingActionButton) rootView.findViewById(R.id.add_fab);
        add_fab.setOnClickListener(this);
        listView = (ListView) rootView.findViewById(R.id.persons_list);
        listView.setOnItemClickListener(this);
        alphabetLayout = (LinearLayout) rootView.findViewById(R.id.alphabetLayout);
        current = (TextView) rootView.findViewById(R.id.current);
//        query_ET = (EditText) rootView.findViewById(R.id.query_ET);

//        list = myResolver.readContacts(null);//在onStart里面赋值，减少读取次数
        listView.setAdapter(myListViewAdapter);
        //添加字母侧栏索引
        alphabetSearch = new AlphabetSearch(alphabetLayout,listView,current);
        //添加fab_add动画
        new FabAnimatorSet().FabAnimatorSet(listView,add_fab);
        return rootView;
    }


    /**
     * 可见时刷新ListView
     * 刷新搜索框
     */
    @Override
    public void onStart() {
        super.onStart();
        List<Person> newList = myResolver.readContacts(null);
        reFlesh(newList);
        listView.setSelection(index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        index = this.listView.getFirstVisiblePosition();
    }

    /**
     * 刷新ListView的数据
     */
    public void reFlesh(List<Person> newList){
        this.list.clear();
        this.list.addAll(newList);
        myListViewAdapter.notifyDataSetChanged();
        alphabetSearch.whenListChange(newList);
    }

    /**
     * 点击ListView中的某个联系人Item
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Person person = (Person) listView.getItemAtPosition(position);
        PersonInfoActivity.actionStart(getActivity(),person.getId(),person.getNumber(),person.getName(),person.getColor());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_fab:
                NewPerson.actionStart(getActivity(),false);
        }
    }
}