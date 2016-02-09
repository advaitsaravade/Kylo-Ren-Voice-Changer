package com.advait.s.emokylo;


import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


public class SnappingRecyclerView extends RecyclerView {

private Context c;

	public SnappingRecyclerView(Context context) {
		super(context);
		c = context;
	}


	public SnappingRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
	}
	
	@Override 
	public boolean fling(int velocityX, int velocityY) {
	    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
	 
	//these four variables identify the views you see on screen. 
	    int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
	    int firstVisibleView = linearLayoutManager.findFirstVisibleItemPosition();
	    View firstView = linearLayoutManager.findViewByPosition(firstVisibleView);
	    View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);
	 
	//these variables get the distance you need to scroll in order to center your views. 
	//my views have variable sizes, so I need to calculate side margins separately.      
	//note the subtle difference in how right and left margins are calculated, as well as 
	//the resulting scroll distances. 
	    WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);
    	int width = size.x;
    	int height = size.y;
	    int leftMargin = ((getWidth()) - lastView.getWidth()) / 2;
	    int rightMargin = ((getWidth()) - firstView.getWidth()) / 2 + firstView.getWidth();
	    int leftEdge = lastView.getLeft();
	    int rightEdge = firstView.getRight();
	    int scrollDistanceLeft = leftEdge - leftMargin;
	    int scrollDistanceRight = rightMargin - rightEdge;
	 
	//if(user swipes to the left)  
	    if(velocityX > 0) smoothScrollBy(scrollDistanceLeft, 0);
	    else smoothScrollBy(-scrollDistanceRight, 0);
	 
	    return true; 
	} 
}
