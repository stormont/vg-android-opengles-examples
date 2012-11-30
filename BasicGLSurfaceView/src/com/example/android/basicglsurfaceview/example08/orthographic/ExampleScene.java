package com.example.android.basicglsurfaceview.example08.orthographic;

import android.opengl.GLES20;

import com.voyagegames.core.android.opengles.modules.Scene;
import com.voyagegames.core.android.opengles.modules.Vector4D;

public class ExampleScene extends Scene {
	
	public ExampleScene() {
		super(new Vector4D(0.0f, 0.0f, 1.0f, 1.0f), GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void clear() {
		super.clear();
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}

}
