package com.cseaeventmanagement;

import android.content.Context;
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

import org.json.JSONObject;

public class App_Feedback_Activity extends AppCompatActivity {

	// the check should be added such that the user that has submitted the feedback should not submit again

	private RatingBar ratingBar1;
	private float rating_ui;
	private EmojiAppCompatTextView display_1;
	private TextView display_2;
	private EmojiAppCompatTextView display_3;
	private TextView display_4;
	private EmojiAppCompatTextView display_5;
	private TextView display_6;
	private RatingBar ratingBar2;
	private float rating_ux;
	private RatingBar ratingBar3;
	private float rating_overall;
	private String app_feeback_comment;
	private Button submit_button;
	private EditText comments;
	private RequestQueue q;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
		EmojiCompat.init(config);
		setContentView(R.layout.activity_app__feedback_);

		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();

		ratingBar1 = (RatingBar) findViewById(R.id.app_feedback_rating_ui);
		ratingBar2 = (RatingBar) findViewById(R.id.app_feedback_rating_ux);
		ratingBar3 = (RatingBar) findViewById(R.id.app_feedback_rating_overall);

		display_1 = (EmojiAppCompatTextView) findViewById(R.id.display_1);
		display_2 = (TextView) findViewById(R.id.display_2);
		display_3 = (EmojiAppCompatTextView) findViewById(R.id.display_3);
		display_4 = (TextView) findViewById(R.id.display_4);
		display_5 = (EmojiAppCompatTextView) findViewById(R.id.display_5);
		display_6 = (TextView) findViewById(R.id.display_6);



		ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_ui = ratingBar1.getRating();
				if (rating_ui < 1) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x2639)));
					display_1.setText("Let us know our shortcomings in the feedback" + emoji.toString());
				} else if (rating_ui < 2) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F636)));
					display_1.setText("Not ok?" + emoji.toString());
				} else if (rating_ui < 3) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F62C)));
					display_1.setText("Okayish" + emoji.toString());
				} else if (rating_ui <= 4) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x263A)));
					display_1.setText("Nice" + emoji.toString());
				} else {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F44C)));
					display_1.setText("Thank You!" + emoji.toString());
				}

				display_2.setText("Your rating is: " + rating_ui + "/5");

			}
		});
		ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_ux = ratingBar2.getRating();
				if (rating_ux < 1) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x2639)));
					display_3.setText("Let us know our shortcomings in the feedback" + emoji.toString());
				} else if (rating_ux < 2) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F636)));
					display_3.setText("Not ok?" + emoji.toString());
				} else if (rating_ux < 3) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F62C)));
					display_3.setText("Okayish" + emoji.toString());
				} else if (rating_ux <= 4) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x263A)));
					display_3.setText("Nice" + emoji.toString());
				} else {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F44C)));
					display_3.setText("Thank You!" + emoji.toString());
				}

				display_4.setText("Your rating is: " + rating_ux + "/5");
			}
		});
		ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_overall = ratingBar3.getRating();
				if (rating_overall < 1) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x2639)));
					display_5.setText("Let us know our shortcomings in the feedback" + emoji.toString());
				} else if (rating_overall < 2) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F636)));
					display_5.setText("Not ok?" + emoji.toString());
				} else if (rating_overall < 3) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F62C)));
					display_5.setText("Okayish" + emoji.toString());
				} else if (rating_overall <= 4) {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x263A)));
					display_5.setText("Nice" + emoji.toString());
				} else {
					StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F44C)));
					display_5.setText("Thank You!" + emoji.toString());
				}

				display_6.setText("Your rating is: " + rating_overall + "/5");
			}
		});

		submit_button = (Button) findViewById(R.id.btn_submit_app_feedback);
		submit_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptFeedbackSubmit(rating_ui, rating_ux, rating_overall);
			}
		});

	}

	public void attemptFeedbackSubmit(float rating_ui, float rating_ux, float rating_overall) {
		if (rating_ui == 0 || rating_ux == 0 || rating_overall == 0) {
			Context context = getApplicationContext();
			CharSequence text = "Fill all the entries first";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			JSONObject obj = new JSONObject();
			comments = (EditText) findViewById(R.id.editText_app_comments);
			if (TextUtils.isEmpty(comments.getText().toString()))
				app_feeback_comment = "";
			else {
				app_feeback_comment = comments.getText().toString();
			}
			try {
				obj.accumulate("App_UI_Rating", rating_ui);
				obj.accumulate("App_UX_Rating", rating_ux);
				obj.accumulate("App_Overall_Rating", rating_overall);
				obj.accumulate("App_Feedback_Comment", app_feeback_comment);
			} catch (Exception e) {
				Log.d("hello", e.toString());
			}
			JsonObjectRequest jor = new JsonObjectRequest(
					Request.Method.POST,
					getString(R.string.ip) + "app-feedback/",
					obj,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							Log.d("hello", response.toString());
							Snackbar.make(findViewById(R.id.app_feedback), "Feedback successfully submitted", Snackbar.LENGTH_SHORT)
									.setAction("Dismiss", new View.OnClickListener() {
										@Override
										public void onClick(View v) {

										}
									}).show();
							finish();
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Log.d("hello", error.toString());
							Snackbar.make(findViewById(R.id.app_feedback), "Error in submission. Check your network and try again", Snackbar.LENGTH_SHORT)
									.setAction("Dismiss", new View.OnClickListener() {
										@Override
										public void onClick(View v) {

										}
									}).show();
							finish();
						}
					}
			);
			q.add(jor);
		}
	}
}
