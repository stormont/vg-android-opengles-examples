package com.example.android.basicglsurfaceview.example01;

import com.voyagegames.core.android.opengles.modules.Orientation;
import com.voyagegames.core.android.opengles.modules.Vector3D;

public class ExampleOrientation extends Orientation {
	
	@Override
	public void update(final long delta) {
		super.update(delta);

        final float angle = 0.090f * ((int)delta);
        
        this.setAngle(angle);
        this.setAxis(new Vector3D(0, 0, 1.0f));
	}

}
