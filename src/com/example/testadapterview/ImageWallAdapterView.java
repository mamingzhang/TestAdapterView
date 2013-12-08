package com.example.testadapterview;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public class ImageWallAdapterView extends AdapterView<ListAdapter> implements
		OnGestureListener
{

	private ListAdapter mListAdapter = null;

	private boolean bHasCalWidth = false;
	
	private int widthPerChildView;
	private int heightPerChildView;
	private int childCountPerScreen;
	private int scrollOffsetX;
	private int marginBetweenChild;
	
	private GestureDetector gestureDetector;

	public ImageWallAdapterView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public ImageWallAdapterView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public ImageWallAdapterView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	@SuppressLint("NewApi")
	private void init()
	{
		marginBetweenChild = 10;
		
		gestureDetector = new GestureDetector(getContext(), this);
		gestureDetector.setIsLongpressEnabled(true); // 监听长按事件
	}

	@Override
	public ListAdapter getAdapter()
	{
		// TODO Auto-generated method stub
		return mListAdapter;
	}

	@Override
	public void setAdapter(ListAdapter adapter)
	{
		// TODO Auto-generated method stub
		mListAdapter = adapter;

		bHasCalWidth = false;
		requestLayout();
	}

	@Override
	public View getSelectedView()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelection(int position)
	{
		// TODO Auto-generated method stub

	}

	private void recycleAllViews()
	{
		removeAllViewsInLayout();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		// TODO Auto-generated method stub
		recycleAllViews();
		
		if(mListAdapter == null || mListAdapter.getCount() == 0)
		{
			return;
		}
		
		boolean bHasAddZero = false;
		
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY);
		if(!bHasCalWidth)
		{		
			if(getChildCount() == 0)
			{
				View childView = mListAdapter.getView(0, null, this);
				addViewInLayout(childView, 0, childView.getLayoutParams());
				
				bHasAddZero = true;
			}
			
			View childView = getChildAt(0);
			measureChild(childView, widthMeasureSpec, heightMeasureSpec);
			
			widthPerChildView = childView.getMeasuredWidth(); 
			heightPerChildView = childView.getMeasuredHeight(); 

			childCountPerScreen = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight())
					/ widthPerChildView; 			
		}
		
		int beginChildX, endChildX;
		
		Log.v("mmz", "onlayout scrollOffsetX : "+scrollOffsetX);
		
		beginChildX = (Math.abs(scrollOffsetX) - getPaddingLeft()) / widthPerChildView;
		endChildX = (Math.abs(scrollOffsetX) + getMeasuredWidth() - getPaddingLeft() + widthPerChildView) / widthPerChildView;
		beginChildX = Math.max(0, beginChildX);
		beginChildX = Math.min(mListAdapter.getCount() - 1, beginChildX);
		endChildX = Math.max(0, endChildX);
		endChildX = Math.min(mListAdapter.getCount() - 1, endChildX);
		
		Log.v("mmz", "beginChildX : "+beginChildX);
		Log.v("mmz", "endChildX : "+endChildX);
		for(int index = beginChildX; index <= endChildX; index++)
		{
			View childView = null;
			
			if(index == 0 && bHasAddZero)
				childView = getChildAt(0);
			
			if(childView == null)
			{
				childView = mListAdapter.getView(index, null, this);
				addViewInLayout(childView, 0, childView.getLayoutParams());
				measureChild(childView, widthMeasureSpec, heightMeasureSpec);
			}
			
			int pLeft = 0, pTop = getPaddingTop();
			if(index == 0)
				pLeft = getPaddingLeft() + scrollOffsetX;
			else
				pLeft = getPaddingLeft() + widthPerChildView * index + marginBetweenChild * index + scrollOffsetX;
						
			childView.layout(pLeft, pTop, pLeft + widthPerChildView, pTop + heightPerChildView);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		// TODO Auto-generated method stub
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int height = getDefaultSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		// TODO Auto-generated method stub
		Log.v("mmz", "onScroll : "+distanceX);
		if(mListAdapter == null || mListAdapter.getCount() == 0)
		{
			scrollOffsetX = 0;
			return true;
		}
		
		int tmpScrollOffsetX = Math.abs(scrollOffsetX);
		tmpScrollOffsetX = (int) (tmpScrollOffsetX + distanceX);
		
		if (tmpScrollOffsetX < 0)
		{
			tmpScrollOffsetX = 0;
		}
		else if (tmpScrollOffsetX > getPaddingLeft() + getPaddingRight() + mListAdapter.getCount() * widthPerChildView
				+ (mListAdapter.getCount() - 1) * marginBetweenChild)
		{
			tmpScrollOffsetX = getPaddingLeft() + getPaddingRight() + mListAdapter.getCount() * widthPerChildView
					+ (mListAdapter.getCount() - 1) * marginBetweenChild - getWidth();
		}
		
		scrollOffsetX = -tmpScrollOffsetX;
		Log.v("mmz", "scrollOffsetX : "+scrollOffsetX);
		
		requestLayout();

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mListAdapter == null)
		{
			return true;
		}
		gestureDetector.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_UP)
		{ // 在手指抬起时调用onUp方法
			onUp();
		}
		
		return true;
	}

	private void onUp()
	{
//		int index = (int) (Math.abs(scrollOffsetX) / widthPerChildView);
//		index += (Math.abs(scrollOffsetX) - index * widthPerChildView) > widthPerChildView / 2 ? 1 : 0;
//		scrollOffsetX = scrollOffsetX > 0 ? index * widthPerChildView : -1 * index * widthPerChildView;
//		requestLayout();
	}
}
