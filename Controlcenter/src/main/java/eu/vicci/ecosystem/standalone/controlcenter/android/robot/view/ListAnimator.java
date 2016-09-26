package eu.vicci.ecosystem.standalone.controlcenter.android.robot.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import eu.vicci.ecosystem.standalone.controlcenter.android.R;

/**
* static helper class to animae collapse and expand of a list
*/
public class ListAnimator {

	public static void expand(View v, ImageButton b){
	   //set Visible
	   v.setVisibility(View.VISIBLE);
	   b.setImageResource(R.drawable.ic_collapse);	   
	   ValueAnimator mAnimator = slideAnimator(0, ((LinearLayout)v).getChildCount()*60, v);
	   mAnimator.start();
	}
	
	/**
	* collapse the chosen list
	*/
	public static void collapse(final View v, final ImageButton b) {		
	    int finalHeight = v.getHeight();
	    ValueAnimator mAnimator = slideAnimator(finalHeight, 0, v);
	  		
  		mAnimator.addListener(new Animator.AnimatorListener() {
  			@Override
  			public void onAnimationEnd(Animator animator) {
  				v.setVisibility(View.GONE);
  				b.setImageResource(R.drawable.ic_expand);
  			}
  			
  			@Override
  			public void onAnimationStart(Animator animator) {
  			}
  
  			@Override
  			public void onAnimationCancel(Animator animator) {
  			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
  		});
  		mAnimator.start();
  	}
	
	/**
	* animator used to expand and collapse the robot list
	*/
	public static ValueAnimator slideAnimator(int start, int end, final View v) {
	 
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		  
	    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	         @Override
	         public void onAnimationUpdate(ValueAnimator valueAnimator) {
	            //Update Height
	            int value = (Integer) valueAnimator.getAnimatedValue();
	            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
	            layoutParams.height = value;
	            v.setLayoutParams(layoutParams);
	         }
	    });
	    return animator;
	}
}
