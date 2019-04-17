package com.cseaeventmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class EventViewActivity extends AppCompatActivity {

	private String event_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);

		TextView marque = (TextView) findViewById(R.id.eventName);
		marque.setSelected(true);


	}
}
