package com.cseaeventmanagement;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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

import org.json.JSONObject;

public class App_Feedback_Activity extends AppCompatActivity {

    // the check should be added such that the user that has submitted the feedback should not submit again

    private RatingBar ratingBar1;
    private float rating_ui;
    private RatingBar ratingBar2;
    private float rating_ux;
    private RatingBar ratingBar3;
    private float rating_overall;
    private String app_feeback_comment;
    private EditText comments;
    private RequestQueue q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__feedback_);

        Network network = new BasicNetwork(new HurlStack());
        q = new RequestQueue(new NoCache(), network);
        q.start();

        ratingBar1 = (RatingBar) findViewById(R.id.app_feedback_rating_ui);
        ratingBar2 = (RatingBar) findViewById(R.id.app_feedback_rating_ux);
        ratingBar3 = (RatingBar) findViewById(R.id.app_feedback_rating_overall);

        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_ui = ratingBar1.getRating();
            }
        });
        ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_ux = ratingBar2.getRating();
            }
        });
        ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_overall = ratingBar3.getRating();
            }
        });

        View focusView = null;
        boolean cancel = false;

        if(rating_ui==0)
        {
            cancel = true;
            focusView = ratingBar1;
        }
        if(rating_ux==0)
        {
            cancel = true;
            focusView = ratingBar2;
        }
        if(rating_overall==0)
        {
            cancel = true;
            focusView = ratingBar3;
        }
        if(cancel)
            focusView.requestFocus();
        else
        {
            JSONObject obj = new JSONObject();
            comments = (EditText) findViewById(R.id.editText_app_comments);
            if(TextUtils.isEmpty(comments.getText().toString()))
                app_feeback_comment = "";
            else
                app_feeback_comment = comments.getText().toString();
            try{
                obj.accumulate("App_UI_Rating",rating_ui);
                obj.accumulate("App_UX_Rating",rating_ux);
                obj.accumulate("App_Overall_Rating",rating_overall);
                obj.accumulate("App_Feedback_Comment",app_feeback_comment);
            }
            catch (Exception e)
            {
                Log.d("APP_FEEDBACK_SUBMIT",e.toString());
            }
            JsonObjectRequest jor = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://172.16.115.46:8000/api/login/",
                    obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("API_FEEDBACK_SUBMIT", response.toString());
                            Context context = getApplicationContext();
                            CharSequence text = "Feedback successfully submitted";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context,text,duration);
                            toast.show();
//                            showProgress(false);
                            // finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("API_CALL_ERR_FEEDBACK", error.toString());
//                            showProgress(false);
                            Snackbar.make(findViewById(R.id.request_event), "Error in submission. Check your network and try again", Snackbar.LENGTH_SHORT)
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

    }
}
