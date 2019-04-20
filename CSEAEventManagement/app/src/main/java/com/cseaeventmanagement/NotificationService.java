package com.cseaeventmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationService extends JobService {

	private static final String TAG = "Notification Job service";
	private boolean jobCancelled = false;
	private JSONObject temp;
	private RequestQueue q;
	private JSONArray resp;
	public static final String CHANNEL1_ID = "channel1";
	public static final String CHANNEL2_ID = "channel2";
	private NotificationManagerCompat notificationManager;

	@Override
	public boolean onStartJob(JobParameters params) {

		doBackgroundWork(params);

		return true;
	}

	public void doBackgroundWork(final JobParameters params) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (jobCancelled)
					return;
				StringRequest jor = new StringRequest(
						Request.Method.GET,
						getSharedPreferences(getString(R.string.ip_pref), 0).getString("ip","127.0.0.1:8000") + "api/events/",
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								Log.d("hello", response);
								try {
									resp = new JSONArray(response);
								} catch (Exception e) {
									Log.d("hello", "Malformed JSON");
								}
								checkResponse();
							}
						},
						new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						}
				);
				q.add(jor);
				jobFinished(params, true);
			}
		}).start();
	}


	private void checkResponse() {
		if (resp.length() == 0)
			return;
		JSONObject[] events = new JSONObject[resp.length()];

		try {
			for (int i = 0; i < resp.length(); i++)
				events[i] = resp.getJSONObject(i);
		} catch (Exception e) {
		}
		Log.d("hello", "haha4");
		for (int i = 0; i < events.length; i++) {
			String q = "";//name
			String w = "";//date
			String s = "";//id
			String ee = "";//appr
			String a = "";//desc
			String t = "null";
			try {
				ee = events[i].getString("approval");
				q = events[i].getString("name");
				w = events[i].getString("date");
				String[] wt = w.split("-");
				w = wt[2] + "/" + wt[1] + "/" + wt[0];
				if (!events[i].getString("time").equals("null"))
					t = events[i].getString("time");
				a = events[i].getString("summary");
				s = events[i].getString("event_id");
			} catch (Exception e) {
			}
			if (displayEvent(w, t)) {
				createNotificationChannels();
				notificationManager = NotificationManagerCompat.from(this);
				Notification notification = new NotificationCompat.Builder(this, CHANNEL1_ID)
						.setSmallIcon(R.drawable.ic_event_note_black_24dp)
						.setContentTitle(q)
						.setContentText("Today at " + t)
						.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
						.setPriority(NotificationCompat.PRIORITY_HIGH)
						.setCategory(NotificationCompat.CATEGORY_EVENT)
						.build();

				notificationManager.notify(2, notification);
			}
		}
	}

	public boolean displayEvent(String event_date, String event_time) {
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
		if(!event_time.equals("null")) {
			String[] eventTime = event_time.split(":");

			int event_hour = Integer.parseInt(eventTime[0]);
			String[] eventMin = eventTime[1].split(" ");
			int event_min = Integer.parseInt(eventMin[0]);
			String is_am = eventMin[1];
		}
		if (curr_year == event_year && curr_month == event_month && curr_date == event_day) {
			if (curr_hr == 8 && curr_min == 0)
				return true;
		}
		return false;

	}

	public void createNotificationChannels() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel1 = new NotificationChannel(CHANNEL1_ID, "Upcoming Event", NotificationManager.IMPORTANCE_HIGH);
			channel1.setDescription("This is channel1");
			NotificationChannel channel2 = new NotificationChannel(CHANNEL2_ID, "Upcoming Event", NotificationManager.IMPORTANCE_HIGH);
			channel2.setDescription("This is channel2");

			NotificationManager manager = getSystemService(NotificationManager.class);
			manager.createNotificationChannel(channel1);
			manager.createNotificationChannel(channel2);
		}

	}

	@Override
	public boolean onStopJob(JobParameters params) {
		jobCancelled = true;
		return false;
	}
}
