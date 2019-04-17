package com.cseaeventmanagement;


import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;


public class EventFeedback extends AppCompatActivity {

    private int event_id;
    private RequestQueue q;
    private JSONObject resp;
    private String event_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_feedback);

        Network network = new BasicNetwork(new HurlStack());
        q = new RequestQueue(new NoCache(), network);
        q.start();

        TextView marque = (TextView) findViewById(R.id.textView_event_feedback_heading);
        marque.setSelected(true);

        attemptFeedback();
    }

    public void attemptFeedback()
    {
        JSONObject obj = new JSONObject();
        try{
            obj.accumulate("event_id",event_id);
        }
        catch (JSONException e)
        {
            Log.d("REQUEST_FEEDBACK_Q",e.toString());
        }
        JsonObjectRequest jor = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.api_home) + "getfeedback/",
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("API_CALL_FEEDBACK_Q", response.toString());
                        try {
                            resp = new JSONObject(response.toString());
                        } catch (Exception e) {
                            Log.d("API_CALL_FDBACK_QCATCH", "Malformed JSON");
                        }
                        checkResponse();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("API_CALL_ERR_LOGIN", error.toString());
//						showProgress(false);
                        Snackbar.make(findViewById(R.id.request_feedback_questions), "Error. Check your network and try again", Snackbar.LENGTH_SHORT)
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
            accepted = resp.getBoolean("accepted");
            error_code = resp.getInt("error_code");
            error_message = resp.getString("error_message");
            event_feedback = resp.getString("Event_Feedback");
        }
        catch (JSONException e) {
            Log.d("REQUEST_FEEDBACK_QCATCH", e.toString());
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
        populateQuestions();
    }

    public void populateQuestions()
    {
        String [] questions = event_feedback.split(";");
        final EditText[] answer_fields = new EditText[questions.length];

        for(int i=0;i<questions.length;i++)
        {
            final TextView ques_text = new TextView(this);
            final EditText ans_text = new EditText(this);

            ans_text.setHint("Write your feedback here");
            ans_text.setLines(5);
            ans_text.setMaxLines(10);
            ans_text.setBackground(getResources().getDrawable(R.drawable.edittext_multiline));

            LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.linear_display_feedback);
            myLinearLayout.addView(ques_text);
            myLinearLayout.addView(ans_text);
        }
    }
}
