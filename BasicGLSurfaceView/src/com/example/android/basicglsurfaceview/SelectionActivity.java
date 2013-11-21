package com.example.android.basicglsurfaceview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectionActivity extends Activity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		
		final Button btnExample01 = (Button)findViewById(R.id.btnExample01);
		btnExample01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example01.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample02 = (Button)findViewById(R.id.btnExample02);
		btnExample02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example02.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample03 = (Button)findViewById(R.id.btnExample03);
		btnExample03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example03.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample04 = (Button)findViewById(R.id.btnExample04);
		btnExample04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example04.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample05 = (Button)findViewById(R.id.btnExample05);
		btnExample05.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example05.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample06 = (Button)findViewById(R.id.btnExample06);
		btnExample06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example06.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample07 = (Button)findViewById(R.id.btnExample07);
		btnExample07.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example07.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample07Camera = (Button)findViewById(R.id.btnExample07Camera);
		btnExample07Camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example07.camera.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample07Cubes = (Button)findViewById(R.id.btnExample07Cubes);
		btnExample07Cubes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example07.cubes.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample07TriangleStrip = (Button)findViewById(R.id.btnExample07TriangleStrip);
		btnExample07TriangleStrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example07.trianglestrip.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample08 = (Button)findViewById(R.id.btnExample08);
		btnExample08.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example08.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample08Light = (Button)findViewById(R.id.btnExample08Light);
		btnExample08Light.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example08.light.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample08Orthographic = (Button)findViewById(R.id.btnExample08Orthographic);
		btnExample08Orthographic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example08.orthographic.ExampleGLSurfaceViewActivity.class));
			}
		
		});
		
		final Button btnExample08TriangleStrip = (Button)findViewById(R.id.btnExample08TriangleStrip);
		btnExample08TriangleStrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				startActivity(
						new Intent(
								SelectionActivity.this,
								com.example.android.basicglsurfaceview.example08.trianglestrip.ExampleGLSurfaceViewActivity.class));
			}
		
		});
	}

}
