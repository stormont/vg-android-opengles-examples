package com.example.android.basicglsurfaceview.example05;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.interfaces.ICamera;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.modules.ShaderSet;

public class ExampleShaderSet extends ShaderSet {
	
	private int mModelViewProjHandle;

	@Override
	public String vertexShader() {
		return  
			        "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
				 
				  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
				  + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
				 
				  + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
				 
				  + "void main() {                  \n"     // The entry point for our vertex shader.
				// Pass through the color.
				  + "   v_Color = a_Color;                                                 \n"
				// gl_Position is a special variable used to store the final position.
				// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
				  + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
				  + "}                                                                     \n";
	}

	@Override
	public String fragmentShader() {
		return  
				  "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a precision in the fragment shader.
                + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the triangle per fragment.
                
                + "void main() {                  \n"     // The entry point for our fragment shader.
                + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                + "}                              \n";
	}
	
	@Override
	public void create() {
		mAttribVars.clear();
		mAttribVars.add("a_Position");
    	mAttribVars.add("a_Color");

    	mUniformVars.clear();
    	mUniformVars.add("u_MVPMatrix");
		
		super.create();
	}
	
	@Override
	public void prepareRender() {
		super.prepareRender();
		
        mModelViewProjHandle = handles().getUniformLocation("u_MVPMatrix");
	}
	
	public void render(final ICamera camera, final IRenderableBuffer buffer, final float[] modelMatrix) {
		final float[] mat = new float[16];

        Matrix.multiplyMM(mat, 0, camera.viewMatrix(), 0, modelMatrix, 0);
        Matrix.multiplyMM(mat, 0, camera.projectionMatrix(), 0, mat, 0);
        GLES20.glUniformMatrix4fv(mModelViewProjHandle, 1, false, mat, 0);
        
        buffer.render(mAttribVars);
	}

}
