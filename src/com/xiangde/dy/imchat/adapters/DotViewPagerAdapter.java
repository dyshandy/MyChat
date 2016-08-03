package com.xiangde.dy.imchat.adapters;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class DotViewPagerAdapter extends PagerAdapter {
	private List<View> mList;

	public DotViewPagerAdapter(List<View> views) {
		this.mList = views;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		/**
		 * 销毁预加载以外的view对象, 会把需要销毁的对象的索引位置传进来，就是arg1，
		 * 因为imageViews只有四条数据，而arg1将会取到很大的值， 所以使用取余数的方法来获取每一条数据项。
		 */
		// ((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// // ((ViewPager) arg0).addView(mList.get(arg1));
		// // return mList.get(arg1);
		// if (arg1 == 0) {
		// arg1 = arg1 + 2;
		// System.out.println("arg1：2");
		// } else if (arg1 == mList.size() - 1) {
		// arg1 = 1;
		// System.out.println("arg1：3");
		// } else {
		// arg1 = arg1 - 1;
		// System.out.println("arg1:" + arg1);
		// }
		View view = mList.get(arg1);
		// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}
		((ViewPager) arg0).addView(view);
		return view;
	}
}
