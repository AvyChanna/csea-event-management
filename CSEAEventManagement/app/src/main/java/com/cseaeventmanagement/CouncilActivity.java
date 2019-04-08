package com.cseaeventmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

public class CouncilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_council);
    }

    public void func(View view)
    {
        Intent intent = new Intent(this, Council_18_19Activity.class);
        startActivity(intent);
    }
}
