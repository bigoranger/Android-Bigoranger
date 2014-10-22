/**
 * 
 */
package com.bigoranger.bigoranger;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author Administrator
 *
 */
public class MyGridView extends GridView {
   /**
	 * @param context
	 */
	public MyGridView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
}
