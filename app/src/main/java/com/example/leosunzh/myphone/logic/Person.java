package com.example.leosunzh.myphone.logic;


/**
 * 联系人实例类
 * Created by leosunzh on 2015/12/12.
 */
public class Person {
    String id;
    String number;
    String name;
    String pinyin;//名字拼音，用于快速索引
    String color;//没有头像时随机的颜色
    Boolean isFavorite;//是否被收藏

    public Person(String id,String name,String number){
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public Person(String id,String number,String name,String pinyin){
        this.id = id;
        this.number = number;
        this.name = name;
        this.pinyin = pinyin;
    }


    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getPinYin() {
        return pinyin;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
