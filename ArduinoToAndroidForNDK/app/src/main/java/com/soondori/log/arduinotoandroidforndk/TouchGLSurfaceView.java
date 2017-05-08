package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ilk48 on 2017-05-09.
 */

public class TouchGLSurfaceView extends GLSurfaceView {
    private TexturedCubeRenderer renderer;

    public TouchGLSurfaceView(Context context) {
        super(context, null);
    }

    public TouchGLSurfaceView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TouchGLSurfaceView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs);
        setEGLContextClientVersion(2);
        setRenderer(new TexturedCubeRenderer(context));
    }

    float prevX, prevY, curX, curY;
    float angleY, angleX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.i("터치 이벤트", "터치 이벤트발생");
        int action = event.getAction();
        if(action==MotionEvent.ACTION_UP) {
            //prevX=prevY=curX=curY=angleX=angleY=0;
            prevX=prevY = 0;
        }else if(action==MotionEvent.ACTION_MOVE) {
            //Log.i("터치 이벤트", "Move 이벤트발생");
            if(prevX==0 && prevY==0) {
                prevX = event.getX();
                prevY = event.getY();
                return true;
            }
            curX = event.getX();
            curY = event.getY();
            if(curX - prevX > 0) {
                this.renderer.angleY=(angleY+=5);
            }else if(curX - prevX < 0) {
                this.renderer.angleY=(angleY-=5);
            }/*
			if(curY - prevY >0) {
				this.renderer.angleX=(angleX+=2);
			}else if(curY - prevY <0) {// 위로
				this.renderer.angleX=(angleX-=2);
			}*/
        }
        return true;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.renderer = (TexturedCubeRenderer)renderer;
    }
}