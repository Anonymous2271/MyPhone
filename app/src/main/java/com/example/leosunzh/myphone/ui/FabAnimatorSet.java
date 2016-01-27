package com.example.leosunzh.myphone.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by leosunzh on 2015/12/24.
 * 列表滑动产生的动画
 */
public class FabAnimatorSet implements View.OnTouchListener{
    float currentY;
    float lastY;
    //滑动方向,下滑为真
    Boolean lastDirection;
    Boolean currentDirection;

    int touchSlop = 10;

    AnimatorSet backAnimatorSet;
    AnimatorSet hideAnimatorSet;

    FloatingActionButton b;
    ListView listView;

    public void FabAnimatorSet(ListView listView, FloatingActionButton b){
        listView.setOnTouchListener(this);
        this.b = b;
        this.listView = listView;
        //创建显示动画
        backAnimatorSet = new AnimatorSet();
        backAnimatorSet.setDuration(300);

        //创建隐藏动画
        hideAnimatorSet = new AnimatorSet();
        hideAnimatorSet.setDuration(300);

    }

    //显示
    private void back(){
        ObjectAnimator fabBack = ObjectAnimator.ofFloat(b,"translationY",b.getTranslationY(),0f);
        ArrayList<Animator> back = new ArrayList<>();
        back.add(fabBack);
        backAnimatorSet.playTogether(back);
        backAnimatorSet.start();
    }
    //隐藏
    private void hide(){
        ObjectAnimator fabHide = ObjectAnimator.ofFloat(b,"translationY",b.getTranslationY(),b.getHeight()+100);
        ArrayList<Animator> hide = new ArrayList<>();
        hide.add(fabHide);
        hideAnimatorSet.playTogether(hide);
        hideAnimatorSet.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float tempCurrentY = event.getY();
                if(Math.abs(tempCurrentY - lastY)>touchSlop){//判定滑动
                    currentY = tempCurrentY;
                    currentDirection = currentY > lastY;//是否下滑
                    if(currentDirection!=lastDirection){
                        if(currentDirection){//下滑
                            back();
                        }else {//上滑
                            hide();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                lastDirection = currentDirection;
                break;
        }
        return false;
    }
}
