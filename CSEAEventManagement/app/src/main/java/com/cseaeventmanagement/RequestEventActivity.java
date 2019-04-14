// code to prevent entering of select an item option is to been checked

package com.cseaeventmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class RequestEventActivity extends AppCompatActivity {

    private static final String TAG = "RequestEventActivity";
    private Button eventDateDisplay;
    private Button eventTimePicker;
    private DatePickerDialog.OnDateSetListener eventDateSetListener;
    private ImageView imgView;
    private Button imgSelBut;
    private static final int PICK_IMAGE = 100;
    private int position_programme;
    private int position_stream;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_event);

        populateVenues();

        final Spinner spinner_sel_programme = (Spinner) findViewById(R.id.spinner_request_programme);
        ArrayAdapter<String> myAdapter_sel_programme = new ArrayAdapter<String>(RequestEventActivity.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.branches_super)){
            @Override
            public boolean isEnabled(int position)
            {
                if(position==0)
                {
                    return false;
                }
                else{
                    position_programme = position;

                    return true;
                }
            }

            @Override
            public View getDropDownView(int position,View convertView,ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position==0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }


        };
        myAdapter_sel_programme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        spinner_sel_programme.setAdapter(myAdapter_sel_programme);
        spinner_sel_programme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateStreams(position);
                populateYear(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        imgView = (ImageView) findViewById(R.id.img_request_poster);
        imgSelBut = (Button) findViewById(R.id.btn_request_eventPoster);
        imgView.setVisibility(View.INVISIBLE);
        imgSelBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        eventTimePicker = (Button) findViewById(R.id.btn_request_eventTime);
        eventTimePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                int mMinute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(RequestEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String am_pm = "";
                                if(cal.get(Calendar.AM_PM)==Calendar.AM)
                                    am_pm = "AM";
                                else if(cal.get(Calendar.AM_PM)==Calendar.PM)
                                    am_pm = "PM";
                                eventTimePicker.setText("Selected Time "+hourOfDay+":"+minute+" "+am_pm);
                            }
                        },mHour,mMinute,false);
                timeDialog.show();
            }
        });

        eventDateDisplay = (Button) findViewById(R.id.btn_request_eventDate);
        eventDateDisplay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RequestEventActivity.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        eventDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        eventDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                Log.d(TAG,"Selected Date is: "+dayOfMonth+"/"+month+"/"+year);
                String exact_month = "";
                if(month==1)
                    exact_month="Jan";
                else if(month==2)
                    exact_month="Feb";
                else if(month==3)
                    exact_month="Mar";
                else if(month==4)
                    exact_month="Apr";
                else if(month==5)
                    exact_month="May";
                else if(month==6)
                    exact_month="June";
                else if(month==7)
                    exact_month="July";
                else if(month==8)
                    exact_month="Aug";
                else if(month==9)
                    exact_month="Sep";
                else if(month==10)
                    exact_month="Oct";
                else if(month==11)
                    exact_month="Nov";
                else if(month==12)
                    exact_month="Dec";
                eventDateDisplay.setText("Selected Date: "+dayOfMonth+" "+exact_month+" "+year);
            }
        };
    }

    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            Log.d(imageUri.toString(),"abcd");
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageURI(imageUri);
            imgView.setForeground(getResources().getDrawable(R.drawable.ic_check_black_24dp));
            imgView.invalidate();
        }
    }

    public void populateVenues()
    {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_request);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(RequestEventActivity.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.venue_array)){
            @Override
            public boolean isEnabled(int position)
            {
                if(position==0)
                {
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position,View convertView,ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position==0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter(myAdapter);
    }

    public void populateStreams(int position_programme)
    {
        String[] decision_for_stream;
        if(position_programme==1)
            decision_for_stream = getResources().getStringArray(R.array.branches_btech);
        else if(position_programme==2)
            decision_for_stream = getResources().getStringArray(R.array.branches_bdes);
        else if(position_programme==3)
            decision_for_stream = getResources().getStringArray(R.array.branches_msc);
        else if(position_programme==4)
            decision_for_stream = getResources().getStringArray(R.array.branches_ma);
        else if(position_programme==5)
            decision_for_stream = getResources().getStringArray(R.array.branches_mtech);
        else if(position_programme==6)
            decision_for_stream = getResources().getStringArray(R.array.branches_mdes);
        else if(position_programme==7)
            decision_for_stream = getResources().getStringArray(R.array.branches_msr);
        else if(position_programme==8)
            decision_for_stream = getResources().getStringArray(R.array.branches_phd);
        else if(position_programme==9)
            decision_for_stream = getResources().getStringArray(R.array.branches_cseDual);
        else if(position_programme==10)
            decision_for_stream = getResources().getStringArray(R.array.branches_eeeDual);
        else
            decision_for_stream = getResources().getStringArray(R.array.branches_btech);
        final Spinner spinner_sel_stream = (Spinner) findViewById(R.id.spinner_request_stream);
        ArrayAdapter<String> myAdapter_sel_stream = new ArrayAdapter<String>(RequestEventActivity.this,android.R.layout.simple_list_item_1,decision_for_stream){
            @Override
            public boolean isEnabled(int position)
            {
                if(position==0)
                {
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position,View convertView,ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position==0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        myAdapter_sel_stream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item );
        spinner_sel_stream.setAdapter(myAdapter_sel_stream);
    }


    public void populateYear(int position_programme)
    {
        String[] decision_for_year;
        if(position_programme==1)
            decision_for_year = getResources().getStringArray(R.array.year_btech);
        else if(position_programme==2)
            decision_for_year = getResources().getStringArray(R.array.year_bdes);
        else if(position_programme==3)
            decision_for_year = getResources().getStringArray(R.array.year_msc);
        else if(position_programme==4)
            decision_for_year = getResources().getStringArray(R.array.year_ma);
        else if(position_programme==5)
            decision_for_year = getResources().getStringArray(R.array.year_mtech);
        else if(position_programme==6)
            decision_for_year = getResources().getStringArray(R.array.year_mdes);
        else if(position_programme==7)
            decision_for_year = getResources().getStringArray(R.array.year_msr);
        else if(position_programme==8)
            decision_for_year = getResources().getStringArray(R.array.year_phd);
        else if(position_programme==9)
            decision_for_year = getResources().getStringArray(R.array.year_cseDual);
        else if(position_programme==10)
            decision_for_year = getResources().getStringArray(R.array.year_eeeDual);
        else
            decision_for_year = getResources().getStringArray(R.array.year_btech);

        final Spinner spinner_sel_year = (Spinner) findViewById(R.id.spinner_request_year);
        ArrayAdapter<String> myAdapter_sel_year = new ArrayAdapter<String>(RequestEventActivity.this,android.R.layout.simple_list_item_1,decision_for_year){
            @Override
            public boolean isEnabled(int position)
            {
                if(position==0)
                {
                    return false;
                }
                else{
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position,View convertView,ViewGroup parent) {
                View view = super.getDropDownView(position,convertView,parent);
                TextView tv = (TextView) view;
                if(position==0)
                {
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        myAdapter_sel_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sel_year.setAdapter(myAdapter_sel_year);
    }
}
