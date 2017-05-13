package com.soondori.log.arduinotoandroidforndk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TexturedCubeRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private MultiTexturedCube cube;
    float angleX, angleY, angleZ, depth;
    // These still work without volatile, but refreshes are not guaranteed to happen.
    public volatile float mDeltaX;
    public volatile float mDeltaY;

    static {
        System.loadLibrary("native-lib");
    }
    private native String saveBitmap(Bitmap bitmap);

    public TexturedCubeRenderer(Context context) {
        this.context = context;
        depth = 0;
        Bitmap[] bitmap = new Bitmap[6];
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.psj);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hy_cycle);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.nougat);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.hy_text);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sul);
        bitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluetooth);
        Log.d("Soondori","test");
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

    /** This is a handle to our cube shading program. */
    private int mProgramHandle;
    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;
    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;
    /** This will be used to pass in the light position. */
    private int mLightPosHandle;
    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;
    /** This will be used to pass in model position information. */
    private int mPositionHandle;
    /** This will be used to pass in model normal information. */
    private int mNormalHandle;
    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;
    /** Stores a copy of the model matrix specifically for the light position. */
    private float[] mLightModelMatrix = new float[16];
    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    private final float[] mLightPosInWorldSpace = new float[4];
    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    private final float[] mLightPosInEyeSpace = new float[4];
    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];
    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];
    /** Store the current rotation. */
    private final float[] mCurrentRotation = new float[16];
    /** A temporary matrix. */
    private float[] mTemporaryMatrix = new float[16];
    /** Store the accumulated rotation. */
    private final float[] mAccumulatedRotation = new float[16];
    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];
    /** These are handles to our texture data. */
    private int mAndroidDataHandle;
    /** The current cubes object. */
    private Cubes mCubes;

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // Replace the current matrix with the identity matrix
        gl.glLoadIdentity();
        // Translates 4 units into the screen.
        gl.glTranslatef(0, depth, -10);

        gl.glRotatef(angleX, 1, 0, 0);
        gl.glRotatef(angleY, 0, 1, 0);
        gl.glRotatef(angleZ, 0, 0, 1);
        // Draw our scene.
        cube.draw(gl);

        //angle += 5;

//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
//
//        // Set our per-vertex lighting program.
//        GLES20.glUseProgram(mProgramHandle);
//
//        // Set program handles for cube drawing.
//        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
//        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix");
//        mLightPosHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_LightPos");
//        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
//        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
//        mNormalHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Normal");
//        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
//
//        // Calculate position of the light. Push into the distance.
//        android.opengl.Matrix.setIdentityM(mLightModelMatrix, 0);
//        android.opengl.Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -1.0f);
//
//        android.opengl.Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
//        android.opengl.Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
//
//        // Draw a cube.
//        // Translate the cube into the screen.
//        android.opengl.Matrix.setIdentityM(mModelMatrix, 0);
//        android.opengl.Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -3.5f);
//
//        // Set a matrix that contains the current rotation.
//        android.opengl.Matrix.setIdentityM(mCurrentRotation, 0);
//        android.opengl.Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
//        android.opengl.Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
//        mDeltaX = 0.0f;
//        mDeltaY = 0.0f;
//
//        // Multiply the current rotation by the accumulated rotation, and then set the accumulated rotation to the result.
//        android.opengl.Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
//        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
//
//        // Rotate the cube taking the overall rotation into account.
//        android.opengl.Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
//        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);
//
//        // This multiplies the view matrix by the model matrix, and stores
//        // the result in the MVP matrix
//        // (which currently contains model * view).
//        android.opengl.Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//        // Pass in the modelview matrix.
//        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // This multiplies the modelview matrix by the projection matrix,
//        // and stores the result in the MVP matrix
//        // (which now contains model * view * projection).
//        android.opengl.Matrix.multiplyMM(mTemporaryMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//        System.arraycopy(mTemporaryMatrix, 0, mMVPMatrix, 0, 16);
//
//        // Pass in the combined matrix.
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
//
//        // Pass in the light position in eye space.
//        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
//
//        // Pass in the texture information
//        // Set the active texture unit to texture unit 0.
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//
//        // Bind the texture to this unit.
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mAndroidDataHandle);
//
//        // Tell the texture uniform sampler to use this texture in the
//        // shader by binding to texture unit 0.
//        GLES20.glUniform1i(mTextureUniformHandle, 0);
//
//        if (mCubes != null) {
//            mCubes.render();
//        }
    }

    /** How many bytes per float. */
    static final int BYTES_PER_FLOAT = 4;
    /** Size of the position data in elements. */
    static final int POSITION_DATA_SIZE = 3;
    /** Size of the normal data in elements. */
    static final int NORMAL_DATA_SIZE = 3;
    /** Size of the texture coordinate data in elements. */
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;

    abstract class Cubes {
        abstract void render();

        abstract void release();

        FloatBuffer[] getBuffers(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            // First, copy cube information into client-side floating point buffers.
            final FloatBuffer cubePositionsBuffer;
            final FloatBuffer cubeNormalsBuffer;
            final FloatBuffer cubeTextureCoordinatesBuffer;

            cubePositionsBuffer = ByteBuffer.allocateDirect(cubePositions.length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            cubePositionsBuffer.put(cubePositions).position(0);

            cubeNormalsBuffer = ByteBuffer.allocateDirect(cubeNormals.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
                cubeNormalsBuffer.put(cubeNormals);
            }

            cubeNormalsBuffer.position(0);

            cubeTextureCoordinatesBuffer = ByteBuffer.allocateDirect(cubeTextureCoordinates.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor * generatedCubeFactor); i++) {
                cubeTextureCoordinatesBuffer.put(cubeTextureCoordinates);
            }

            cubeTextureCoordinatesBuffer.position(0);

            return new FloatBuffer[] {cubePositionsBuffer, cubeNormalsBuffer, cubeTextureCoordinatesBuffer};
        }

        FloatBuffer getInterleavedBuffer(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
            final int cubeDataLength = cubePositions.length
                    + (cubeNormals.length * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor)
                    + (cubeTextureCoordinates.length * generatedCubeFactor * generatedCubeFactor * generatedCubeFactor);
            int cubePositionOffset = 0;
            int cubeNormalOffset = 0;
            int cubeTextureOffset = 0;

            final FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(cubeDataLength * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();

            for (int i = 0; i < generatedCubeFactor * generatedCubeFactor * generatedCubeFactor; i++) {
                for (int v = 0; v < 36; v++) {
                    cubeBuffer.put(cubePositions, cubePositionOffset, POSITION_DATA_SIZE);
                    cubePositionOffset += POSITION_DATA_SIZE;
                    cubeBuffer.put(cubeNormals, cubeNormalOffset, NORMAL_DATA_SIZE);
                    cubeNormalOffset += NORMAL_DATA_SIZE;
                    cubeBuffer.put(cubeTextureCoordinates, cubeTextureOffset, TEXTURE_COORDINATE_DATA_SIZE);
                    cubeTextureOffset += TEXTURE_COORDINATE_DATA_SIZE;
                }

                // The normal and texture data is repeated for each cube.
                cubeNormalOffset = 0;
                cubeTextureOffset = 0;
            }

            cubeBuffer.position(0);

            return cubeBuffer;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        TouchGLSurfaceView.iWindowHeight = height;
        TouchGLSurfaceView.iWindowWidth = width;

        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);
        //gl.glViewport(0, 0, 200, 200);

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