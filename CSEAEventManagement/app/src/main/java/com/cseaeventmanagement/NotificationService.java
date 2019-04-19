package com.cseaeventmanagement;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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
    private JSONObject resp;
    public static final String CHANNEL1_ID = "channel1";
    public static final String CHANNEL2_ID = "channel2";
    private NotificationManagerCompat notificationManager;

    @Override
    public boolean onStartJob(JobParameters params) {

        doBackgroundWork(params);

        return true;
    }

    public void doBackgroundWork(final JobParameters params)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(jobCancelled)
                    return;
                JsonObjectRequest jor = new JsonObjectRequest(
                        Request.Method.POST,
                        "http://172.16.115.46:8000/geteventsall/",
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
                            }
                        }
                );
                jor.setTag(this.getClass());
                q.add(jor);
                jobFinished(params,true);
            }
        }).start();
    }

    public void checkResponse()
    {
        JSONArray events = null;
        int code = -1;
        String message = "";
        try {
            events = resp.getJSONArray("events");
        } catch (Exception e) {
        }
        try {
            code = resp.getInt("error_code");
        } catch (Exception e) {
        }
        try {
            message = resp.getString("error_message");
        } catch (Exception e) {
        }

        if (code == 0 && events != null)
        {
            for (int i = 0; i < events.length(); i++)
            {
                try {
                    temp = events.getJSONObject(i);
                } catch (Exception e) {

                }
                String q = "";
                String w = "";
                String s = "";
                String a = "";
                try {
                    q = temp.getString("event_name");
                    w = temp.getString("event_date");
                    s = temp.getString("event_time");
                    a = temp.getString("event_desc");
                } catch (Exception e) {

                }

                if(displayEvent(w,s))
                {
                    createNotificationChannels();
                    notificationManager = NotificationManagerCompat.from(this);
                    Notification notification = new NotificationCompat.Builder(this,CHANNEL1_ID)
                            .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                            .setContentTitle(q)
                            .setContentText("Today at "+s)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_EVENT)
                            .build();

                    notificationManager.notify(2,notification);
                }

            }
        }

    }

    public boolean displayEvent(String event_date,String event_time)
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

        if(curr_year==event_year&&curr_month==event_month&&curr_date==event_day)
        {
            if(curr_hr==8&&curr_min==0)
                return true;
        }
        return false;

    }

    public void createNotificationChannels()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 =  new NotificationChannel(CHANNEL1_ID,"Upcoming Event", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is channel1");
            NotificationChannel channel2 =  new NotificationChannel(CHANNEL2_ID,"Upcoming Event", NotificationManager.IMPORTANCE_HIGH);
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
