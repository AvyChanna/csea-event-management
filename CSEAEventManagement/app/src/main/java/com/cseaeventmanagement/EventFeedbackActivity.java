// Feedback bharne ke baad vapas event activity details pe bhej dena hai
// abhi usmein ye bhi lena hai, event_id

package com.cseaeventmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.bundled.BundledEmojiCompatConfig;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EventFeedbackActivity extends AppCompatActivity {

    private TextView rating_val;
    private EmojiAppCompatTextView rating_message;
    private RatingBar ratingBar;
    private float ratedVal;
    private Button btn_event_feedback;
    private EditText comment;
    private int event_id;
    private RequestQueue q;
    private JSONObject resp;
    private String comment_text;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
        setContentView(R.layout.activity_event_feedback);

        ratingBar = (RatingBar) findViewById(R.id.event_rating);
        rating_val = (TextView) findViewById(R.id.captcha_feedback);
        rating_message = (EmojiAppCompatTextView) findViewById(R.id.captcha_message);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratedVal = ratingBar.getRating();
                rating_val.setText("Your rating: "+ratedVal+"/5");
                if(ratedVal<1)
                {
                    StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F620)));
                    rating_message.setText("Bad event"+emoji.toString());
                }
                else if(ratedVal<2)
                {
                    StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F624)));
                    rating_message.setText("Below Average"+emoji.toString());
                }
                else if(ratedVal<3)
                {
                    StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F610)));
                    rating_message.setText("Okayish"+emoji.toString());
                }
                else if(ratedVal<=4)
                {
                    StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F643)));
                    rating_message.setText("Nice event");
                }
                else if(ratedVal<5)
                {
                    StringBuilder emoji = new StringBuilder(new String(Character.toChars(0x1F600)));
                    rating_message.setText("Brilliant!!");
                }
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username","");
        SharedPreferences.Editor editor = sharedpreferences.edit();

        btn_event_feedback = (Button) findViewById(R.id.btn_submit_event_feedback);
        btn_event_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = (EditText) findViewById(R.id.editText_event_feedback);
                if(TextUtils.isEmpty((comment.getText().toString())))
                    comment_text ="";
                else
                    comment_text = comment.getText().toString();

                if(ratedVal==0)
                {
                    Context context = getApplicationContext();
                    CharSequence text = "Rating must be filled";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                attemptFeedback();
            }
        });
    }

    public void attemptFeedback()
    {
        JSONObject obj = new JSONObject();
        if(username.equals("")||event_id==-1)
        {
            Context context = getApplicationContext();
            CharSequence text = "Error filling feedback, make sure you are logged in";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            try{
                obj.accumulate("event_id",event_id);
                obj.accumulate("username",username);
                obj.accumulate("Event_Rating",ratedVal);
                obj.accumulate("Event_Feedback_Comment",comment_text);
            }
            catch (JSONException e)
            {
                Log.d("SUBMIT_FEEDBACK",e.toString());
            }
            JsonObjectRequest jor = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.api_home) + "event_feedback/",
                    obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("API_CALL_SUBMIT_FEED", response.toString());
                            Context context = getApplicationContext();
                            CharSequence text = "Feedback successfully submitted";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("API_CALL_ERR_FEEDBACK", error.toString());
//						showProgress(false);
                            Snackbar.make(findViewById(R.id.event_feedback), "Error. Check your network and try again", Snackbar.LENGTH_SHORT)
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
