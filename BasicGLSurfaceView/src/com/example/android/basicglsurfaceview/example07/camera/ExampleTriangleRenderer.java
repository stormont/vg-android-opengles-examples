/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.basicglsurfaceview.example07.camera;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.android.basicglsurfaceview.R;
import com.voyagegames.core.android.opengles.TextureFactory;
import com.voyagegames.core.android.opengles.buffers.CubeBuffer;
import com.voyagegames.core.android.opengles.buffers.TexturedNormalTriangleBuffer;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.interfaces.IScene;
import com.voyagegames.core.android.opengles.interfaces.IShaderSet;
import com.voyagegames.core.android.opengles.modules.Camera;
import com.voyagegames.core.android.opengles.modules.Frustum;
import com.voyagegames.core.android.opengles.modules.LookAt;
import com.voyagegames.core.android.opengles.modules.Texture;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public class ExampleTriangleRenderer implements Renderer {
    
    private final float[] TEMP_VERTICES_DATA_FRONT = {
			// Front face         // Front face          // Front face
			-1.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f,      0.0f, 0.0f, 				
			-1.0f, -1.0f, 1.0f,   0.0f, 0.0f, 1.0f,	     0.0f, 1.0f,
			 1.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f,	     1.0f, 0.0f,
			 1.0f, -1.0f, 1.0f,   0.0f, 0.0f, 1.0f,	     1.0f, 1.0f
    };

    private final float[] TEMP_VERTICES_DATA_RIGHT = {
			// Right face	      // Right face 	     // Right face 
			1.0f, 1.0f, 1.0f,	1.0f, 0.0f, 0.0f,	0.0f, 0.0f, 				
			1.0f, -1.0f, 1.0f,    1.0f, 0.0f, 0.0f,	     0.0f, 1.0f,
			1.0f, 1.0f, -1.0f,    1.0f, 0.0f, 0.0f,	     1.0f, 0.0f,
			1.0f, -1.0f, -1.0f,   1.0f, 0.0f, 0.0f,	     1.0f, 1.0f
    };

    private final float[] TEMP_VERTICES_DATA_BACK = {
			// Back face	      // Back face 	     // Back face 
			1.0f, 1.0f, -1.0f,	0.0f, 0.0f, -1.0f,	0.0f, 0.0f, 				
			1.0f, -1.0f, -1.0f,   0.0f, 0.0f, -1.0f,     0.0f, 1.0f,
			-1.0f, 1.0f, -1.0f,   0.0f, 0.0f, -1.0f,     1.0f, 0.0f,
			-1.0f, -1.0f, -1.0f,  0.0f, 0.0f, -1.0f,     1.0f, 1.0f
    };

    private final float[] TEMP_VERTICES_DATA_LEFT = {
			// Left face	      // Left face 	     // Left face 
			-1.0f, 1.0f, -1.0f,	-1.0f, 0.0f, 0.0f,	0.0f, 0.0f, 				
			-1.0f, -1.0f, -1.0f,  -1.0f, 0.0f, 0.0f,     0.0f, 1.0f,
			-1.0f, 1.0f, 1.0f,    -1.0f, 0.0f, 0.0f,     1.0f, 0.0f,
			-1.0f, -1.0f, 1.0f,   -1.0f, 0.0f, 0.0f,     1.0f, 1.0f
    };

    private final float[] TEMP_VERTICES_DATA_TOP = {
			// Top face	      // Top face 	     // Top face 
			-1.0f, 1.0f, -1.0f,	0.0f, 1.0f, 0.0f,	0.0f, 0.0f, 				
			-1.0f, 1.0f, 1.0f,    0.0f, 1.0f, 0.0f,	     0.0f, 1.0f,
			1.0f, 1.0f, -1.0f,    0.0f, 1.0f, 0.0f,	     1.0f, 0.0f,
			1.0f, 1.0f, 1.0f,     0.0f, 1.0f, 0.0f,	     1.0f, 1.0f
    };

    private final float[] TEMP_VERTICES_DATA_BOTTOM = {
			// Bottom face	      // Bottom face 	     // Bottom face 
			1.0f, -1.0f, -1.0f,	0.0f, -1.0f, 0.0f,	0.0f, 0.0f, 				
			1.0f, -1.0f, 1.0f,    0.0f, -1.0f, 0.0f,     0.0f, 1.0f,
			-1.0f, -1.0f, -1.0f,  0.0f, -1.0f, 0.0f,     1.0f, 0.0f,
			-1.0f, -1.0f, 1.0f,   0.0f, -1.0f, 0.0f,     1.0f, 1.0f
    };

    private final Context mContext;
    private final IScene mScene;
    private final IRenderableBuffer mCube;
    private final IShaderSet mShaderSet;
    private final IShaderSet mPointShaderSet;

    private Texture mTexture;
    private int[] mVertexBufferObjects;

    public ExampleTriangleRenderer(final Context context) {
        mContext = context;
        mScene = new ExampleScene();
    	mShaderSet = new ExampleShaderSet();
    	mPointShaderSet = new ExamplePointShaderSet();
        mCube = new CubeBuffer(
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_FRONT, GLES20.GL_TRIANGLE_STRIP),
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_RIGHT, GLES20.GL_TRIANGLE_STRIP),
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_BACK, GLES20.GL_TRIANGLE_STRIP),
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_LEFT, GLES20.GL_TRIANGLE_STRIP),
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_TOP, GLES20.GL_TRIANGLE_STRIP),
        		new TexturedNormalTriangleBuffer(TEMP_VERTICES_DATA_BOTTOM, GLES20.GL_TRIANGLE_STRIP));
    }

    @Override
    public void onDrawFrame(final GL10 glUnused) {
    	mScene.clear();

    	
		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        
        final float[] lightPosInEyeSpace = new float[4];
        final float[] modelMatrix = new float[16];
        
        
        // Calculate position of the light. Rotate and then push into the distance.
        final float[] lightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
        final float[] lightPosInWorldSpace = new float[4];
        final float[] lightModelMatrix = new float[16];
        
        Matrix.setIdentityM(lightModelMatrix, 0);
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(lightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
               
        Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0);
        Matrix.multiplyMV(lightPosInEyeSpace, 0, mScene.camera().viewMatrix(), 0, lightPosInWorldSpace, 0);

        
        // Update camera to track light
        final LookAt lookAt = new LookAt(
        		new Vector3D(0, 0, -0.5f),
        		new Vector3D(lightPosInWorldSpace[0], lightPosInWorldSpace[1], lightPosInWorldSpace[2]),
        		new Vector3D(0, 1.0f, 0));
        
        mScene.setCamera(new Camera(lookAt, mScene.camera().frustum()));
        
        
        // Draw some cubes
        mShaderSet.prepareRender();
        
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 4.0f, 0.0f, -7.0f);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1.0f, 0, 0);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mCube, modelMatrix, lightPosInEyeSpace);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 1.0f, 0);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mCube, modelMatrix, lightPosInEyeSpace);
        
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 0, 1.0f);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mCube, modelMatrix, lightPosInEyeSpace);
        
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, -4.0f, -7.0f);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mCube, modelMatrix, lightPosInEyeSpace);
        
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 1.0f, 0);
        Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 0, 1.0f);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mCube, modelMatrix, lightPosInEyeSpace);

        
        // Draw the light
        mPointShaderSet.prepareRender();
        ((ExamplePointShaderSet)mPointShaderSet).render(mScene.camera(), lightPosInModelSpace, lightModelMatrix);
    }

    @Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height) {
        mScene.updateViewport(width, height);
        mCube.setHandles(mShaderSet.handles());
        ((ExampleShaderSet)mShaderSet).setTextureID(mTexture.textureID);
        
        mVertexBufferObjects = new int[6];
        GLES20.glGenBuffers(6, mVertexBufferObjects, 0);
        ((CubeBuffer)mCube).convertToVertexBufferObject(mVertexBufferObjects);
    }

    @Override
    public void onSurfaceCreated(final GL10 glUnused, final EGLConfig config) {
    	mShaderSet.create();
    	mPointShaderSet.create();
    	mTexture = TextureFactory.create(mContext, R.raw.robot);

        final LookAt lookAt = new LookAt(new Vector3D(0, 0, -0.5f), new Vector3D(0, 0, -5.0f), new Vector3D(0, 1.0f, 0));
        final Frustum frustum = new Frustum(1.0f, -1.0f, 1.0f, 10.0f);
        
        mScene.setCamera(new Camera(lookAt, frustum));
    }
    
}
