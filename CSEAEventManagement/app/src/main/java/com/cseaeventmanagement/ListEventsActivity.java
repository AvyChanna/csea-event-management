package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import java.util.ArrayList;
import java.util.List;

public class ListEventsActivity extends AppCompatActivity {
	boolean reqinqueue;
	private ProgressBar p;
	private RequestQueue q;
	private RecyclerView recyclerView;
	private JSONObject resp;
	private JSONObject temp;
	private List<List_Event_Data_POJO> List_Events;
	private ListEventAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		reqinqueue = false;
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();
		setContentView(R.layout.activity_list_events);
		p = findViewById(R.id.loading);
		recyclerView = findViewById(R.id.recyclerView);
		List_Events = new ArrayList<>();
		List_Events.add(new List_Event_Data_POJO("A", "A", "A", "A"));
		adapter = new ListEventAdapter(null);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		getEvents();
	}

	private void showProgress(final boolean show) {
		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

		recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
		recyclerView.animate().setDuration(shortAnimTime).alpha(
				show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
			}
		});

		p.setVisibility(show ? View.VISIBLE : View.GONE);
		p.animate().setDuration(shortAnimTime).alpha(
				show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				p.setVisibility(show ? View.VISIBLE : View.GONE);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		q.cancelAll(this.getClass());
	}

	private void getEvents() {
		JsonObjectRequest jor = new JsonObjectRequest(
				Request.Method.POST,
				"http://172.16.115.44:8000/geteventsall/",
				null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("loda", response.toString());
						showProgress(false);
						try {
							resp = new JSONObject(response.toString());
						} catch (Exception e) {
							Log.d("loda", "Malformed JSON");
						}
						checkResponse();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d("loda", error.toString());
						showProgress(false);
						Snackbar.make(recyclerView, "Error getting event data. Check your network and try again", Snackbar.LENGTH_SHORT)
								.setAction("Dismiss", new View.OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
					}
				}
		);
		jor.setTag(this.getClass());
		q.add(jor);
		showProgress(true);
	}

	private void checkResponse() {
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
		Log.d("haha", Boolean.toString(events!=null));
		if (code == 0 && events != null) {
			Log.d("loda","haha4");
			for (int i = 0; i < events.length(); i++) {
				try {
					temp = events.getJSONObject(i);
				} catch (Exception e) {
					Log.d("loda", e.toString());
					Log.d("loda","haha5");
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
					Log.d("loda", e.toString());
					Log.d("loda","haha6");
				}
				Log.d("loda","haha7");
				List_Events.add(new List_Event_Data_POJO(q, w + "," + s, a, ""));
			}
			List_Event_Data_POJO[] myArray = new List_Event_Data_POJO[List_Events.size()];
			for (int j = 0; j < List_Events.size(); j++) {
				myArray[j] = List_Events.get(j);
			}
			Log.d("loda","haha8");
			adapter.setData(myArray);
			adapter.notifyDataSetChanged();
		}
		showProgress(false);

	}
}
