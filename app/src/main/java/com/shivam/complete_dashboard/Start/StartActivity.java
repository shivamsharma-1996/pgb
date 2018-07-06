package com.shivam.complete_dashboard.Start;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shivam.complete_dashboard.Other.ConnectionDetector;
import com.shivam.complete_dashboard.R;


public class StartActivity extends AppCompatActivity {

    private RadioButton mSellerRadioBtn;
    private RadioButton mSeekerRadioBtn;
    private Button mSignupBtn;
    private Button mSigninBtn;

    //ConnectionDetector
    ConnectionDetector cd;


    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSellerRadioBtn = (RadioButton) findViewById(R.id.start_seller);
        mSeekerRadioBtn = (RadioButton) findViewById(R.id.start_seeker);
        mSignupBtn = (Button) findViewById(R.id.start_signupBtn);
        mSigninBtn = (Button) findViewById(R.id.start_signinBtn);


        //FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

      /*  mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mCurrentUser!=null)
                {
                    //matlab logged tha last time per
                    Intent dashboardIntent =  new Intent(StartActivity.this, DashboardActivity.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboardIntent);
                    finish();
                }
                else
                {
                    Log.i("detected Authstate is:","LOGOUT");
                }
            }
        };*/

        //checking internet availability
        cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent)
        {
            //Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
            builder.setMessage("Please Check your Internet Connection!")
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            return;
        }
        else
        {
            //Toast.makeText(this, "internet available", Toast.LENGTH_LONG).show();

            mSignupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);

                    if (mSellerRadioBtn.isChecked())
                    {
                        reg_intent.putExtra("user", "Seller");
                    }
                    else if (mSeekerRadioBtn.isChecked())
                    {
                        reg_intent.putExtra("user", "Seeker");
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                        builder.setMessage("Please select your identity")
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(false);
                        alert.show();
                        return;
                    }

                    startActivity(reg_intent);
                }
            });

            mSigninBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(StartActivity.this, LoginActivity.class));

                }
            });

        }
    }
}
