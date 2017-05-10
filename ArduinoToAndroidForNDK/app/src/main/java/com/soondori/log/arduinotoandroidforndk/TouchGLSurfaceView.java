package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class TouchGLSurfaceView extends GLSurfaceView {
    private TexturedCubeRenderer renderer;
    public static int iWindowWidth;
    public static int iWindowHeight;
    public static int iCubePosition = 0;
    float fSaveAngleX;
    float fSaveAngleY;


    public TouchGLSurfaceView(Context context) {
        super(context, null);
        //super(context);
    }

    public TouchGLSurfaceView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TouchGLSurfaceView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs);
        setEGLContextClientVersion(2);
        setRenderer(new TexturedCubeRenderer(context));
        fSaveAngleX = this.renderer.angleX;
        fSaveAngleY = this.renderer.angleY;
    }

    float prevX, prevY, curX, curY;
    float angleY, angleX;
    float Xvalue, Yvalue, Zvalue;
    boolean boEventFlag = false;
    boolean boTakeoffFlag = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        //Log.i("터치 이벤트", "터치 이벤트발생");
//        int action = event.getAction();
//        if(action==MotionEvent.ACTION_UP) {
//            //prevX=prevY=curX=curY=angleX=angleY=0;
//            prevX=prevY = 0;
//        }else if(action==MotionEvent.ACTION_MOVE) {
//            if(boTakeoffFlag){
//                boTakeoffFlag = false;
//            }
//
//            //Log.i("터치 이벤트", "Move 이벤트발생");
//            if(prevX==0 && prevY==0) {
//                prevX = event.getX();
//                prevY = event.getY();
//                return true;
//            }
//            curX = event.getX();
//            curY = event.getY();
//            if(curX - prevX > 0) {
//                this.renderer.angleY=(angleY+=5);
//            }else if(curX - prevX < 0) {
//                this.renderer.angleY=(angleY-=5);
//            }/*
//			if(curY - prevY >0) {
//				this.renderer.angleX=(angleX+=2);
//			}else if(curY - prevY <0) {// 위로
//				this.renderer.angleX=(angleX-=2);
//			}*/
//        }
        return true;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.renderer = (TexturedCubeRenderer)renderer;
    }
}