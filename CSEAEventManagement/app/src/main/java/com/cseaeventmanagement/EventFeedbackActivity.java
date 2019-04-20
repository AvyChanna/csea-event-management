// Feedback bharne ke baad vapas event activity details pe bhej dena hai
// abhi usmein ye bhi lena hai, event_id

package com.cseaeventmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONException;
import org.json.JSONObject;

public class EventFeedbackActivity extends AppCompatActivity {

	private TextView rating_val;
	private EmojiAppCompatTextView rating_message;
	private RatingBar ratingBar;
	private float ratedVal;
	private Button btn_event_feedback;
	private EditText comment;
	private String event_id;
	private RequestQueue q;
	private JSONObject resp;
	private String comment_text;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
		EmojiCompat.init(config);
		setContentView(R.layout.activity_event_feedback);
		Intent i = getIntent();
		event_id = i.getStringExtra("event_id");
		ratingBar = (RatingBar) findViewById(R.id.event_rating);
		rating_val = (TextView) findViewById(R.id.captcha_feedback);
		rating_message = (EmojiAppCompatTextView) findViewById(R.id.captcha_message);
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();

		ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				ratedVal = ratingBar.getRating();
				rating_val.setText("Your rating: " + ratedVal + "/5");
				if (ratedVal < 1) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F620)));
					rating_message.setText("Bad event" + emoji.toString());
				} else if (ratedVal < 2) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F624)));
					rating_message.setText("Below Average" + emoji.toString());
				} else if (ratedVal < 3) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F610)));
					rating_message.setText("Okayish" + emoji.toString());
				} else if (ratedVal <= 4) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F643)));
					rating_message.setText("Nice event" + emoji.toString());
				} else if (ratedVal < 5) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F600)));
					rating_message.setText("Brilliant!!" + emoji.toString());
				}
			}
		});

		SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		username = sharedpreferences.getString("username", "");
		SharedPreferences.Editor editor = sharedpreferences.edit();

		btn_event_feedback = (Button) findViewById(R.id.btn_submit_event_feedback);
		btn_event_feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				comment = (EditText) findViewById(R.id.editText_event_feedback);
				if (TextUtils.isEmpty((comment.getText().toString())))
					comment_text = "";
				else
					comment_text = comment.getText().toString();

				if (ratedVal == 0) {
					Context context = getApplicationContext();
					CharSequence text = "Rating must be filled";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}

				attemptFeedback();
			}
		});
	}

	public void attemptFeedback() {
		Log.d("hello", "Attempting Feedback .... ");
		JSONObject obj = new JSONObject();
		if (username.equals("")) {
			Context context = getApplicationContext();
			CharSequence text = "Error filling feedback, make sure you are logged in";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			try {
				obj.accumulate("to_event", event_id);
				obj.accumulate("submiter", username);
				obj.accumulate("rating", ratedVal);
				obj.accumulate("content", comment_text);
			} catch (JSONException e) {
				Log.d("hello", e.toString());
			}
			q.add(new JsonObjectRequest(
					Request.Method.POST,
					getSharedPreferences(getString(R.string.ip_pref), 0).getString("ip","127.0.0.1:8000") + "api/event-feedback/",
					obj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.d("hello9", response.toString());
							Context context = getApplicationContext();
							CharSequence text = "Feedback successfully submitted";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
//							finish();
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d("hello", error.toString());
//						showProgress(false);
							Snackbar.make(findViewById(R.id.event_feedback), "Error. Check your network and try again", Snackbar.LENGTH_SHORT)
									.setAction("Dismiss", new View.OnClickListener() {
										@Override
										public void onClick(View v) {

										}
									}).show();

						}
					}
			));
		}
	}

}
