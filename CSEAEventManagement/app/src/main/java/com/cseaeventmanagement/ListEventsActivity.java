package com.cseaeventmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cseaeventmanagement.ui.eventcard.EventCardFragment;

public class ListEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_events_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EventCardFragment.newInstance())
                    .commitNow();
        }
    }
}
