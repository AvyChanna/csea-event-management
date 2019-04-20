package com.cseaeventmanagement;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private String old_password;
    private String new_password;
    private String new_confirm_password;
    private RequestQueue q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button btn_submit = findViewById(R.id.btnChangePassword);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View focusView = null;
                boolean cancel = false;

                TextView peep1 = findViewById(R.id.oldPassword);
                old_password = peep1.getText().toString();
                TextView peep2 = findViewById(R.id.newPassword);
                new_password = peep2.getText().toString();
                TextView peep3 = findViewById(R.id.newConfirmPassword);
                new_confirm_password = peep3.getText().toString();
                SharedPreferences sp = getSharedPreferences("Login",MODE_PRIVATE);

                if(TextUtils.isEmpty(peep1.getText().toString()))
                {
                    focusView = peep1;
                    cancel = true;
                    peep1.setError("This field is required");
                }
                if(TextUtils.isEmpty(peep2.getText().toString()))
                {
                    focusView = peep2;
                    cancel = true;
                    peep2.setError("This field is required");
                }
                if(TextUtils.isEmpty(peep3.getText().toString()))
                {
                    focusView = peep3;
                    cancel = true;
                    peep3.setError("This field is required");
                }
                if(!peep1.equals(peep2))
                {
                    focusView = peep3;
                    cancel = true;
                    peep3.setError("Passwords do not match");
                }
                if(!old_password.equals(sp.getString("password","")))
                {
                    focusView = peep1;
                    cancel = true;
                    peep3.setError("Old password is not correct");
                }

                if(cancel)
                    focusView.requestFocus();
                else
                {
                    final JSONObject obj = new JSONObject();
                    try {
                        obj.accumulate("username", sp.getString("username",""));
                        obj.accumulate("old_password", sp.getString("password",""));
                        obj.accumulate("new_password",new_password);
                    } catch (Exception e) {
                        Log.d("hello", e.toString());
                    }
                    JsonObjectRequest jor = new JsonObjectRequest(
                            Request.Method.POST,
                            getString(R.string.ip) + "api-change-pw/",
                            obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("hello", response.toString());
//                                    showProgress(false);
                                    try {
//                                        resp = new JSONObject(response.toString());
                                    } catch (Exception e) {
                                        Log.d("hello", "Malformed JSON");
                                    }
//                                    checkResponse();
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("hello", error.toString());
//                                    showProgress(false);
                                    Snackbar.make(findViewById(R.id.login_form), "Error changing passowrd. Check your network and try again", Snackbar.LENGTH_SHORT)
                                            .setAction("Dismiss", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            }).show();
                                }
                            }
                    );
                    q.add(jor);
//                    showProgress(true);
                }
            }
        });
    }
}
