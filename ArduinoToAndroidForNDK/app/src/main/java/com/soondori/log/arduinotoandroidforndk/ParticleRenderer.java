package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ilk48 on 2017-05-08.
 */

public class ParticleRenderer implements GLSurfaceView.Renderer
{
    Context mContext;
    private Triangle mTriangle;

    public static int loadShader(int type, String shaderCode){

        // 다음 2가지 타입 중 하나로 shader객체를 생성한다.
        // vertex shader type (GLES20.GL_VERTEX_SHADER)
        // 또는 fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // shader객체에 shader source code를 로드합니다.
        GLES20.glShaderSource(shader, shaderCode);

        //shader객체를 컴파일 합니다.
        GLES20.glCompileShader(shader);

        return shader;
    }

    public ParticleRenderer(Context context)
    {
        mContext = context;
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        //glClearColor에서 설정한 값으로 color buffer를 클리어합니다.
        //glClear메소드를 사용하여 클리어할 수 있는 버퍼는 다음 3가지 입니다.
        //Color buffer (GL_COLOR_BUFFER_BIT)
        //depth buffer (GL_DEPTH_BUFFER_BIT)
        //stencil buffer (GL_STENCIL_BUFFER_BIT)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        //viewport를 설정합니다.
        //specifies the affine transformation of x and y from
        //normalized device coordinates to window coordinates
        //viewport rectangle의 왼쪽 아래를 (0,0)으로 지정하고
        //viewport의 width와 height를 지정합니다.
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        //shape가 정의된 Triangle 클래스의 인스턴스를 생성합니다.
        mTriangle = new Triangle();

        //color buffer를 클리어할 때 사용할 색을 지정합니다.
        //red, green, blue, alpha 순으로 0~1사이의 값을 지정합니다.
        //여기에서는 검은색으로 지정하고 있습니다.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }
}