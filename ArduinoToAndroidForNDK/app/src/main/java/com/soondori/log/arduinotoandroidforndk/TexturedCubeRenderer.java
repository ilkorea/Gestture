package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TexturedCubeRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private MultiTexturedCube cube;

    public TexturedCubeRenderer(Context context) {
        this.context = context;
        Bitmap[]bitmap = new Bitmap[6];
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.up);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.down);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.left);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.right);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.forward);
        bitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.backward);
        cube = new MultiTexturedCube(bitmap);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background color to black ( rgba ).
        gl.glClearColor(0.5f, 0.5f, 0.0f, 0.5f);

        //gl.glViewport(0,0,2,2);

        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);

        // Depth buffer setup.
        gl.glClearDepthf(1.0f);

        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);

        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);

        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    float angleX, angleY;

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Replace the current matrix with the identity matrix
        gl.glLoadIdentity();

        // Translates 4 units into the screen.
        gl.glTranslatef(0, 0, -10);
        gl.glRotatef(20, 1, 0, 0); // 카메라를 향해 약간 기울여서 윗면이 보이도록 한다
        gl.glRotatef(angleX, 1, 0, 0);
        gl.glRotatef(angleY, 0, 1, 0);

        // Draw our scene.
        cube.draw(gl);

        //angle += 5;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);

        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);

        // Reset the projection matrix
        gl.glLoadIdentity();

        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,	1000.0f);

        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);

        // Reset the modelview matrix
        gl.glLoadIdentity();
    }
}