package com.example.android.basicglsurfaceview.example01;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.interfaces.ICamera;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.modules.ShaderSet;

public class ExampleShaderSet extends ShaderSet {
	
	private int mTextureID;
	private int mModelViewProjHandle;
	
	public void setTextureID(final int textureID) {
		mTextureID = textureID;
	}

	@Override
	public String vertexShader() {
		return  "uniform mat4 uMVPMatrix;\n" +
		        "attribute vec4 aPosition;\n" +
		        "attribute vec2 aTextureCoord;\n" +
		        "varying vec2 vTextureCoord;\n" +
		        "void main() {\n" +
		        "  gl_Position = uMVPMatrix * aPosition;\n" +
		        "  vTextureCoord = aTextureCoord;\n" +
		        "}\n";
	}

	@Override
	public String fragmentShader() {
		return  "precision mediump float;\n" +
		        "varying vec2 vTextureCoord;\n" +
		        "uniform sampler2D sTexture;\n" +
		        "void main() {\n" +
		        "  gl_FragColor = texture2D(sTexture, vTextureCoord);\n" +
		        "}\n";
	}
	
	@Override
	public void create() {
    	mAttribVars.clear();
    	mAttribVars.add("aPosition");
    	mAttribVars.add("aTextureCoord");
    	
    	mUniformVars.clear();
    	mUniformVars.add("uMVPMatrix");
    	
    	super.create();
	}

	@Override
	public void prepareRender() {
		super.prepareRender();
		
		mModelViewProjHandle = mHandles.getUniformLocation("uMVPMatrix");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
	}
	
	public void render(final ICamera camera, final IRenderableBuffer buffer, final float[] modelMatrix) {
		final float[] mvpMatrix = new float[16];
		
        Matrix.multiplyMM(mvpMatrix, 0, camera.viewMatrix(), 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, camera.projectionMatrix(), 0, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewProjHandle, 1, false, mvpMatrix, 0);
        buffer.render(mAttribVars);
	}

}
