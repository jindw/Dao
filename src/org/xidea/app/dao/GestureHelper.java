package org.xidea.app.dao;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureHelper {
	public static void enableFliper(View view,GestureDetector.OnGestureListener listener){
		final GestureDetector det = new GestureDetector(view.getContext(),listener);
		if(listener instanceof GestureDetector.OnDoubleTapListener){
			det.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)listener);
		}
		view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return det.onTouchEvent(event);
			}
		});
	}

}
