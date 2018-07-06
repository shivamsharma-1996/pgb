package com.shivam.complete_dashboard.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.shivam.complete_dashboard.Adapter.FullScreenRecAdapter;
import com.shivam.complete_dashboard.Adapter.detailviewPagerAdapter;
import com.shivam.complete_dashboard.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class FullScreenActivity extends AppCompatActivity {

    private RecyclerView fullScreen_recyclerview;
    private FullScreenRecAdapter fullScreenAdapter;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen);
        String from = getIntent().getExtras().getString("from");
        Log.i("pos", String.valueOf(from));

        mToolbar = (Toolbar) findViewById(R.id.fullScreen_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Preview");
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        //Back button listner
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


        fullScreen_recyclerview = (RecyclerView) findViewById(R.id.fullScreean_recyclerview);
        switch (from)
        {
            case "PreviewActivity" :
                fullScreenAdapter = new FullScreenRecAdapter();
                fullScreenAdapter.setBitmapImgList(detailviewPagerAdapter.getBitmapImgList());
                break;
            case "DetailViewActivity2" :
                fullScreenAdapter = new FullScreenRecAdapter();
                fullScreenAdapter.setImgUrlList(detailviewPagerAdapter.getImgUrlList());
                break;
        }

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        fullScreen_recyclerview.setLayoutManager(verticalLayoutManager);
        fullScreen_recyclerview.setHasFixedSize(true);
        fullScreen_recyclerview.setAdapter(fullScreenAdapter);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        fullScreenAdapter = null;
    }
}
