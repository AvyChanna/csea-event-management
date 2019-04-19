package com.cseaeventmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventViewActivity extends AppCompatActivity {

	private int event_id=2;
	private JSONObject resp;
	private RequestQueue q;
	private String event_name = "";
	private String event_details = "";
	private String event_date = "";
	private String event_time = "";
	private String event_venue = "";
	private int event_fee = 0;
	private JSONArray get_faq;
	private String event_target_audience = "";
	private String event_poster = "";
	private ImageView poster_image;
	private Button btn_event_feedback;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);

		Context context = getApplicationContext();
		poster_image.setMaxHeight(getScreenWidth(context));

		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();


		// if a user is not logged in, he/she must not see the give feedback button
		SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
		String checker_login = sharedpreferences.getString("username","");
		if(checker_login.equals(""))
		{
			btn_event_feedback = (Button) findViewById(R.id.btn_give_event_feedback);
			btn_event_feedback.setVisibility(View.INVISIBLE);
			// TODO if a user has submitted a feedback, he/she can't submit again
		}
		else
		{
//			isFeedbackSubmitted();
		}

		TextView marque = (TextView) findViewById(R.id.eventName);
		marque.setSelected(true);

		attemptGetEventData();

	}

	public static int getScreenWidth(Context context)
	{
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		int widthInDP = Math.round(dm.widthPixels / dm.density);
		return widthInDP;
	}

	public void attemptGetEventData()
	{
		JSONObject obj = new JSONObject();
		try{
			obj.accumulate("event_id",event_id);
		}
		catch (JSONException e)
		{
			Log.d("REQUEST_EVENT_DETAILS",e.toString());
		}
		JsonObjectRequest jor = new JsonObjectRequest(
				Request.Method.POST,
				getString(R.string.ip) + "getevent/",
				obj,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("API_CALL_EVENT_DATA", response.toString());
						try {
							resp = new JSONObject(response.toString());
						} catch (Exception e) {
							Log.d("API_CALL_EVE_DATACATCH", "Malformed JSON");
						}
						checkResponse();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("API_CALL_ERR_LOGIN", error.toString());
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

	public void checkResponse()
	{
		Boolean accepted = false;
		String error_message = "";
		int error_code=-1;
		try{
			try
			{
				accepted = resp.getBoolean("accepted");
			}
			catch(Exception e){}
			try{error_code = resp.getInt("error_code");}
			catch(Exception e){}
			try{event_name = resp.getString("Event_Name");}
			catch(Exception e){}
			try{event_details = resp.getString("Event_Description");}
			catch(Exception e){}
			try{event_date = resp.getString("Event_Date");}
			catch(Exception e){}
			try{event_time = resp.getString("Event_Time");}
			catch(Exception e){}
			try{event_venue = resp.getString("Event_Venue");}
			catch(Exception e){}
			try{event_fee = resp.getInt("Event_Fee");}
			catch(Exception e){}
			try{error_message = resp.getString("error_message");}
			catch(Exception e){}
			try{event_target_audience = resp.getString("Event_Target_Audience");}
			catch(Exception e){}
			try{event_poster = resp.getString("Event_Poster");}
			catch(Exception e){}
			try{get_faq = resp.getJSONArray("Event_FAQs");}
			catch(Exception e){}
		}
		catch (Exception e)
		{
			Log.d("CHECK_EVENT_DATA_RESP", "try catch error");
		}
		if (!(accepted && error_code == 0)) {
			Snackbar.make(findViewById(R.id.event_view_activity), error_message, Snackbar.LENGTH_SHORT)
					.setAction("Dismiss", new View.OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();

			return;
		}
		populateEvent();
	}

	public void populateEvent()
	{
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
		TextView text_fee = (TextView) findViewById(R.id.eventFee);
		text_fee.setText(Integer.toString(event_fee));

		// code snippet to check current system date with event start time
		checkEventandSystemDates();


		String [] arr = event_target_audience.split(";");
		final TextView[] myTextViews = new TextView[arr.length]; // create an empty array;

		for(int i=0;i<arr.length;i++)
		{
			final TextView rowTextView = new TextView(this);
			String [] peep = arr[i].split(",");
			// set some properties of rowTextView or something
			int temp1 = Integer.parseInt(peep[0]);
			String [] prog_array = getResources().getStringArray(R.array.branches_super);
			String programme = prog_array[temp1];

			int temp2;
			int temp3;

			if(temp1==1)
			{
				temp2 = Integer.parseInt(peep[1]);
				String [] stream_array = getResources().getStringArray(R.array.branches_btech);
				String stream = stream_array[temp2];
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_btech);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Department: "+stream+" "+"Year: "+year);
			}
			else if(temp1==2)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_bdes);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}
			else if(temp1==3)
			{
				temp2 = Integer.parseInt(peep[1]);
				String [] stream_array = getResources().getStringArray(R.array.branches_msc);
				String stream = stream_array[temp2];
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_msc);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Department: "+stream+" "+"Year: "+year);
			}
			else if(temp1==4)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_ma);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}
			else if(temp1==5)
			{
				temp2 = Integer.parseInt(peep[1]);
				String [] stream_array = getResources().getStringArray(R.array.branches_mtech);
				String stream = stream_array[temp2];
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_mtech);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Department: "+stream+" "+"Year: "+year);
			}
			else if(temp1==6)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_mdes);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}
			else if(temp1==7)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_msr);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}
			else if(temp1==8)
			{
				temp2 = Integer.parseInt(peep[1]);
				String [] stream_array = getResources().getStringArray(R.array.branches_phd);
				String stream = stream_array[temp2];
				rowTextView.setText(programme+" "+"Department: "+stream);
			}
			else if(temp1==9)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_cseDual);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}
			else if(temp1==10)
			{
				temp3 = Integer.parseInt(peep[2]);
				String [] year_array = getResources().getStringArray(R.array.year_eeeDual);
				String year = year_array[temp3];

				rowTextView.setText(programme+" "+"Year: "+year);
			}

			// add the textview to the linearlayout
			LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_target_audi);
			myLinearLayout.addView(rowTextView);

			// save a reference to the textview for later
			myTextViews[i] = rowTextView;
		}

		byte[] decodedString = Base64.decode(event_poster,Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
		poster_image.setImageBitmap(decodedByte);
		LinearLayout image_wala = (LinearLayout) findViewById(R.id.display_poster_image);
		image_wala.addView(poster_image);

		int num_faqs = get_faq.length();
		final TextView[] questions = new TextView[num_faqs];
		final TextView[] answers = new TextView[num_faqs];
		final View[] view = new View[num_faqs];

		JSONObject obj=null;

		for(int i=0;i<num_faqs;i++)
		{
			LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.display_faq);
			final TextView rowQuestion = new TextView(this);
			final TextView rowAnswer = new TextView(this);
			try
			{
				obj = get_faq.getJSONObject(i);
			}
			catch(Exception e){
				Log.d("JSON_faq_OBJ_CALL",e.toString());
			}

			try{
				rowQuestion.setText(obj.getString("question"));
			}
			catch (Exception e)
			{

			}
			try{
				rowAnswer.setText(obj.getString("answer"));
			}
			catch (Exception e)
			{

			}
			myLinearLayout.addView(rowQuestion);
			myLinearLayout.addView(rowAnswer);
		}

		btn_event_feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO write a check that a logged in user can give only one feedback about the event
				// enabling the feedback button and letting them give feedback
				Intent intent = new Intent(getApplicationContext(),EventFeedbackActivity.class);
				intent.putExtra("event_id",event_id);
				startActivity(intent);
			}
		});



	}

	public void checkEventandSystemDates()
	{
		Date current_time = Calendar.getInstance().getTime();
		String date_time = current_time.toString();
		String [] dateAndTime = date_time.split(" ");
		String [] event_dateAndTime = dateAndTime[3].split(":");
		int curr_hr = Integer.parseInt(event_dateAndTime[0]);
		int curr_min = Integer.parseInt(event_dateAndTime[1]);

		String pattern = "dd-MM-yyyy";
		String dateInString =new SimpleDateFormat(pattern).format(new Date());
		String [] dateArray = dateInString.split("-");
		int curr_year = Integer.parseInt(dateArray[2]);
		int curr_month = Integer.parseInt(dateArray[1]);
		int curr_date = Integer.parseInt(dateArray[0]);

		String [] eventDateArray= event_date.split("-");
		int event_year = Integer.parseInt(eventDateArray[2]);
		int event_month = Integer.parseInt(eventDateArray[1]);
		int event_day = Integer.parseInt(eventDateArray[0]);

		String [] eventTime = event_time.split(":");
		int event_hour = Integer.parseInt(eventTime[0]);
		String [] eventMin = eventTime[1].split(" ");
		int event_min = Integer.parseInt(eventMin[0]);
		String is_am = eventMin[1];

		if(event_year>curr_year)
		{
			btn_event_feedback.setVisibility(View.INVISIBLE);
		}
		if(event_year==curr_year&&event_month>curr_month)
		{
			btn_event_feedback.setVisibility(View.INVISIBLE);
		}
		if(event_year==curr_year&&event_month==curr_month&&event_day>curr_date)
		{
			btn_event_feedback.setVisibility(View.INVISIBLE);
		}

		if(is_am.equals("AM"))
		{
			if(event_hour>curr_hr)
				btn_event_feedback.setVisibility(View.INVISIBLE);
			if(event_hour==curr_hr&&event_min>curr_min)
				btn_event_feedback.setVisibility(View.INVISIBLE);
		}
		else
		{
			if(event_hour+12>curr_hr)
				btn_event_feedback.setVisibility(View.INVISIBLE);
			if(event_hour+12==curr_hr&&event_min>curr_min)
				btn_event_feedback.setVisibility(View.INVISIBLE);
		}
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
