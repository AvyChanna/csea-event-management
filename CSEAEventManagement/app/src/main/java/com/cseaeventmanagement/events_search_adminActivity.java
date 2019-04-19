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
import android.widget.CheckBox;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class events_search_adminActivity extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private ImageView imv;
    private RequestQueue q = null;
    private JSONArray resp;
    private boolean LoginInQueue = false;
    private Button b1;
    private JSONObject object;
    private String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_search_admin);

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
        /*final JSONObject obj = new JSONObject();
        try {
            obj.accumulate("event_name", "all_events");
        } catch (Exception e) {
            Log.d("EVENTS_SEARCH_ERROR", e.toString());
        }*/

        JsonArrayRequest jor = new JsonArrayRequest(
                Request.Method.GET,
                getString(R.string.api_event_search)+"events/",
                null,
                new Response.Listener<JSONArray>() {
                    //@Override
                    public void onResponse(JSONArray response) {
                        LoginInQueue=false;

                        //Removing progress bar
                        View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                        parentLinearLayout.removeView(nextChild);

                        Log.d("API_CALL_RES_SEARCH", response.toString());


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

            JsonArrayRequest jor = new JsonArrayRequest(
                    Request.Method.GET,
                    getString(R.string.api_event_search)+"events/",
                    null,
                    new Response.Listener<JSONArray>() {
                        //@Override
                        public void onResponse(JSONArray response) {
                            LoginInQueue=false;

                            //Removing progress bar
                            View nextChild = ((ViewGroup)parentLinearLayout).getChildAt(0);
                            parentLinearLayout.removeView(nextChild);

                            Log.d("API_CALL_RES_SEARCH", response.toString());
                            //showProgress(false);


                            try {
                                resp = new JSONArray(response.toString());
                            } catch (Exception e) {
                                Log.d("API_CALL_RES_SEARCH", "Malformed JSON");
                            }

                            checkresponse2(resp,name);
                        }
                    },
                    new Response.ErrorListener() {
                        //@Override
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

    private void checkresponse(JSONArray resp){
        //Populate parent layout with the views

        for(int n = 0; n < resp.length(); n++){
            try {
                object = resp.getJSONObject(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CheckBox c1=(CheckBox) findViewById(R.id.checkBox1);
            CheckBox c2=(CheckBox) findViewById(R.id.checkBox2);
            CheckBox c3=(CheckBox) findViewById(R.id.checkBox3);
            String data="";
            try {
                data=object.getString("approval");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(c1.isChecked()&&data.equals("Appr"))
            {
                add_event(object);
            }
            if(c2.isChecked()&&data.equals("Decl"))
            {
                add_event(object);
            }
            if(c3.isChecked()&&data.equals("Pend"))
            {
                add_event(object);
            }
            }

    }

    private void checkresponse2(JSONArray resp,String name){
        //Populate parent layout with the views having name=="name"

        for(int n = 0; n < resp.length(); n++){
            try {
                object = resp.getJSONObject(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String data="";
            try {
                data=object.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(data.toLowerCase().contains(name.toLowerCase()))
            {
                CheckBox c1=(CheckBox) findViewById(R.id.checkBox1);
                CheckBox c2=(CheckBox) findViewById(R.id.checkBox2);
                CheckBox c3=(CheckBox) findViewById(R.id.checkBox3);
                String data2="";
                try {
                    data2=object.getString("approval");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(c1.isChecked()&&data2.equals("Appr"))
                {
                    add_event(object);
                }
                if(c2.isChecked()&&data2.equals("Decl"))
                {
                    add_event(object);
                }
                if(c3.isChecked()&&data2.equals("Pend"))
                {
                    add_event(object);
                }
            }

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


    public void add_event(JSONObject object) {
        String data="";

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_admin, null);
        imv = (ImageView) ((ViewGroup)rowView).getChildAt(0);
        TextView event_name=(TextView)  ((ViewGroup)rowView).getChildAt(1);
        TextView event_time=(TextView)  ((ViewGroup)rowView).getChildAt(2);
        TextView event_date=(TextView)  ((ViewGroup)rowView).getChildAt(3);
        TextView target_audience=(TextView)  ((ViewGroup)rowView).getChildAt(4);
        TextView event_fee=(TextView)  ((ViewGroup)rowView).getChildAt(5);
        TextView event_requester=(TextView)  ((ViewGroup)rowView).getChildAt(6);
        TextView event_tags=(TextView)  ((ViewGroup)rowView).getChildAt(7);
        TextView event_faqs=(TextView)  ((ViewGroup)rowView).getChildAt(8);
        TextView event_venue=(TextView)  ((ViewGroup)rowView).getChildAt(9);
        TextView contact_info=(TextView)  ((ViewGroup)rowView).getChildAt(10);
        TextView status=(TextView)  ((ViewGroup)rowView).getChildAt(11);
        TextView approval=(TextView)  ((ViewGroup)rowView).getChildAt(12);
        // todo Add bitmap image to imv
        imv.setImageResource(R.drawable.avengers);
        //Adding the text to all textviews


        //event_name
        try {
            data=object.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_name.setText("Name: "+data);

        //event_time
        try {
            data=object.getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_time.setText("Time: "+data);

        //event_date
        try {
            data=object.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedCurrentDate=null;
        try {
            convertedCurrentDate = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        event_date.setText("Date: "+sdf2.format(convertedCurrentDate ));

        //Change color
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        String formattedDate = sdf.format(c);

        if(convertedCurrentDate.compareTo(c)<=0)
        {
            event_date.setTextColor(Color.parseColor("#ff0000"));
        }
        else
        {
            event_date.setTextColor(Color.parseColor("#008000"));
        }

        //target_audience
        try {
            data=object.getString("target_audience");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        target_audience.setText("Target Audience: "+data);

        //event_fee
        try {
            data=object.getString("fee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_fee.setText("Fees: "+data);

        //event_requester
        try {
            data=object.getString("organisors");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_requester.setText("Organizers: "+data);

        //event_tags
        try {
            data=object.getString("tags");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_tags.setText("Tags: "+data);

        //event_faqs
        try {
            data=object.getString("faq");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_faqs.setText("FAQS: "+data);

        //event_venue
        try {
            data=object.getString("venue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        event_venue.setText("Venue: "+data);

        //contact info
        try {
            data=object.getString("contact_info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contact_info.setText("Contact: "+data);

        //Status
        String capacity="";
        String audience="";
        try {
            capacity=object.getString("capacity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            audience=object.getString("curr_audience");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Try to change the strings to ints if not NULL
        int cap=-1,aud=-1;
        if(capacity!=null&&capacity!="")
        {
            cap=Integer.parseInt(capacity);
        }
        if(capacity!=null&&capacity!="")
        {
            aud=Integer.parseInt(audience);
        }

        if(cap!=-1&&aud!=-1)
        {
            if(aud<cap)
            {
                status.setText("Status: Seats available");
                status.setTextColor(Color.parseColor("#008000"));
            }
            else
            {
                status.setText("Status: Event Full");
                status.setTextColor(Color.parseColor("#FF0000"));
            }
        }
        else {
            status.setText("Status: Undecided");
            status.setTextColor(Color.parseColor("#000000"));
        }

        //Approval Status
        try {
            data=object.getString("approval");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(data.equals("Appr"))
        {
            approval.setText("Approval Status: Approved");
            approval.setTextColor(Color.parseColor("#008000"));
        }
        if(data.equals("Pend"))
        {
            approval.setText("Approval Status: Pending");
            approval.setTextColor(Color.parseColor("#0000FF"));
        }
        if(data.equals("Decl"))
        {
            approval.setText("Approval Status: Declined");
            approval.setTextColor(Color.parseColor("#ff0000"));
        }




        // Add the new row

        parentLinearLayout.addView(rowView);

    }

}
