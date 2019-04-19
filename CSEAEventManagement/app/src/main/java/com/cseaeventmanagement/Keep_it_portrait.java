package com.cseaeventmanagement;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Keep_it_portrait extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivityStarted(Activity activity) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivityResumed(Activity activity) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivityPaused(Activity activity) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivityStopped(Activity activity) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
				activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
	}
}
