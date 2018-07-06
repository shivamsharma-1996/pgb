package com.shivam.complete_dashboard.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.shivam.complete_dashboard.R;

public class BookmarkActivity extends AppCompatActivity {


    //Toolbar
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        mToolbar = (Toolbar) findViewById(R.id.bookmark_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Bookmarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }
}
