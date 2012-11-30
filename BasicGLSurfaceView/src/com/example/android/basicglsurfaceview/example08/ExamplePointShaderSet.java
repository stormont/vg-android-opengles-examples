package com.example.android.basicglsurfaceview.example08;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.interfaces.ICamera;
import com.voyagegames.core.android.opengles.modules.ShaderSet;

public class ExamplePointShaderSet extends ShaderSet {
	
	private int mModelViewProjHandle;
	private int mPosHandle;

	@Override
	public String vertexShader() {
		return  
	        	  "uniform mat4 u_MVPMatrix;      \n"		
	            + "attribute vec4 a_Position;     \n"		
	            + "void main()                    \n"
	            + "{                              \n"
	            + "   gl_Position = u_MVPMatrix   \n"
	            + "               * a_Position;   \n"
	            + "   gl_PointSize = 5.0;         \n"
	            + "}                              \n";
	}

	@Override
	public String fragmentShader() {
		return  
	        	  "precision mediump float;       \n"					          
	            + "void main()                    \n"
	            + "{                              \n"
	            + "   gl_FragColor = vec4(1.0,    \n" 
	            + "   1.0, 1.0, 1.0);             \n"
	            + "}                              \n";
	}
	
	@Override
	public void create() {
		mAttribVars.clear();
		mAttribVars.add("a_Position");

		mUniformVars.clear();
		mUniformVars.add("u_MVPMatrix");
		
		super.create();
	}

	@Override
	public void prepareRender() {
		super.prepareRender();

		mModelViewProjHandle = GLES20.glGetUniformLocation(mHandles.program, "u_MVPMatrix");
        mPosHandle = GLES20.glGetAttribLocation(mHandles.program, "a_Position");
	}
	
	public void render(final ICamera camera, final float[] lightPosInModelSpace, final float[] modelMatrix) {
		final float[] mvpMatrix = new float[16];
        
		// Pass in the position.
		GLES20.glVertexAttrib3f(mPosHandle, lightPosInModelSpace[0], lightPosInModelSpace[1], lightPosInModelSpace[2]);

		// Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(mPosHandle);  
		
		// Pass in the transformation matrix.
		Matrix.multiplyMM(mvpMatrix, 0, camera.viewMatrix(), 0, modelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, camera.projectionMatrix(), 0, mvpMatrix, 0);
		GLES20.glUniformMatrix4fv(mModelViewProjHandle, 1, false, mvpMatrix, 0);
		
		// Draw the point.
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	}

}
