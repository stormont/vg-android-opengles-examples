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

package com.example.android.basicglsurfaceview.example01;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

import com.example.android.basicglsurfaceview.R;
import com.voyagegames.core.android.opengles.TextureFactory;
import com.voyagegames.core.android.opengles.buffers.TexturedTriangleBuffer;
import com.voyagegames.core.android.opengles.interfaces.IOrientation;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.interfaces.IScene;
import com.voyagegames.core.android.opengles.interfaces.IShaderSet;
import com.voyagegames.core.android.opengles.modules.Camera;
import com.voyagegames.core.android.opengles.modules.Frustum;
import com.voyagegames.core.android.opengles.modules.LookAt;
import com.voyagegames.core.android.opengles.modules.Texture;
import com.voyagegames.core.android.opengles.modules.Vector3D;

class ExampleTriangleRenderer implements Renderer {
    
    private final float[] TEMP_TRIANGLE_VERTICES_DATA = {
            // X, Y, Z, U, V
            -1.0f, -0.5f,         0, -0.5f,  0,
             1.0f, -0.5f,         0,  1.5f,  0,
             0.0f,  1.11803399f,  0,  0.5f,  1.61803399f };

    private final Context mContext;
    private final IScene mScene;
    private final IRenderableBuffer mTriangleBuffer;
    private final IOrientation mOrientation;
    private final IShaderSet mShaderSet;

    private Texture mTexture;

    public ExampleTriangleRenderer(final Context context) {
        mContext = context;
        mScene = new ExampleScene();
        mTriangleBuffer = new TexturedTriangleBuffer(TEMP_TRIANGLE_VERTICES_DATA);
        mOrientation = new ExampleOrientation();
    	mShaderSet = new ExampleShaderSet();
    }

    @Override
    public void onDrawFrame(final GL10 glUnused) {
    	mScene.clear();
        mShaderSet.prepareRender();

        final long time = SystemClock.uptimeMillis() % 4000L;
        
        mOrientation.update(time);
        ((ExampleShaderSet)mShaderSet).render(mScene.camera(), mTriangleBuffer, mOrientation.modelMatrix());
    }

    @Override
    public void onSurfaceChanged(final GL10 glUnused, final int width, final int height) {
        mScene.updateViewport(width, height);
        mTriangleBuffer.setHandles(mShaderSet.handles());
        ((ExampleShaderSet)mShaderSet).setTextureID(mTexture.textureID);
    }

    @Override
    public void onSurfaceCreated(final GL10 glUnused, final EGLConfig config) {
    	mShaderSet.create();
    	mTexture = TextureFactory.create(mContext, R.raw.robot);

        final LookAt lookAt = new LookAt(new Vector3D(0, 0, -5.0f), new Vector3D(0, 0, 0), new Vector3D(0, 1.0f, 0));
        final Frustum frustum = new Frustum(-1.0f, 1.0f, 3.0f, 7.0f);
        
        mScene.setCamera(new Camera(lookAt, frustum));
    }
    
}
