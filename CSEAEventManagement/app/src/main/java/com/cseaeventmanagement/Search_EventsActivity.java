package com.cseaeventmanagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Search_EventsActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private ImageView imv;
    private RequestQueue q = null;
    private JSONObject resp;
    private boolean LoginInQueue = false;
    private Button b1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__events);

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);


        //Starting Queue
        Network network = new BasicNetwork(new HurlStack());
        q = new RequestQueue(new NoCache(), network);
        q.start();
        b1=(Button) findViewById(R.id.All_Button);

    }

    public void onAllClick(View v){
        //Show all the events
        if (LoginInQueue)
            return;
        LoginInQueue=true;
        final JSONObject obj = new JSONObject();
        try {
            obj.accumulate("event_name", "all_events");
        } catch (Exception e) {
            Log.d("EVENTS_SEARCH_ERROR", e.toString());
        }

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.ip) + "ReplaceWithAPI/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoginInQueue=false;
                        //Removing progress bar
                        View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                        parentLinearLayout.removeView(nextChild);

                        Log.d("API_CALL_RES_SEARCH", response.toString());


                        try {
                            resp = new JSONObject(response.toString());
                        } catch (Exception e) {
                            Log.d("API_CALL_RES_SEARCH", "Malformed JSON");
                        }

                        // todo checkResponse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LoginInQueue=false;
                        //Removing progress bar
                        View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                        parentLinearLayout.removeView(nextChild);

                        Log.d("API_CALL_ERR_SEARCH", error.toString());

                        Snackbar.make(findViewById(R.id.parent_scroll_view), "Check your network and try again", Snackbar.LENGTH_LONG)
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

    public void onFilterClick(View v){
        if (LoginInQueue)
            return;
        final EditText Et = (EditText) findViewById(R.id.searchbox);
        String name="";
        name=Et.getText().toString();

        /*Snackbar.make(findViewById(R.id.parent_scroll_view), name, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();*/

        if(name.isEmpty()||name.equals(""))        //Show all the events
        {
            //Calling OnAllClick function
            onAllClick(b1);
        }

        else    //Show all the events by the current name
        {
            LoginInQueue=true;
            final JSONObject obj = new JSONObject();
            try {
                obj.accumulate("event_name", name);
            } catch (Exception e) {
                Log.d("EVENTS_SEARCH_ERROR", e.toString());
            }

            JsonObjectRequest jor = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.ip) + "ReplaceWithAPI/",
                    obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            LoginInQueue=false;
                            //Removing progress bar
                            View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                            parentLinearLayout.removeView(nextChild);
                            Log.d("API_CALL_RES_SEARCH", response.toString());
                            //showProgress(false);
                            // todo login hua ya nahi dekhna hai
                            try {
                                resp = new JSONObject(response.toString());
                            } catch (Exception e) {
                                Log.d("API_CALL_RES_SEARCH", "Malformed JSON");
                            }

                            // todo checkResponse();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            LoginInQueue=false;
                            //Removing progress bar
                            View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                            parentLinearLayout.removeView(nextChild);
                            Log.d("API_CALL_ERR_SEARCH", error.toString());
                            //showProgress(false);
                            Snackbar.make(findViewById(R.id.parent_scroll_view), "Check your network and try again", Snackbar.LENGTH_LONG)
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

    }

    private void showProgress(final boolean show) {
        //Removing all the views inside the parentlinearlayout
        for(int index=((ViewGroup)parentLinearLayout).getChildCount()-1; index>=0; index--) {

            View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(index);
            parentLinearLayout.removeView(nextChild);

        }

        //Adding progress animation
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater2.inflate(R.layout.progressbar, null);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);

    }


    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        imv = (ImageView) ((ViewGroup)rowView).getChildAt(0);
        //Add bitmap image to imv
        imv.setImageResource(R.drawable.avengers);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }


}
