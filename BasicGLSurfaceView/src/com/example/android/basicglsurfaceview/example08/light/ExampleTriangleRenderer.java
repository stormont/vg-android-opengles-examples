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

package com.example.android.basicglsurfaceview.example08.light;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.voyagegames.core.android.opengles.buffers.ColoredNormalTriangleBuffer;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.interfaces.IScene;
import com.voyagegames.core.android.opengles.interfaces.IShaderSet;
import com.voyagegames.core.android.opengles.modules.Camera;
import com.voyagegames.core.android.opengles.modules.Entity;
import com.voyagegames.core.android.opengles.modules.Frustum;
import com.voyagegames.core.android.opengles.modules.Light;
import com.voyagegames.core.android.opengles.modules.LookAt;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public class ExampleTriangleRenderer implements Renderer {
    
    private final float[] TEMP_VERTICES_DATA = {
			// Front face           // Front face          // Front face
			-1.0f,  1.0f, 1.0f,     0.0f, 0.0f, 1.0f,      1.0f, 0.0f, 0.0f, 1.0f,		
			-1.0f, -1.0f, 1.0f,     0.0f, 0.0f, 1.0f,      0.0f, 1.0f, 0.0f, 1.0f,
			 1.0f,  1.0f, 1.0f,     0.0f, 0.0f, 1.0f,      0.0f, 0.0f, 1.0f, 1.0f,
			 1.0f, -1.0f, 1.0f,     0.0f, 0.0f, 1.0f,      1.0f, 1.0f, 1.0f, 1.0f
    };
    
    private final short[] TEMP_INDICES_DATA = {
    	    0, 1, 2, 3
    };

    private final int[] mVertexBufferObjects = new int[1];
    private final int[] mIndexBufferObjects = new int[1];

    private final IScene mScene;
    private final IRenderableBuffer mTriangleBuffer;
    private final IShaderSet mShaderSet;
    private final IShaderSet mPointShaderSet;
    
    private final Light mLight;
    private final Entity[] mObjects;

    public ExampleTriangleRenderer(final Context context) {
        mScene = new ExampleScene();
        mTriangleBuffer = new ColoredNormalTriangleBuffer(TEMP_VERTICES_DATA, TEMP_INDICES_DATA, GLES20.GL_TRIANGLE_STRIP);
    	mShaderSet = new ExampleShaderSet();
    	mPointShaderSet = new ExamplePointShaderSet();
    	
    	mLight = new Light();
    	mObjects = new Entity[5];
    	
    	initEntities();
    }
    
    private void initEntities() {
    	mLight.setPosition(new Vector3D(0.0f, 0.0f, -5.0f));
    	mLight.setRotationAxis(new Vector3D(0.0f, 1.0f, 0.0f));
        
        mObjects[0] = new Entity();
        mObjects[0].setPosition(new Vector3D(4.0f, 0.0f, -7.0f));
        mObjects[0].setRotationAxis(new Vector3D(1.0f, 0, 0));
        
        mObjects[1] = new Entity();
        mObjects[1].setPosition(new Vector3D(-4.0f, 0.0f, -7.0f));
        mObjects[1].setRotationAxis(new Vector3D(0, 1.0f, 0));
        
        mObjects[2] = new Entity();
        mObjects[2].setPosition(new Vector3D(0.0f, 4.0f, -7.0f));
        mObjects[2].setRotationAxis(new Vector3D(0, 0, 1.0f));
        
        mObjects[3] = new Entity();
        mObjects[3].setPosition(new Vector3D(0.0f, -4.0f, -7.0f));
        
        mObjects[4] = new Entity();
        mObjects[4].setPosition(new Vector3D(0.0f, 0.0f, -5.0f));
        mObjects[4].setRotationAxis(new Vector3D(0, 1.0f, 1.0f));
    }

    @Override
    public void onDrawFrame(final GL10 glUnused) {
    	mScene.clear();

    	
		// Do a complete rotation every 10 seconds.
		long time = SystemClock.uptimeMillis() % 10000L;
		float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        
        
        // Calculate position of the light. Rotate and then push into the distance.
		mLight.setRotationAngle(angleInDegrees);
        
        final float[] lightModelMatrix = mLight.modelMatrix();

        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        final float[] lightPosInEyeSpace = mLight.positionInEyeSpace(mScene.camera().viewMatrix(), mLight.positionInWorldSpace(lightModelMatrix));
        
        
        // Draw some cubes
        mShaderSet.prepareRender();

        for (int i = 0; i < mObjects.length; i++) {
            mObjects[i].setRotationAngle(angleInDegrees);
            ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mTriangleBuffer, mObjects[i].modelMatrix(), lightPosInEyeSpace);
        }

        
        // Draw the light
        mPointShaderSet.prepareRender();
        ((ExamplePointShaderSet)mPointShaderSet).render(mScene.camera(), mLight.positionInModelSpace(), lightModelMatrix);
    }

    @Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height) {
        mScene.updateViewport(width, height);
        mTriangleBuffer.setHandles(mShaderSet.handles());
        
        GLES20.glGenBuffers(1, mVertexBufferObjects, 0);
        GLES20.glGenBuffers(1, mIndexBufferObjects, 0);
        mTriangleBuffer.convertToVertexBufferObject(mVertexBufferObjects[0]);
        mTriangleBuffer.convertToIndexBufferObject(mIndexBufferObjects[0]);
    }

    @Override
    public void onSurfaceCreated(final GL10 glUnused, final EGLConfig config) {
    	mShaderSet.create();
    	mPointShaderSet.create();

        final LookAt lookAt = new LookAt(new Vector3D(0, 0, -0.5f), new Vector3D(0, 0, -5.0f), new Vector3D(0, 1.0f, 0));
        final Frustum frustum = new Frustum(1.0f, -1.0f, 1.0f, 10.0f);
        
        mScene.setCamera(new Camera(lookAt, frustum));
    }
    
}
