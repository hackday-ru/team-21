package com.epam.safety;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dbychkov on 16.04.16.
 */
public class ScrollOffBottomBehavior extends CoordinatorLayout.Behavior<View> {

    public static final int TRANSLATION_MULTIPLIER = 2;

    private int viewHeight;
    private ObjectAnimator animator;

    public ScrollOffBottomBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        viewHeight = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if(animator == null || !animator.isRunning()){
            int totalScroll = (dyConsumed + dyUnconsumed);
            int targetTranslation = totalScroll > 0 ? viewHeight : 0;
            animator = ObjectAnimator.ofFloat(child, "translationY", targetTranslation * TRANSLATION_MULTIPLIER);
            animator.start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;


    }


}