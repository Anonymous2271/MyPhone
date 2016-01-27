package com.example.leosunzh.myphone.logic;

import android.text.Editable;

import com.example.leosunzh.myphone.logic.CallLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找模块逻辑操作类
 * Created by leosunzh on 2015/12/21.
 */
public class PinYinAndNumSearch{

//    List<?>list;
//    List<String> pinyin;

//    final static String []py_node = {"^[A-Z]+","^[A-Z]+","^[A-C]+","^[D-F]+","^[G-I]+","^[J-L]+","^[M-O]+","^[P-S]+","^[T-V]+","^[W-Z]+","^[*]+","^[#]+"};

    public PinYinAndNumSearch(){
    }

//    public void whenCallListChange(List<CallLog>list){
////        this.list = list;
//        pinyin = new ArrayList<>();
//        for (int i=0;i<list.size();i++){
//            pinyin.add(list.get(i).getTime());
//        }
//    }


    //拨号盘按号码查找
    public List<CallLog> searchForCall(List<Person>list,Editable s) {
        List<CallLog> newList = new ArrayList<>();
        String ss = s.toString();
            for (int i=0;i<list.size();i++) {
                Person person =  list.get(i);
                if (person.getNumber().contains(ss)) {
                    newList.add(new CallLog(person.getName(),person.getNumber(),"person","",""));
                }
            }
        return newList;
    }
    //搜索活动查找
    public List<Person> searchForPerson(List<Person>list,Editable s) {
        List<Person> newList = new ArrayList<>();
        String ss = s.toString();
        for (int i = 0; i < list.size(); i++) {
            Person person = list.get(i);
            if(ss.matches("^[0-9]+")) {//号码
                if (person.getNumber().contains(ss)) {
                    newList.add(person);
                }
            }else {//名字
                if(person.getName().contains(ss)){
                    newList.add(person);
                }
            }
        }
        return newList;
    }


//
//    /**
//     * T9拼音查找
//     */
//    public List<CallLog> searchForPin(Editable s, int start,int count){
//        int the = Integer.parseInt(s.subSequence(start,count).toString());
//        List<CallLog> personList =  list;
//        switch (the){
//            case 2:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[2]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 3:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[3]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 4:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[4]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 5:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[5]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 6:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[6]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 7:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[7]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 8:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[8]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            case 9:
//                for (int i=0;i<pinyin.size();i++) {
//                    if (pinyin.get(i).matches(py_node[9]) == false) {
//                        personList.remove(i);
//                        pinyin.remove(i);
//                    }
//                }break;
//            default:break;
//        }
//        return personList;
//    }
}
