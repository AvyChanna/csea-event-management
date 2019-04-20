package com.cseaeventmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class CustomFAQ extends AppCompatActivity {

	private EditText question;
	private EditText answer;
//	private JSONArray obj_array;
	private int count=0;
	private String cust_faq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_faq);
//		obj_array = new JSONArray();
	}

	public void getfaqed(View v) {
		if(count==0)
		{
			Context context = getApplicationContext();
			CharSequence text = "Fill atleast one question and answer";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else{
			Intent i = new Intent();
			i.putExtra("faq", cust_faq);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	public void clear_and_save(View v) {
		question = findViewById(R.id.editText_faq_question);
		answer = findViewById(R.id.editText_faq_answer);

		View focusView = null;
		boolean cancel = false;

		if (TextUtils.isEmpty(question.getText().toString())) {
			question.setError("This field is required");
			focusView = question;
			cancel = true;
		}
		if (TextUtils.isEmpty(answer.getText().toString())) {
			answer.setError("This field is required");
			focusView = answer;
			cancel = true;
		}

		if (cancel)
			focusView.requestFocus();
		else {
			JSONObject obj = new JSONObject();
			try {
//				obj.accumulate("question", question.getText().toString());
//				obj.accumulate("answer", answer.getText().toString());
//				obj_array.put(obj);
				cust_faq = cust_faq + question.getText().toString() + "=" + answer.getText().toString() + "|";
				question.setText("");
				answer.setText("");
				count++;
				Context context = getApplicationContext();
				CharSequence text = "This question was successfully submitted";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} catch (Exception e) {
				Log.d("REQUEST_CUST_FAQ_CATCH", e.toString());
				count--;
			}
		}
	}
}
