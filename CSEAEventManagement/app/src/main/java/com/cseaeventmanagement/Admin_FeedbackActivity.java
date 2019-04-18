package com.cseaeventmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Admin_FeedbackActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;

    private RequestQueue q = null;
    private JSONObject resp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__feedback);

        parentLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_admin_feedback_3);


        //Starting Queue
        Network network = new BasicNetwork(new HurlStack());
        q = new RequestQueue(new NoCache(), network);
        q.start();

        //Populate all the comments
        getcomments();
    }

    public void getcomments(){

        //Show all the comments with the USER

        final JSONObject obj = new JSONObject();
        try {
            obj.accumulate("comment", "all_comments");
        } catch (Exception e) {
            Log.d("ADMIN_FEEDBACK_ERROR", e.toString());
        }

        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.api_home) + "ReplaceWithAPI/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

                        //Removing progress bar
                        View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                        parentLinearLayout.removeView(nextChild);

                        Log.d("API_CALL_ERR_SEARCH", error.toString());

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


    public void addcomment() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field2, null);
        // Add the new row
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
    }
}
