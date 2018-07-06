package com.shivam.complete_dashboard.Start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.shivam.complete_dashboard.Database.AllEmail;
import com.shivam.complete_dashboard.Nodejs.NodeVolleyRequest;
import com.shivam.complete_dashboard.Other.ConnectionDetector;
import com.shivam.complete_dashboard.Other.MyCustomInterface;
import com.shivam.complete_dashboard.Other.SessionManager;
import com.shivam.complete_dashboard.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shivam sharma on 2/7/2018.
 */

public class RegisterActivity2 extends AppCompatActivity implements MyCustomInterface
{
    private TextInputLayout tilName, tilEmail, tilNumber, tilPassword1, tilPassword2;
    private EditText etName, etEmail, etNumber, etPassword1, etPassword2;
    private RadioButton mSellerRadioBtn, mSeekerRadioBtn;
    private Button mSignupBtn;
    private Button mSigninBtn;

    //ConnectionDetector
    ConnectionDetector cd;
    Boolean isInternetPresent;

    private ProgressDialog mRegProgress, mLoginProgress;

    //firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef;
    private FirebaseUser currentUser;
    private String current_uid;

    public String user_identity = null;

    private User user;

    //checkIsEmailAlreadyExist()
    boolean isEmailAlreadyexist = false;
    AllEmail allEmail;
    Query query = null;
    Query v_query = null;

    private String name, email, number, password1, password2;

    //Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inilizViews();

        //Session class instance
        session = new SessionManager(getApplicationContext());


        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = tilName.getEditText().getText().toString();
                email = tilEmail.getEditText().getText().toString().trim();
                number = tilNumber.getEditText().getText().toString().trim();
                password1 = tilPassword1.getEditText().getText().toString().trim();
                password2 = tilPassword2.getEditText().getText().toString().trim();
                if (mSellerRadioBtn.isChecked()) {
                    user_identity = "Seller";
                } else if (mSeekerRadioBtn.isChecked()) {
                    user_identity = "Seeker";
                } else {
                    Toast.makeText(RegisterActivity2.this, "Please select identity", Toast.LENGTH_LONG).show();
                }


                isEmailAlreadyexist = false;


                validate();
            }
        });

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity2.this, LoginActivity.class));
            }
        });
    }

    private void inilizViews()
    {
        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        //progressDialog
        mRegProgress = new ProgressDialog(this);
        mLoginProgress = new ProgressDialog(this);

        //textInputLayout
        tilName = (TextInputLayout) findViewById(R.id.tilName);
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilNumber = (TextInputLayout) findViewById(R.id.tilNumber);
        tilPassword1 = (TextInputLayout) findViewById(R.id.tilPassword1);
        tilPassword2 = (TextInputLayout) findViewById(R.id.tilPassword2);

        //editText
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etPassword1 = (EditText) findViewById(R.id.etPassword1);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);


        //radioButtons
        mSellerRadioBtn = (RadioButton) findViewById(R.id.start_seller);
        mSeekerRadioBtn = (RadioButton) findViewById(R.id.start_seeker);

        //button
        mSignupBtn = (Button) findViewById(R.id.signup);
        mSigninBtn = (Button) findViewById(R.id.signin);
    }

    private void validate()
    {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!identityValidation()) {
                return;
            }
            if (!nameValidation()) {
                return;
            }

            if (!emailValidation()) {
                return;
            }

            if (!numberValidation()) {
                return;
            }

            if (!passwordValidation()) {
                return;
            }

            showRegProgress();
            register_user();
        }
    }


    private boolean identityValidation() {
        if (user_identity == null) {
            Toast.makeText(RegisterActivity2.this, "Please select identity", Toast.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    private Boolean nameValidation() {
        if (name.isEmpty()) {
            tilName.setError("Name Required");
            etName.requestFocus();
            return false;
        } else {
            if (name.matches("^([A-Za-z]+(\\\\s[A-Za-z]+)?)$"))    //it allow single whitespace between 2(only) words ,where //s stands for sigle whitespace
            {                                                   // ye simple regx h -->   [A-Za-z ]?
                tilName.setErrorEnabled(false);
                return true;
            } else {
                tilName.setError("Invalid Name");
                return false;
            }
        }
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

    @NonNull
    private Boolean numberValidation() {
        if (number.isEmpty()) {
            tilNumber.setError("Number Required");
            etNumber.requestFocus();
            return false;
        } else if (number.length() != 10) {
            tilNumber.setError("Invalid Number");
            etNumber.requestFocus();
            return false;
        } else if (number.length() == 10) {
            tilNumber.setErrorEnabled(false);
        }
        return true;
    }

    private boolean passwordValidation() {
        if (password1.isEmpty()) {
            tilPassword1.setError("Password Required");
            etPassword1.requestFocus();
            return false;
        }
        if (password2.isEmpty()) {
            tilPassword2.setError("Password Required");
            etPassword2.requestFocus();
            return false;
        } else if (password1.length() < 6) {
            tilPassword1.setError("Password Field Must Contains 6 Characters");
            etPassword2.requestFocus();
            return false;
        } else if (!(password1.equals(password2))) {
            tilPassword1.setError("Password Not Matched!");
            tilPassword2.setError("Password Not Matched!");
            tilPassword1.requestFocus();
            return false;
        } else {
            tilPassword1.setErrorEnabled(false);
            tilPassword2.setErrorEnabled(false);
        }
        return true;
    }

    private void showRegProgress()
    {
        mRegProgress.setTitle("Registering User");
        mRegProgress.setMessage("Please wait while we create your account!");
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.setCancelable(false);
        mRegProgress.show();
    }

    private void register_user()
    {
        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                current_uid = currentUser.getUid();

                if (task.isSuccessful())
                {
                    //First, Saving to firebase
                    save_TO_Firebase();

                    //Then, Saving to nodeServer first
                    save_To_nodeServer();
                    Log.i("save_To_nodeServer","saving user to node server");
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

                        //Two cases if email already used:   1)it is unverified  2)or,verified
                        isEmailVerified();
                    }
                    catch(Exception e)
                    {
                        Log.e("TAG", e.getMessage());
                    }
                }
            }
        });
    }


    private void save_TO_Firebase()
    {
        user = new User(name, email, number, password1);

        //adding 2 queries in a single hashmap so that  we can minimized a lot of code & can improve our code
        Map requestMap = new HashMap();
        requestMap.put("Users/" + user_identity + "/" + current_uid, user);
        requestMap.put("AllEmail/" + user_identity + "/" + current_uid, user.getEmail());
        requestMap.put("AuthId/" + current_uid , user_identity);

        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null)
                {
                    Toast.makeText(RegisterActivity2.this, user + " is saved", Toast.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity2.this, user.getEmail() + "saved" , Toast.LENGTH_LONG).show();

                    /*Intent home_intent = new Intent(RegisterActivity2.this, Device_location.class);
                    home_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(new Intent());*/
                }
                else
                {
                    Toast.makeText(RegisterActivity2.this, "error in insert", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    private void save_To_nodeServer()
    {
        //sending POST request to nodejs with new registration details
        NodeVolleyRequest nodeVolleyReq = new NodeVolleyRequest();
        nodeVolleyReq.registerServiceCall(user_identity, name, email, number, password1);

        nodeVolleyReq.send_OTP_Data(this);  //Binding the listener for recieving jsonResponse from NodeVolleyRequest class to this actvity
    }


    private void isEmailVerified()
    {

    }

    @Override
    public void send_OTP_Data(String SERVER_OTP, String identity) throws JSONException {

    }

    @Override
    public void resend_OTP_Data(String SERVER_OTP, String identity) throws JSONException {

    }
}
