package com.cseaeventmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
	public String ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

	}

	public void func_setting(View v) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.ip_pref), 0);
		SharedPreferences.Editor editor = pref.edit();
		EditText textbox = (EditText) findViewById(R.id.edittext_settings);
		editor.clear();
		editor.apply();
		editor.putString("ip", textbox.getText().toString());
		editor.commit();
	}
}
