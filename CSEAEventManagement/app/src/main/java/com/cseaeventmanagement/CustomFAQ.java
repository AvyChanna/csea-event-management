package com.cseaeventmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CustomFAQ extends AppCompatActivity {

    private Button btn_submit_faq;
    private EditText question;
    private EditText answer;
    private Button faq_done;
    private RequestQueue q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_faq);

        final JSONArray obj_array = new JSONArray();

        Network network = new BasicNetwork(new HurlStack());
        q = new RequestQueue(new NoCache(), network);
        q.start();

        btn_submit_faq = (Button) findViewById(R.id.btn_submit_faq);
        btn_submit_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                question = (EditText) findViewById(R.id.editText_faq_question);
                answer = (EditText) findViewById(R.id.editText_faq_answer);

                View focusView = null;
                boolean cancel = false;

                if(TextUtils.isEmpty(question.getText().toString()))
                {
                    question.setError("This field is required");
                    focusView = question;
                    cancel = true;
                }
                if(TextUtils.isEmpty(answer.getText().toString()))
                {
                    answer.setError("This field is required");
                    focusView = answer;
                    cancel = true;
                }

                if(cancel)
                    focusView.requestFocus();
                else
                {
                    JSONObject obj = new JSONObject();
                    try{
                        obj.accumulate("question",question.getText().toString());
                        obj.accumulate("answer",answer.getText().toString());
                        obj_array.put(obj);

                    } catch (JSONException e) {
                        Log.d("REQUEST_CUST_FAQ_CATCH", e.toString());
                    }
                }
            }
        });

        faq_done = (Button) findViewById(R.id.btn_faq_done);
        faq_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject maha_obj = new JSONObject();
                try{
                    maha_obj.accumulate("context",obj_array);
                }
                catch (JSONException e)
                {
                    Log.d("REQUEST_SEND_FAQ_CATCH", e.toString());
                    return;
                }
                Intent i = new Intent();
                i.putExtra("content", maha_obj.toString());
                setResult(RESULT_OK,i);
                finish();
            }
        });

    }
}
