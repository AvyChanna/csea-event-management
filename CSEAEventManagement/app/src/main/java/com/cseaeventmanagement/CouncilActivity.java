package com.cseaeventmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    public void func2(View view)
    {
        Intent intent = new Intent(this, Council17_18Activity.class);
        startActivity(intent);
    }

    public void func3(View view)
    {
        Intent intent = new Intent(this, Council_15_16Activity.class);
        startActivity(intent);
    }

    public void func4(View view)
    {
        Intent intent = new Intent(this, Council_14_15Activity.class);
        startActivity(intent);
    }

    public void func5(View view)
    {
        Intent intent = new Intent(this, Council_13_14Activity.class);
        startActivity(intent);
    }


}
