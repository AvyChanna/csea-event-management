package com.cseaeventmanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Admin_FeedbackActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    private RequestQueue q = null;
    private JSONArray resp;
    SharedPreferences pref;
    private JSONObject object;
    private float ui_total;
    private float ux_total;
    private float overall_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__feedback);

        parentLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_admin_feedback_3);

	private RequestQueue q = null;
	private JSONObject resp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin__feedback);

		parentLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_admin_feedback_3);
        //Getting shared pref
        pref = getApplicationContext().getSharedPreferences(getString(R.string.ip_pref), 0);

        //Setting rating to 0
        ui_total=0;
        ux_total=0;
        overall_total=0;
	//Starting Queue
		Network network = new BasicNetwork(new HurlStack());
		q = new RequestQueue(new NoCache(), network);
		q.start();
        //Populate all the comments
        getcomments();




    }


        JsonArrayRequest jor = new JsonArrayRequest(
                Request.Method.GET,"http://"+
                pref.getString("ip","127.0.0.1:8000")+"/api/app-feedback/",
                null,
                new Response.Listener<JSONArray>() {
                    //@Override
                    public void onResponse(JSONArray response) {


		final JSONObject obj = new JSONObject();
		try {
			obj.accumulate("comment", "all_comments");
		} catch (Exception e) {
			Log.d("ADMIN_FEEDBACK_ERROR", e.toString());
		}

		JsonObjectRequest jor = new JsonObjectRequest(
				Request.Method.POST,
				getString(R.string.ip) + "ReplaceWithAPI/",
				obj,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						//Removing progress bar
						View nextChild = ((ViewGroup) parentLinearLayout).getChildAt(0);
						parentLinearLayout.removeView(nextChild);

                        try {
                            resp = new JSONArray(response.toString());
                        } catch (Exception e) {
                            Log.d("API_CALL_RES_SEARCH", "Malformed JSON");
                        }

                        checkresponse(resp);
                    }
                },
                new Response.ErrorListener() {
                    //@Override
                    public void onErrorResponse(VolleyError error) {

						// todo checkResponse();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						//Removing progress bar
						View nextChild = ((ViewGroup) parentLinearLayout).getChildAt(0);
						parentLinearLayout.removeView(nextChild);

                        Snackbar.make(findViewById(R.id.scroll_view_admin_feedback), "Check your network and try again", Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

						Snackbar.make(findViewById(R.id.scroll_view_admin_feedback), "Check your network and try again-Feedback", Snackbar.LENGTH_LONG)
								.setAction("Dismiss", new View.OnClickListener() {
									@Override
									public void onClick(View v) {

									}
								}).show();
					}
				}
		);
		q.add(jor);
		showProgress(true);

	}

	private void showProgress(final boolean show) {
		//Removing all the views inside the parentlinearlayout
		for (int index = ((ViewGroup) parentLinearLayout).getChildCount() - 1; index >= 0; index--) {

			View nextChild = ((ViewGroup) parentLinearLayout).getChildAt(index);
			parentLinearLayout.removeView(nextChild);

		}

		//Adding progress animation
		LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater2.inflate(R.layout.progressbar, null);

		// Add the new row before the add field button.
		parentLinearLayout.addView(rowView);

    private void checkresponse(JSONArray resp){
        //Populate parent layout with the views
        int n;
        for( n = 0; n < resp.length(); n++){
            try {
                object = resp.getJSONObject(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addcomment(object);}

        update(n);

    }

    private void update(int n){
        RatingBar ui=(RatingBar) findViewById(R.id.ratingBar_admin_feedback_1);
        RatingBar ux=(RatingBar) findViewById(R.id.ratingBar_admin_feedback_2);
        RatingBar overall=(RatingBar) findViewById(R.id.ratingBar_admin_feedback_3);
        ui.setRating(ui_total/n);
        ux.setRating(ux_total/n);
        overall.setRating(overall_total/n);

    }

    public void addcomment(JSONObject object) {
        String data="";
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field2, null);

        TextView comment=(TextView)  ((ViewGroup)rowView).getChildAt(0);



        //Comment
        try {
            data=object.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(data.isEmpty()||data==null||data.equals(""))
            return;
        comment.setText("Comment: "+data);



        //Ratings
        String ui="";
        String ux="";
        String overall="";
        try {
            ui=object.getString("rating_ui");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ux=object.getString("rating_ux");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            overall=object.getString("rating_overall");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Trying to change to float
        float ui_temp=-1;
        float ux_temp=-1;
        float overall_temp=-1;
        if(ui!=null&&!(ui.isEmpty()))
        {
            ui_temp=Float.parseFloat(ui);
        }
        if(ux!=null&&!(ux.isEmpty()))
        {
            ux_temp=Float.parseFloat(ux);
        }
        if(overall!=null&&!(overall.isEmpty()))
        {
            overall_temp=Float.parseFloat(overall);
        }

        //Updating global float
        if(ui_temp!=-1)
            ui_total+=ui_temp;
        if(ux_temp!=-1)
            ux_total+=ux_temp;
        if(overall_temp!=-1)
            overall_total+=overall_temp;

        // Add the new row
        parentLinearLayout.addView(rowView);
    }
}
