package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageButton;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
* static helper class to animate collapse and expand of a map (just overwrites the expand method of listanimator to adjust the height)
*/
public class MapAnimator extends ListAnimator{

	public static void expand(View v, ImageButton b){
	   //set Visible
	   v.setVisibility(View.VISIBLE);
	   b.setImageResource(R.drawable.ic_collapse);	   
	   ValueAnimator mAnimator = slideAnimator(0, 250, v);
	   mAnimator.start();
	}
}
