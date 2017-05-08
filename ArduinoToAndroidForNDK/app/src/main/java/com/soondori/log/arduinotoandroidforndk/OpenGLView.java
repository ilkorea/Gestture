package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by ilk48 on 2017-05-08.
 */

public class OpenGLView extends GLSurfaceView
{

    //programmatic instantiation
    public OpenGLView(Context context)
    {
        this(context, null);
    }

    //XML inflation/instantiation
    public OpenGLView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public OpenGLView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);

        // Tell EGL to use a ES 2.0 Context
        setEGLContextClientVersion(2);

        // Set the renderer
        setRenderer(new ParticleRenderer(context));
    }

}