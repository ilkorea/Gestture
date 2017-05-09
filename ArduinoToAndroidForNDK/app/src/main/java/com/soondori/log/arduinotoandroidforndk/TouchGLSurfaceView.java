package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float fXunit = iWindowWidth / 90;
        float fYunit = iWindowHeight / 90;

        //Log.i("터치 이벤트", "터치 이벤트발생");
        int action = event.getAction();
        if(action==MotionEvent.ACTION_UP) {
            //prevX=prevY=curX=curY=angleX=angleY=0;
            prevX=prevY = 0;
            fSaveAngleX = this.renderer.angleX;
            fSaveAngleY = this.renderer.angleY;

            switch (iCubePosition){
                case 0:
                    if(Yvalue > 60) {
                        for (int i = (int) Yvalue; i < 90; i++)
                            this.renderer.angleY++;
                        Yvalue = 0;
                        iCubePosition = 3;
                    }else if((Yvalue <= 60) && (Yvalue > 0)){
                        for (int i = (int) Yvalue; i > 0; i--)
                            this.renderer.angleY--;
                        Yvalue = 0;
                        iCubePosition = 0;
                    }else if (Yvalue <= -60){
                        for (int i = (int) Yvalue; i < -90; i--)
                            this.renderer.angleY--;
                        Yvalue = 0;
                        iCubePosition = 1;
                    }else if ((Yvalue > -60) && (Yvalue < 0)){
                        for (int i = (int) Yvalue; i < 0; i++)
                            this.renderer.angleY++;
                        Yvalue = 0;
                        iCubePosition = 0;
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }else if(action==MotionEvent.ACTION_MOVE) {
            //Log.i("터치 이벤트", "Move 이벤트발생");
            if(prevX==0 && prevY==0) {
                prevX = event.getX();
                prevY = event.getY();
                return true;
            }
            curX = event.getX();
            curY = event.getY();

            switch (iCubePosition){
                case 0:
                    if((curX - prevX) > (curY - prevY)){
                        if(curX - prevX > 0) {
                            this.renderer.angleY = fSaveAngleX + (curX / fXunit);
                            Yvalue = (curX / fXunit);
                            return true;
                        }
                        else if(curX - prevX < 0) {
                            this.renderer.angleY = fSaveAngleX - ((iWindowWidth - curX) / fXunit);
                            Yvalue = ((iWindowWidth - curX) / fXunit) * -1;
                            return true;
                        }
                    }else{
                        if(curY - prevY > 0){
                            this.renderer.angleX = fSaveAngleY + (curY / fYunit);
                            Xvalue = (curY / fYunit);
                            return true;
                        }
                        else if(curY - prevY < 0){// 위로
                            this.renderer.angleX = fSaveAngleY - ((iWindowHeight - curY) / fYunit);
                            Xvalue = ((iWindowHeight - curY) / fYunit) * -1;
                            return true;
                        }
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    if((curX - prevX) > (curY - prevY)){
                        if(curX - prevX > 0) {
                            this.renderer.angleY = fSaveAngleX + (curX / fXunit);
                            Yvalue = (curX / fXunit);
                            return true;
                        }
                        else if(curX - prevX < -0) {
                            this.renderer.angleY = fSaveAngleX - ((iWindowWidth - curX) / fXunit);
                            Yvalue = ((iWindowWidth - curX) / fXunit) * -1;
                            return true;
                        }
                    }else{
                        if(curY - prevY > 0){
                            this.renderer.angleZ = fSaveAngleY + (curY / fYunit);
                            Xvalue = (curY / fYunit);
                            return true;
                        }
                        else if(curY - prevY < -0){// 위로
                            this.renderer.angleZ = fSaveAngleY - ((iWindowHeight - curY) / fYunit);
                            Xvalue = ((iWindowHeight - curY) / fYunit) * -1;
                            return true;
                        }
                    }
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }

        }
        return true;
    }



    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.renderer = (TexturedCubeRenderer)renderer;
    }
}