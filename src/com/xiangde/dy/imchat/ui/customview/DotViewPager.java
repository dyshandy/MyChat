package com.xiangde.dy.imchat.ui.customview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.xiangde.dy.imchat.adapters.DotViewPagerAdapter;
import com.xiangde.mychat_tencent.R;

/**
 * 自定义带有底部原点的ViewPager
 * 
 */
public class DotViewPager extends LinearLayout implements OnClickListener,
		Cloneable {// , Runnable {
	private ViewPager viewPager;
	private LinearLayout viewDots;
	private List<ImageView> dots;
//	private List<View> views;
	// private int myposition = 0;
	// private boolean isContinue = false;// true;
	private float dotsViewHeight;// 引导view的高度
	private float dotsSpacing;// 点与点直接的间隔
	private Drawable dotsFocusImage;// 当前选中的Dots
	private Drawable dotsBlurImage;// 未选中的Dots
	private ScaleType scaleType;// view如果是图片的话使用该属性
	private int gravity;// 原点位置
	private Drawable dotsBackground;// 引导View的背景
	private float dotsBgAlpha;// 定义底部指示物的背景颜色或背景图的透明度，取值为0-1,0代表透明;
	private int changeInterval;
	// private Context mContext;

	// dotsViewHeight定义底部指示物所在视图(我定义为一个LinearLayout)的高度，也就是示例图中圆圈所在灰色透明部分的高度，默认为40像素;
	// dotsSpacing定义底部指示物之间的间距，默认为0;
	// dotsFocusImage定义代表当前页的指示物的样子;
	// dotsBlurImage定义代表非当前页的指示物的样子;
	// android:scaleType定义ViewPager中ImageView的scale类型，如果ViewPager中的View不是ImageView，则此属性没有效果，默认为ScaleType.FIT_XY;
	// android:gravity定义底部指示物在父View(即示例灰色透明部分)的gravity属性;
	// dotsBackground定义底部指示物的背景颜色或背景图;
	// dotsBgAlpha定义底部指示物的背景颜色或背景图的透明度，取值为0-1,0代表透明;
	// changeInteval定义ViewPager自动切换的时间间隔，单位为ms，默认为1000ms；
	private NewOnclick mynewOnclick = null;

	public void setNewOnclick(NewOnclick newOnclick) {
		mynewOnclick = newOnclick;
	}

	public interface NewOnclick {
		public void MyNewClick(String str, int nums);
	}

	public DotViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.mContext = context;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.DotViewPager, 0, 0);
		try {
			dotsViewHeight = a.getDimension(
					R.styleable.DotViewPager_dotsViewHeight, 40);
			dotsSpacing = a.getDimension(R.styleable.DotViewPager_dotsSpacing,
					0);
			dotsFocusImage = a
					.getDrawable(R.styleable.DotViewPager_dotsFocusImage);
			dotsBlurImage = a
					.getDrawable(R.styleable.DotViewPager_dotsBlurImage);
			int scaleNum = a.getInteger(
					R.styleable.DotViewPager_android_scaleType,
					ScaleType.FIT_XY.ordinal());
			scaleType = ScaleType.FIT_XY;
			for (ScaleType st : ScaleType.values()) {
				if (st.ordinal() == scaleNum) {
					scaleType = st;
					break;
				}
			}
			gravity = a.getInteger(R.styleable.DotViewPager_android_gravity,
					Gravity.CENTER);
			dotsBackground = a
					.getDrawable(R.styleable.DotViewPager_dotsBackground);
			dotsBgAlpha = a.getFloat(R.styleable.DotViewPager_dotsBgAlpha, 0);
			changeInterval = a.getInteger(
					R.styleable.DotViewPager_changeInterval, 1000);

			if (dotsFocusImage == null) {
				// dotsFocusImage =
				// getResources().getDrawable(R.drawable.dot_red);
				dotsFocusImage = ContextCompat.getDrawable(context,
						R.drawable.point_focused);
			}
			if (dotsBlurImage == null) {
				dotsFocusImage = ContextCompat.getDrawable(context,
						R.drawable.point_unfocused);

			}
		} finally {
			a.recycle();
		}

		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		viewPager = new ViewPager(getContext());
		viewDots = new LinearLayout(getContext());

		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(viewPager, lp);
		// lp = new LayoutParams(LayoutParams.MATCH_PARENT, 40);
		// lp.topMargin = -40;
		if (dotsBackground != null) {
			dotsBackground.setAlpha((int) (dotsBgAlpha * 255));
			viewDots.setBackground(dotsBackground);
		}
		viewDots.setGravity(gravity);
		addView(viewDots, lp);
	}

	public void addDots(int num) {
		viewDots.removeAllViews();
		dots = new ArrayList<ImageView>();

		for (int i = 0; i < num; i++) {
			ImageView dot = new ImageView(getContext());
			int margin = (int) (dotsSpacing / 2);
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.leftMargin = lp.rightMargin = margin;

			if (dots.size() == 0)
				dot.setImageDrawable(dotsFocusImage);
			else
				dot.setImageDrawable(dotsBlurImage);

			dots.add(dot);

			viewDots.addView(dot, lp);
		}

	}

	public void switchToDot(int index) {
		for (int i = 0; i < dots.size(); i++) {
			dots.get(i).setImageDrawable(dotsBlurImage);
		}
		dots.get(index).setImageDrawable(dotsFocusImage);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void setViewPagerViews(final Context mContext, final List<View> views) {
		// 后面添加一个view
//		this.views = views;
		addDots(views.size());
		viewPager.setAdapter(new DotViewPagerAdapter(views));
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				switchToDot(index);
				// myposition = index;
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				viewPager.getParent().requestDisallowInterceptTouchEvent(true);

			}
		});
		viewPager.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				view.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		// new Thread(this).start();
	}

	//
	// 设置ViewPager转到指定的界面
	// viewPager.setCurremtItem(page); 即可指定到某个界面了
	// 点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case value:
		//
		// break;
		//
		default:
			break;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View child = this.getChildAt(0);
		// child.measure(r - l, b - t);如果有这一行，一张都显示不出来
		child.layout(0, 0, getWidth(), getHeight());
		if (changed) {
			// 如果没有if前面的几行，而加在这里，如果默认显示第一张图，则第三张以后都不能显示
			// View child = this.getChildAt(0);
			// child.measure(r - l, b - t);
			// child.layout(0, 0, getWidth(), getHeight());

			child = this.getChildAt(1);
			child.measure(r - l, (int) dotsViewHeight);
			child.layout(0, getHeight() - (int) dotsViewHeight, getWidth(),
					getHeight());
		}
	}
	//
	// /*
	// * 设置handler自动切换，需要实现 Runnable接口
	// */
	// @Override
	// public void run() {
	// while (true) {
	// if (isContinue) {
	// pageHandler.sendEmptyMessage(position);
	// position = (position + 1) % views.size();
	// try {
	// Thread.sleep(changeInterval);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// Handler pageHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// viewPager.setCurrentItem(msg.what);
	// super.handleMessage(msg);
	// }
	// };

}