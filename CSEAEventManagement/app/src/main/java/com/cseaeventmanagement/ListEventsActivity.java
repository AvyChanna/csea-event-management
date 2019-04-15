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
        List_Event_Data_POJO[] List_Event_Data_POJO = new List_Event_Data_POJO[]{
                new List_Event_Data_POJO("Email", "a", "a", "a"),
                new List_Event_Data_POJO("Infooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo", "a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "a"),
                new List_Event_Data_POJO("Delete", "a", "a", "a"),
                new List_Event_Data_POJO("Dialer", "a", "a", "a"),
                new List_Event_Data_POJO("Alert", "a", "a", "a"),
                new List_Event_Data_POJO("Map", "a", "a", "a"),
                new List_Event_Data_POJO("Email", "a", "a", "a"),
                new List_Event_Data_POJO("Info", "a", "a", "a"),
                new List_Event_Data_POJO("Delete", "a", "a", "a"),
                new List_Event_Data_POJO("Dialer", "a", "a", "a"),
                new List_Event_Data_POJO("Alert", "a", "a", "a"),
                new List_Event_Data_POJO("Map", "a", "a", "a")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        ListEventAdapter adapter = new ListEventAdapter(List_Event_Data_POJO);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}  