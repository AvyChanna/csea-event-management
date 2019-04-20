package com.cseaeventmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingActivity extends AppCompatActivity {

	Context c = this;
	private ViewPager mSlideViewPager;
	private LinearLayout mDotLayout;
	private SlideAdapter slideAdapter;
	private TextView[] mdots;
	private int mCurrentPage;
	ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int i, float v, int i1) {

		}

		@Override
		public void onPageSelected(int i) {
			addDotsIndicator(i);
			mCurrentPage = i;
		}

		@Override
		public void onPageScrollStateChanged(int i) {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_boarding);

		mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
		mDotLayout = (LinearLayout) findViewById(R.id.DotsLayout);
		slideAdapter = new SlideAdapter(this);
		mSlideViewPager.setAdapter(slideAdapter);

		addDotsIndicator(0);
		mSlideViewPager.addOnPageChangeListener(viewListener);

		findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((OnBoardingActivity) c).finish();
			}
		});
	}

	public void addDotsIndicator(int position) {
		mdots = new TextView[3];
		mDotLayout.removeAllViews();
		for (int i = 0; i < mdots.length; i++) {
			mdots[i] = new TextView(this);
			mdots[i].setText(Html.fromHtml("&#8226;"));
			mdots[i].setTextSize(35);
			mdots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

			mDotLayout.addView(mdots[i]);
		}

		if (mdots.length > 0) {
			mdots[position].setTextColor(getResources().getColor(R.color.colorWhite));
		}
	}
}
