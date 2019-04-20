package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	boolean reqinqueue;
	private ProgressBar p;
	private RequestQueue q;
	private RecyclerView recyclerView;
	private JSONArray resp;
	private JSONObject temp;
	private List<List_Event_Data_POJO> List_Events;
	private ListEventAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		p = findViewById(R.id.loading);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		reqinqueue = false;
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();

		recyclerView = findViewById(R.id.recyclerViewMain);
		List_Events = new ArrayList<>();
		adapter = new ListEventAdapter(null);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(adapter);
		SharedPreferences sp = getSharedPreferences("OnBoard", MODE_PRIVATE);
		if (sp.getBoolean("first_launch", false) == false) {
			Intent onBoard = new Intent(this, OnBoardingActivity.class);
			startActivity(onBoard);
			SharedPreferences.Editor mahEditor = sp.edit();
			mahEditor.putBoolean("first_launch", true);
			mahEditor.apply();
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(view.getContext(), RequestEventActivity.class);
				startActivity(i);
			}
		});

		getEvents();
		//Calling shared preference
		SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.ip_pref), 0);
		if (!pref.contains("ip")) {
			SharedPreferences.Editor editor = pref.edit();
			editor.clear();
			editor.apply();
			editor.putString("ip", "127.0.0.1:8000");
			editor.commit();
		}
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
			String q = "";
			String w = "";
			String s = "";
			String ee = "";
			String a = "";
			try {
				ee = events[i].getString("approval");
				q = events[i].getString("name");
				w = events[i].getString("date");
				String[] wt = w.split("-");
				w = "Date=" + wt[2] + "/" + wt[1] + "/" + wt[0];
				if (!events[i].getString("time").equals("null"))
					w += ", Time=" + events[i].getString("time");
				a = events[i].getString("summary");
				s = events[i].getString("event_id");
			} catch (Exception e) {
			}
			if (!(ee.equals("Pend")))
				List_Events.add(new List_Event_Data_POJO(q, w, a, s));
		}
		List_Event_Data_POJO[] myArray = new List_Event_Data_POJO[List_Events.size()];
		for (int j = 0; j < List_Events.size(); j++) {
			myArray[j] = List_Events.get(j);
		}
		Log.d("hello", "haha8");
		adapter.setData(myArray);
		adapter.notifyDataSetChanged();
		showProgress(false);
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	private void getEvents() {
		StringRequest jor = new StringRequest(
				Request.Method.GET,
				getString(R.string.ip) + "api/events/",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("hello", response);
						showProgress(false);
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
						Log.d("hello", error.toString());
						showProgress(false);
						Snackbar.make(recyclerView, "Error getting event data. Check your network and try again", Snackbar.LENGTH_SHORT)
								.setAction("Dismiss", new View.OnClickListener() {
									@Override
									public void onClick(View v) {
									}
								}).show();
					}
				});
		jor.setTag(this.getClass());
		q.add(jor);
		showProgress(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_login_signup) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_upcoming_events) {
			Intent intent = new Intent(this, ListEventsActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_past_events) {

		} else if (id == R.id.nav_approve) {
			Intent intent = new Intent(this, Admin_ApproveActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_search_events) {
			//Calling Search_Events
			Intent intent = new Intent(this, Search_EventsActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_feedback_app) {
			Intent intent = new Intent(this, App_Feedback_Activity.class);
			startActivity(intent);
		} else if (id == R.id.nav_faq_app) {
			Intent intent = new Intent(this, EventViewActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_request_event) {
			Intent intent = new Intent(this, RequestEventActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_csea_core_team) {
			//Calling CouncilActivity
			Intent intent = new Intent(this, CouncilActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_view_feedback) {
			//Calling CouncilActivity
			Intent intent = new Intent(this, Admin_FeedbackActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_logout) {
			SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
			SharedPreferences.Editor e = sp.edit();
			e.clear();
			e.apply();
		}
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void scheduleJob(View v) {
		ComponentName componentName = new ComponentName(this, NotificationService.class);
		JobInfo info = new JobInfo.Builder(123, componentName)
				.setPersisted(true)
				.setPeriodic(15 * 60 * 1000)
				.build();

		JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
		int resultCode = scheduler.schedule(info);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		q.cancelAll(this.getClass());
	}

	public void cancelJob(View v) {
		JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
		scheduler.cancel(123);
	}
}
