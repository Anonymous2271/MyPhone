package com.example.leosunzh.myphone.logic;

/**
 * 通话记录实例类
 * Created by leosunzh on 2015/12/15.
 */
public class CallLog {
    String id;
    String number;
    String name;
    String type;
    String time;
    String duration;
    public CallLog(String id,String name, String number, String type, String time,String duration){
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.time = time;
        this.duration = duration;
    }

    public CallLog(String name, String number, String type, String time,String duration){
        this.name = name;
        this.number = number;
        this.type = type;
        this.time = time;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }
}
