package com.cseaeventmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ListEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        List_Event_Data_POJO[] List_Event_Data_POJO = new List_Event_Data_POJO[] {
                new List_Event_Data_POJO("Email"),
                new List_Event_Data_POJO("Info"),
                new List_Event_Data_POJO("Delete"),
                new List_Event_Data_POJO("Dialer"),
                new List_Event_Data_POJO("Alert"),
                new List_Event_Data_POJO("Map"),
                new List_Event_Data_POJO("Email"),
                new List_Event_Data_POJO("Info"),
                new List_Event_Data_POJO("Delete"),
                new List_Event_Data_POJO("Dialer"),
                new List_Event_Data_POJO("Alert"),
                new List_Event_Data_POJO("Map")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ListEventAdapter adapter = new ListEventAdapter(List_Event_Data_POJO);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}  