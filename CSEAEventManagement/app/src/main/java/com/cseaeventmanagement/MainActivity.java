package com.cseaeventmanagement;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		SharedPreferences sp = getSharedPreferences("OnBoard",MODE_PRIVATE);
		if(sp.getBoolean("first_launch",false)==false)
		{
			Intent onBoard = new Intent(this,OnBoardingActivity.class);
			startActivity(onBoard);
			SharedPreferences.Editor mahEditor = sp.edit();
			mahEditor.putBoolean("first_launch",true);
			mahEditor.apply();
		}

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
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
			Intent intent = new Intent(this, AdminActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_search_events) {
			//Calling Search_Events
			Intent intent = new Intent(this, Search_EventsActivity.class);
			startActivity(intent);

		} else if (id == R.id.nav_feedback_app) {
			gotoFeedback();
		} else if (id == R.id.nav_faq_app) {
			Intent intent = new Intent(this,EventViewActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_logout) {
			Intent intent = new Intent(this, RequestEventActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_csea_core_team) {
			//Calling CouncilActivity
			Intent intent = new Intent(this, CouncilActivity.class);
			startActivity(intent);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private void gotoFeedback() {
		Intent intent = new Intent(this, App_Feedback_Activity.class);
		startActivity(intent);
	}
}
