package com.summercoding.krakowcityguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("UnusedParameters")
    public void clickItineraries(View view) {
        Intent intent = new Intent(this, ItinerariesActivity.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void clickChecklist(View view) {
        Intent intent = new Intent(this, ChecklistActivity.class);
        intent.putExtra(ChecklistActivity.EXTRA_TITLE, "Must do checklist");
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void clickMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
