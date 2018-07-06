package com.shivam.complete_dashboard.Start;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shivam.complete_dashboard.Activities.MainActivity;
import com.shivam.complete_dashboard.Database.AllEmail;
import com.shivam.complete_dashboard.Other.ConnectionDetector;
import com.shivam.complete_dashboard.Other.SessionManager;
import com.shivam.complete_dashboard.R;


public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private EditText etEmail;
    private EditText etPassword;
    private Button mLoginBtn;
    private Button mSignupBtn;
    private TextView forgotPWD;


    String identity ="none";


    //toolbar
    private Toolbar mToolbar;
    private int checkCount = 0;
    private boolean IS_REGISTERED=false;

    //ConnectionDetector
    ConnectionDetector cd;
    Boolean isInternetPresent;

    //progressDialog
    private ProgressDialog mLoginProgress;

    //firebase auth
    private FirebaseAuth mAuth;

    //firebase database
    private DatabaseReference allEmail_Ref;

     private String email,password;


    //Session Manager Class
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //toolbar set
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

        inilizViews();

        //Session Manager
        session = new SessionManager(getApplicationContext());


        if (!isInternetPresent)
        {
            //Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("Please Check your Internet Connection!")
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Action for 'NO' Button
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
            Toast.makeText(LoginActivity.this, "internet present!", Toast.LENGTH_LONG).show();


            //internet available
            mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    email = tilEmail.getEditText().getText().toString();
                    password = tilPassword.getEditText().getText().toString();


                    validate();
                    
                }
            });

            forgotPWD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent forgotActivtiy = new Intent(LoginActivity.this, ForgotPassword.class);
                    startActivity(forgotActivtiy);
                }
            });

        }
        
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }
    
    

    private void inilizViews() {
        //progressDialog
        mLoginProgress = new ProgressDialog(this);

        //Firebase auth
        mAuth = FirebaseAuth.getInstance();

        //textInputLayout
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);

        //editText
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        //button
        mLoginBtn = (Button) findViewById(R.id.signin);
        mSignupBtn =(Button) findViewById(R.id.signup);

        //textview
        forgotPWD = (TextView) findViewById(R.id.tv_forgotPWD);

        //checking internet availability
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
    }

    private void showProgress()
    {
        mLoginProgress.setTitle("Logging In...");
        mLoginProgress.setMessage("Please wait while we check your credentials.");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.setCancelable(false);
        mLoginProgress.show();
    }
    
    private void validate()
    {
        //Toast.makeText(LoginActivity.this, "validate() called", Toast.LENGTH_LONG).show();

        if (!emailValidation())
        {
            return;
        }
        if (!passwordValidation()) {
            return;
        }

        //checkIsEmailAlreadyUsed();
        showProgress();
        login_user();
    }

    @NonNull
    private Boolean emailValidation() {
        if (email.isEmpty()) {
            tilEmail.setError("Email Required");
            etEmail.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Invalid Email");
            etEmail.requestFocus();
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean passwordValidation() {
        if (password.isEmpty())
        {
            tilPassword.setError("Password Required");
            etPassword.requestFocus();
            return false;
        }
         else if (password.length() < 6)
         {
            tilPassword.setError("Password Field Must Contains 6 Characters");
            etPassword.requestFocus();
            return false;
        }
        else
        {
            tilPassword.setErrorEnabled(false);
        }
        return true;
    }

    //     private void checkIsEmailAlreadyUsed()
//    {
//        Toast.makeText(LoginActivity.this, "checkIsEmailAlreadyUsed() called", Toast.LENGTH_LONG).show();
//
//        allEmail_Ref = FirebaseDatabase.getInstance().getReference();
//        Query query = allEmail_Ref.child("AllEmail").orderByChild("email").equalTo(email);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                Log.i("email:",dataSnapshot.toString());
//                //checkCount++;
//
//                for(DataSnapshot emailSnapshot : dataSnapshot.getChildren()) {
//                    AllEmail Email_Obj = emailSnapshot.getValue(AllEmail.class);
//
//                    if (email.equals(Email_Obj.getEmail()))
//                    {
//                        if (mLoginProgress != null)
//                        {
//                            mLoginProgress.dismiss();
//                        }
//
//
//                       /* if(checkCount==1)                       //check flag se jugad kiya h kuki addValueEventListener do bar call ho rha tha
//                        {
//                            tilEmail.setError("Your email id is alreday exist");
//                            etEmail.requestFocus();
//                        }*/
//
//                        return;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//                Log.i("TAG", "onCancelled: " + databaseError);
//                if (mLoginProgress != null)
//                {
//                    mLoginProgress.dismiss();
//                    Log.i("Cnt signing in firebase", databaseError.toString());
//                }
//                return;
//            }
//        });
//
//        //email id is not  in use so registering the user
//        Toast.makeText(LoginActivity.this, "Email is not  in use!", Toast.LENGTH_LONG).show();
//        //login_user();
//    }



    private void login_user()
    {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    //signingWithSharedPref();
                    Toast.makeText(LoginActivity.this, "Signing in....!", Toast.LENGTH_LONG).show();
                    mLoginProgress.dismiss();

                    //Creating user login session
                    session.createLoginSession(email, identity, password);
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);


                    // clears the all activity tasks in task manager
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch(FirebaseAuthUserCollisionException e)
                    {
                        tilEmail.setError(getString(R.string.error_user_exists));
                        tilEmail.requestFocus();
                    }
                    catch(Exception e)
                    {
                        Log.e("TAG", e.getMessage());
                    }
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "cant sign in. plz enter correct email & password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //<---------------- maaan kr chal rha hu ki registered email hi dalega user------------------>
 /*   private void signingWithSharedPref()
    {
        Log.i("1","1");
        Query query1 = allEmail_Ref.child("AllEmail").child("V_Seller").orderByChild("email").equalTo(email);
        Query query2 = allEmail_Ref.child("AllEmail").child("V_Seeker").orderByChild("email").equalTo(email);
        query1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.i("email:",dataSnapshot.toString());
                if (email.equals(dataSnapshot.getValue(AllEmail.class)))
                {
                    identity = "Seller";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TAG", "onCancelled: " + databaseError);
            }
        });

        query2.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.i("email:",dataSnapshot.toString());
                if (email.equals(dataSnapshot.getValue(AllEmail.class)))
                {
                    identity = "Seeker";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("TAG", "onCancelled: " + databaseError);
            }
        });

        Log.i("2","2");

        //Creating user login session
        session.createLoginSession(email, identity, password);
        Log.i("Session Values:", String.valueOf(session.getUserDetails()));
        Log.i("3","3");

    }*/
}