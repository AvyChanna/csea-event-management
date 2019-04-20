package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;

public class EventViewActivity extends AppCompatActivity {

	private String event_id = "";
	private JSONObject resp;
	private RequestQueue q;
	private String event_name = "";
	private String event_details = "";
	private String event_date = "";
	private String event_time = "";
	private String event_venue = "";
	private int event_fee = 0;
	private int capacity = 100;
	private String get_faq;
	private String event_target_audience = "";
	private String event_poster = "None";
	private ImageView poster_image;
	private Button btn_event_feedback;
	private String username;
	private String event_committee = "";
	private String contact_info = "";
	private JSONArray btech_members;
	private JSONArray mtech_members;
	private JSONArray phd_members;

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		int widthInDP = Math.round(dm.widthPixels / dm.density);
		return widthInDP;
	}

	@Override
	public boolean onSupportNavigateUp() {
		finish();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// app icon in action bar clicked; go home
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);
		try {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
		}
		try {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
		}
		Intent i = getIntent();
		event_id = i.getStringExtra("event_id");
		Context context = getApplicationContext();
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();
		showProgress(true);

		// if a user is not logged in, he/she must not see the give feedback button
		SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		String checker_login = sharedpreferences.getString("username", "");
		btn_event_feedback = (Button) findViewById(R.id.btn_give_event_feedback);
		if (checker_login.equals("")) {
			btn_event_feedback.setVisibility(View.INVISIBLE);
			// TODO if a user has submitted a feedback, he/she can't submit again
		} else {
//			isFeedbackSubmitted();
		}

		TextView marque = findViewById(R.id.eventName);
		marque.setSelected(true);
		TextView marque2 = findViewById(R.id.eventDetails);
		marque2.setSelected(true);

		attemptGetEventData();

	}

	private void showProgress(final boolean show) {
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
		final ScrollView mLoginFormView = findViewById(R.id.EventViewForm);
		final ProgressBar mProgressView = findViewById(R.id.progress);
		mLoginFormView.setVisibility(show ? GONE : View.VISIBLE);
		mLoginFormView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mLoginFormView.setVisibility(show ? GONE : View.VISIBLE);
			}
		});

		mProgressView.setVisibility(show ? View.VISIBLE : GONE);
		mProgressView.animate().setDuration(shortAnimTime).alpha(
				show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mProgressView.setVisibility(show ? View.VISIBLE : GONE);
			}
		});

	}

	public void attemptGetEventData() {

		JsonObjectRequest jor = new JsonObjectRequest(
				Request.Method.GET,
				getString(R.string.ip) + "api/events/" + event_id + "/",
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("hello", response.toString());
						try {
							resp = new JSONObject(response.toString());
						} catch (Exception e) {
							Log.d("hello", "Malformed JSON");
						}
						checkResponse();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("hello", error.toString());
//						showProgress(false);
						Snackbar.make(findViewById(R.id.event_view_activity), "Error. Check your network and try again", Snackbar.LENGTH_SHORT)
								.setAction("Dismiss", new View.OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
					}
				}
		);
		q.add(jor);
	}

	public void checkResponse() {
		try {
			event_name = resp.getString("name");
		} catch (Exception e) {
		}
		try {
			event_details = resp.getString("summary");
		} catch (Exception e) {
		}
		try {
			event_date = resp.getString("date");
		} catch (Exception e) {
		}
		try {
			event_time = resp.getString("time");
		} catch (Exception e) {
		}
		try {
			event_venue = resp.getString("venue");
		} catch (Exception e) {
		}
		try {
			event_fee = resp.getInt("fee");
		} catch (Exception e) {
		}
		try {
			// TODO TAGS
//				event_target_audience = resp.getString("tags");
		} catch (Exception e) {
		}
		try {
			event_poster = resp.getString("image_string");
		} catch (Exception e) {
		}
		try {
			get_faq = resp.getString("faq");
		} catch (Exception e) {
		}
		try {
			event_committee = resp.getString("organisors");
		} catch (Exception e) {
		}
		try {
			contact_info = resp.getString("contact_info");
		} catch (Exception e) {
		}
		try {
			capacity = resp.getInt("capacity");
		} catch (Exception e) {
		}
		try {
			btech_members = resp.getJSONArray("invitees_btech");
		} catch (Exception e) {
		}
		try {
			mtech_members = resp.getJSONArray("invitees_mtech");
		} catch (Exception e) {
		}
		try {
			phd_members = resp.getJSONArray("invitees_phd");
		} catch (Exception e) {
		}
		// TODO get invitees
		populateEvent();
	}

	public void populateEvent() {
		TextView text_name = (TextView) findViewById(R.id.eventName);
		text_name.setText(event_name);
		TextView text_details = (TextView) findViewById(R.id.eventDetails);
		text_details.setText(event_details);
		TextView text_date = (TextView) findViewById(R.id.EventDate);
		text_date.setText(event_date);
		TextView text_time = (TextView) findViewById(R.id.EventTime);
		text_time.setText(event_time);
		TextView text_venue = (TextView) findViewById(R.id.eventVenue);
		text_venue.setText(event_venue);
		TextView text_fee = findViewById(R.id.eventFee);
		text_fee.setText(Integer.toString(event_fee));
		TextView text_capacity = (TextView) findViewById(R.id.eventCapacity);
		text_capacity.setText(Integer.toString(capacity));
		TextView contactInfo = (TextView) findViewById(R.id.contact_info);
		contactInfo.setText(contact_info);
		TextView organizors = (TextView) findViewById(R.id.committee);
		organizors.setText(event_committee);

		// code snippet to check current system date with event start time
		checkEventandSystemDates();

		for(int i=0;i<btech_members.length();i++)
		{
			int peep=0;
			try{peep = btech_members.getInt(i);}
			catch (Exception e){}
			final TextView rowTextView = new TextView(this);
			if(peep>0)
			{
				String [] ok = getResources().getStringArray(R.array.branches_btech);
				if(peep<=10)
				{
					rowTextView.setText("BTech "+ok[peep]+" 1st year");
				}
				else if(peep<=20)
				{
					rowTextView.setText("BTech "+ok[peep-10]+" 2nd year");
				}
				else if(peep<=30)
				{
					rowTextView.setText("BTech "+ok[peep-20]+" 3rd year");
				}
				else if(peep<=40)
				{
					rowTextView.setText("BTech "+ok[peep-30]+" 4th year");
				}
			}
			if(!rowTextView.getText().toString().equals(""))
			{
				LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_target_audi);
				myLinearLayout.addView(rowTextView);
			}
		}

		for(int i=0;i<mtech_members.length();i++)
		{
			int peep=0;
			try{peep = mtech_members.getInt(i);}
			catch (Exception e){}
			final TextView rowTextView = new TextView(this);
			if(peep>0)
			{
				String [] ok = getResources().getStringArray(R.array.branches_btech);
				if(peep<=10)
				{
					rowTextView.setText("MTech "+ok[peep]+" 1st year");
				}
				else if(peep<=20)
				{
					rowTextView.setText("MTech "+ok[peep-10]+" 2nd year");
				}
			}
			if(!rowTextView.getText().toString().equals(""))
			{
				LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_target_audi);
				myLinearLayout.addView(rowTextView);
			}
		}

		for(int i=0;i<phd_members.length();i++)
		{
			int peep=0;
			try{peep = phd_members.getInt(i);}
			catch (Exception e){}
			final TextView rowTextView = new TextView(this);
			if(peep>0)
			{
				String [] ok = getResources().getStringArray(R.array.branches_btech);
				if(peep<=10)
				{
					rowTextView.setText("PHD "+ok[peep]);
				}
			}
			if(!rowTextView.getText().toString().equals(""))
			{
				LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_target_audi);
				myLinearLayout.addView(rowTextView);
			}
		}

//		String[] arr = event_target_audience.split(";");
//		final TextView[] myTextViews = new TextView[arr.length]; // create an empty array;
//
//		for (int i = 0; i < arr.length; i++) {
//			final TextView rowTextView = new TextView(this);
//			String[] peep = arr[i].split(",");
//			// set some properties of rowTextView or something
//			int temp1 = Integer.parseInt(peep[0]);
//			String[] prog_array = getResources().getStringArray(R.array.branches_super);
//			String programme = prog_array[temp1];
//
//			int temp2;
//			int temp3;
//			// TODO change logic for invitees
//
//			if (temp1 == 1) {
//				temp2 = Integer.parseInt(peep[1]);
//				String[] stream_array = getResources().getStringArray(R.array.branches_btech);
//				String stream = stream_array[temp2];
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_btech);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Department: " + stream + " " + "Year: " + year);
//			} else if (temp1 == 2) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_bdes);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			} else if (temp1 == 3) {
//				temp2 = Integer.parseInt(peep[1]);
//				String[] stream_array = getResources().getStringArray(R.array.branches_msc);
//				String stream = stream_array[temp2];
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_msc);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Department: " + stream + " " + "Year: " + year);
//			} else if (temp1 == 4) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_ma);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			} else if (temp1 == 5) {
//				temp2 = Integer.parseInt(peep[1]);
//				String[] stream_array = getResources().getStringArray(R.array.branches_mtech);
//				String stream = stream_array[temp2];
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_mtech);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Department: " + stream + " " + "Year: " + year);
//			} else if (temp1 == 6) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_mdes);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			} else if (temp1 == 7) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_msr);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			} else if (temp1 == 8) {
//				temp2 = Integer.parseInt(peep[1]);
//				String[] stream_array = getResources().getStringArray(R.array.branches_phd);
//				String stream = stream_array[temp2];
//				rowTextView.setText(programme + " " + "Department: " + stream);
//			} else if (temp1 == 9) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_cseDual);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			} else if (temp1 == 10) {
//				temp3 = Integer.parseInt(peep[2]);
//				String[] year_array = getResources().getStringArray(R.array.year_eeeDual);
//				String year = year_array[temp3];
//
//				rowTextView.setText(programme + " " + "Year: " + year);
//			}
//
//			// add the textview to the linearlayout
//			LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_target_audi);
//			myLinearLayout.addView(rowTextView);
//
//			// save a reference to the textview for later
//			myTextViews[i] = rowTextView;
//		}
		if (!event_poster.equals("None")) {
			Log.d("hello4", event_poster);
			byte[] decodedString = Base64.decode(event_poster, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
			Log.d("hello4", Integer.toString(decodedString.length));
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			poster_image = new ImageView(this);
			poster_image.setMaxHeight(getScreenWidth(this));
			poster_image.setImageBitmap(decodedByte);
			LinearLayout image_wala = (LinearLayout) findViewById(R.id.display_poster_image);
			image_wala.addView(poster_image);
		}
		// displaying event committee
		TextView peep = (TextView) findViewById(R.id.committee);
		final TextView committee_members = new TextView(this);
		committee_members.setText(event_committee);
//		peep.addView(committee_members);

		// displaying faqs
		String[] faq_array = get_faq.split("\\|");
		int num_faqs = faq_array.length;
		if (get_faq.equals(null) || get_faq.equals("") || get_faq.equals("null"))
			findViewById(R.id.faq_group).setVisibility(GONE);

		final TextView[] questions = new TextView[num_faqs];
		final TextView[] answers = new TextView[num_faqs];
		final View[] view = new View[num_faqs];

		for (int i = 0; i < num_faqs; i++) {
			LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_faq);
			final TextView rowQuestion = new TextView(this);
			final TextView rowAnswer = new TextView(this);
			String[] faq_qa = faq_array[i].split("=");
			try {
				rowQuestion.setText("Question: "+faq_qa[0]+"?");
			} catch (Exception e) {

			}
			try {
				rowAnswer.setText("Answer: " + faq_qa[1]);
			} catch (Exception e) {

			}
			myLinearLayout.addView(rowQuestion);
			myLinearLayout.addView(rowAnswer);
		}

		btn_event_feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// enabling the feedback button and letting them give feedback
				Intent intent = new Intent(getApplicationContext(), EventFeedbackActivity.class);
				intent.putExtra("event_id", event_id);
				startActivity(intent);
			}
		});
		showProgress(false);
	}

	public void checkEventandSystemDates() {
		Date current_time = Calendar.getInstance().getTime();
		String date_time = current_time.toString();
		String[] dateAndTime = date_time.split(" ");
		String[] event_dateAndTime = dateAndTime[3].split(":");
		int curr_hr = Integer.parseInt(event_dateAndTime[0]);
		int curr_min = Integer.parseInt(event_dateAndTime[1]);

		String pattern = "dd-MM-yyyy";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		String[] dateArray = dateInString.split("-");
		int curr_year = Integer.parseInt(dateArray[2]);
		int curr_month = Integer.parseInt(dateArray[1]);
		int curr_date = Integer.parseInt(dateArray[0]);

		String[] eventDateArray = event_date.split("-");
		int event_year = Integer.parseInt(eventDateArray[2]);
		int event_month = Integer.parseInt(eventDateArray[1]);
		int event_day = Integer.parseInt(eventDateArray[0]);
		if (event_time.equals("")) {
			String[] eventTime = event_time.split(":");
			int event_hour = Integer.parseInt(eventTime[0]);
//		String[] eventMin = eventTime[1].split(" ");
			int event_min = Integer.parseInt(eventTime[1]);
//		String is_am = eventMin[1];

			if (event_year > curr_year) {
				btn_event_feedback.setVisibility(View.INVISIBLE);
			}
			if (event_year == curr_year && event_month > curr_month) {
				btn_event_feedback.setVisibility(View.INVISIBLE);
			}
			if (event_year == curr_year && event_month == curr_month && event_day > curr_date) {
				btn_event_feedback.setVisibility(View.INVISIBLE);
			}

			if (event_hour > curr_hr)
				btn_event_feedback.setVisibility(View.INVISIBLE);
			if (event_hour == curr_hr && event_min > curr_min)
				btn_event_feedback.setVisibility(View.INVISIBLE);
		}
//		if (is_am.equals("AM")) {
//			if (event_hour > curr_hr)
//				btn_event_feedback.setVisibility(View.INVISIBLE);
//			if (event_hour == curr_hr && event_min > curr_min)
//				btn_event_feedback.setVisibility(View.INVISIBLE);
//		} else {
//			if (event_hour + 12 > curr_hr)
//				btn_event_feedback.setVisibility(View.INVISIBLE);
//			if (event_hour + 12 == curr_hr && event_min > curr_min)
//				btn_event_feedback.setVisibility(View.INVISIBLE);
//		}
	}

//	public void isFeedbackSubmitted()
//	{
//		SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
//		username = sharedpreferences.getString("username","");
//		if(username.equals(""))
//		{
//			btn_event_feedback.setVisibility(View.INVISIBLE);
//		}
//		else
//		{
//			JSONObject obj = new JSONObject();
//			try{
//				obj.accumulate("event_id",event_id);
//				obj.accumulate("username",username)
//			}
//			catch (JSONException e)
//			{
//
//			}
//			JsonObjectRequest jor = new JsonObjectRequest(
//					Request.Method.POST,
//					getString(R.string.api_home) + "event-feedback/",
//					obj,
//					new Response.Listener<JSONObject>() {
//						@Override
//						public void onResponse(JSONObject response) {
//							try {
//								resp = new JSONObject(response.toString());
//							} catch (Exception e) {
//
//							}
//							checkFeedback();
//						}
//					},
//					new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError error) {
////						showProgress(false);
//							Snackbar.make(findViewById(R.id.event_view_activity), "Error. Check your network and try again", Snackbar.LENGTH_SHORT)
//									.setAction("Dismiss", new View.OnClickListener() {
//										@Override
//										public void onClick(View v) {
//
//										}
//									}).show();
//						}
//					}
//			);
//			q.add(jor);
//		}
//	}

}
