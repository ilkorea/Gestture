package com.soondori.log.ardoinotoandroid;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Log on 2017-05-05.
 */

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(3);
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
