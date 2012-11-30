package com.example.android.basicglsurfaceview.example02;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.voyagegames.core.android.opengles.interfaces.ICamera;
import com.voyagegames.core.android.opengles.interfaces.IRenderableBuffer;
import com.voyagegames.core.android.opengles.modules.ShaderSet;

public class ExampleShaderSet extends ShaderSet {
	
	private int mModelViewProjHandle;
	private int mModelViewHandle;
	private int mLightPosHandle;

	@Override
	public String vertexShader() {
		return  
			        "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
				  + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
				  + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.
				 
				  + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
				  + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
				  + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in.
				 
				  + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
				 
				  + "void main() {                  \n"     // The entry point for our vertex shader.
				// Transform the vertex into eye space.
				  + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
				// Transform the normal's orientation into eye space.
				  + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
				// Will be used for attenuation.
				  + "   float distance = length(u_LightPos - modelViewVertex);             \n"
				// Get a lighting direction vector from the light to the vertex.
				  + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
				// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
				// pointing in the same direction then it will get max illumination.
				  + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
				// Attenuate the light based on distance.
				  + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
				// Multiply the color by the illumination level. It will be interpolated across the triangle.
				  + "   v_Color = a_Color * diffuse;                                       \n"
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
    	mAttribVars.add("a_Normal");
    	mAttribVars.add("a_Color");

    	mUniformVars.clear();
    	mUniformVars.add("u_MVPMatrix");
    	mUniformVars.add("u_MVMatrix");
    	mUniformVars.add("u_LightPos");
		
		super.create();
	}
	
	@Override
	public void prepareRender() {
		super.prepareRender();
		
        mModelViewProjHandle = handles().getUniformLocation("u_MVPMatrix");
        mModelViewHandle = handles().getUniformLocation("u_MVMatrix");
        mLightPosHandle = handles().getUniformLocation("u_LightPos");
	}
	
	public void render(final ICamera camera, final IRenderableBuffer buffer, final float[] modelMatrix, final float[] lightPosInEyeSpace) {
		final float[] mat = new float[16];

        Matrix.multiplyMM(mat, 0, camera.viewMatrix(), 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewHandle, 1, false, mat, 0);
        Matrix.multiplyMM(mat, 0, camera.projectionMatrix(), 0, mat, 0);
        GLES20.glUniformMatrix4fv(mModelViewProjHandle, 1, false, mat, 0);

        GLES20.glUniform3f(mLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1], lightPosInEyeSpace[2]);
        
        buffer.render(mAttribVars);
	}

}
