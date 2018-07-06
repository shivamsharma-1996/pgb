package com.shivam.complete_dashboard.Start;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shivam.complete_dashboard.Activities.MainActivity;
import com.shivam.complete_dashboard.Database.AllEmail;
import com.shivam.complete_dashboard.Database.AuthId;
import com.shivam.complete_dashboard.Database.Seeker;
import com.shivam.complete_dashboard.Database.Seller;
import com.shivam.complete_dashboard.Nodejs.NodeVolleyRequest;
import com.shivam.complete_dashboard.Other.ConnectionDetector;
import com.shivam.complete_dashboard.Other.MyCustomInterface;
import com.shivam.complete_dashboard.Other.SessionManager;
import com.shivam.complete_dashboard.R;

import org.json.JSONException;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements MyCustomInterface {

    private TextInputLayout tilName, tilEmail, tilNumber, tilPassword1, tilPassword2;
    private EditText etName, etEmail, etNumber, etPassword1, etPassword2;
/*
    private RadioButton mSellerRadioBtn,mSeekerRadioBtn;
*/
    private Button mSignupBtn;
    private Button mSigninBtn;


    private Toolbar mToolbar;

    //ConnectionDetector
    ConnectionDetector cd;
    Boolean isInternetPresent;

    private ProgressDialog mRegProgress, mLoginProgress;

    //firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;
    private String current_uid;

    public String user_identity = null;

    //Firebase database
    Seller seller;
    Seeker seeker;

    //checkIsEmailAlreadyExist()
    boolean isEmailAlreadyexist = false;
    AllEmail allEmail1,allEmail2;
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


        mSignupBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                name = tilName.getEditText().getText().toString();
                email = tilEmail.getEditText().getText().toString().trim();
                number = tilNumber.getEditText().getText().toString().trim();
                password1 = tilPassword1.getEditText().getText().toString().trim();
                password2 = tilPassword2.getEditText().getText().toString().trim();

                user_identity = "Seller";

               /* if (mSellerRadioBtn.isChecked())
                {
                    user_identity = "Seller";
                }
                else if (mSeekerRadioBtn.isChecked())
                {
                    user_identity = "Seeker";
                }*/
               /* else
                {
                    Toast.makeText(RegisterActivity.this, "Please select identity", Toast.LENGTH_LONG).show();
                }*/


                isEmailAlreadyexist = false;


                validate();
            }
        });

        mSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void inilizViews()
    {
        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

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


      /*  //radioButtons
        mSellerRadioBtn = (RadioButton) findViewById(R.id.start_seller);
        mSeekerRadioBtn = (RadioButton) findViewById(R.id.start_seeker);*/

        //button
        mSignupBtn = (Button) findViewById(R.id.signup);
        mSigninBtn = (Button) findViewById(R.id.signin);
    }
    private void validate()
    {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent)
        {
            Toast.makeText(this, "no internet", Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            if(!identityValidation())
            {
                return;
            }
            if (!nameValidation())
            {
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

            showRegProgress();                                                    //Show loader
            Log.i("isEmailAlreadyexist1", String.valueOf(isEmailAlreadyexist));
            checkIsEmailAlreadyUsed();
        }
    }
    private boolean identityValidation()
    {
        if(user_identity == null)
        {
            Toast.makeText(RegisterActivity.this, "Please select identity",Toast.LENGTH_LONG);
            return false;
        }
        else
        {
            return true;
        }
    }
    private Boolean nameValidation()
    {
        if (name.isEmpty())
        {
            tilName.setError("Name Required");
            etName.requestFocus();
            return false;
        }
        else
        {
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
    private Boolean emailValidation()
    {
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
    private Boolean numberValidation()
    {
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
    private boolean passwordValidation()
    {
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

    private void checkIsEmailAlreadyUsed()           //Two cases if email already used:   1)it is unverified  2)or,verified
    {
        DatabaseReference allEmail_Ref = FirebaseDatabase.getInstance().getReference().child("AllEmail");

        if(user_identity.equals("Seller"))
        {
            query = allEmail_Ref.child("Seller").orderByChild("email").equalTo(email);
            v_query = allEmail_Ref.child("V_Seller").orderByChild("email").equalTo(email);
        }
        else if(user_identity.equals("Seeker"))
        {
            query = allEmail_Ref.child("Seeker").orderByChild("email").equalTo(email);
            v_query = allEmail_Ref.child("V_Seeker").orderByChild("email").equalTo(email);
        }

        //Case 1 of  checking for if email is "Unverified"???
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.i("email:", dataSnapshot.toString());

                if(dataSnapshot.getValue() != null)
                {
                    Log.i("query", "yes");
                    for (DataSnapshot emailSnapshot : dataSnapshot.getChildren()) {
                        AllEmail Email_Obj = emailSnapshot.getValue(AllEmail.class);
                        if (email.equals(Email_Obj.getEmail()))
                        {
                            isEmailAlreadyexist = true;

                            tilEmail.setError("Your email id is unverified so plz check your email for your otp");
                            etEmail.requestFocus();

                            //send request to send resend old OTP again (resend button)
                            NodeVolleyRequest nodeVolleyRequest = new NodeVolleyRequest();
                            nodeVolleyRequest.resendOTPServiceCall(user_identity, email);
                            nodeVolleyRequest.resend_OTP_Data(RegisterActivity.this);     //Binding the listener for recieving jsonResponse from MYVolley class to this actvity
                        }
                    }
                }
                else if (dataSnapshot.getValue() == null)
                {
                    Log.i("v_query", "yes");

                    //if email is not found in "unverified" list, then it may be in "verified" list
                    //Case 2 of checking for if email is "verified"???
                    final ValueEventListener valueEventListener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("email:", dataSnapshot.toString());

                            for (DataSnapshot emailSnapshot : dataSnapshot.getChildren())
                            {
                                AllEmail Email_Obj = emailSnapshot.getValue(AllEmail.class);
                                if (email.equals(Email_Obj.getEmail()))
                                {
                                    if (mRegProgress != null)
                                    {
                                        mRegProgress.dismiss();
                                    }

                                    //showLoginProgress();

                                    tilEmail.setError("Your email id is already registered & verified!!!");
                                    isEmailAlreadyexist = true;
                                }
                            }

                            if(isEmailAlreadyexist != true)
                            {
                                Toast.makeText(RegisterActivity.this, "Email is not used, so registering user!", Toast.LENGTH_LONG).show();
                                register_user();  //email id is not registered becoz it is niether found in "unverified" or "verified" list.                                    }
                            }
                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("TAG", "onCancelled: " + databaseError);
                            if (mRegProgress != null) {
                                mRegProgress.dismiss();
                                Toast.makeText(RegisterActivity.this, databaseError.toString(), Toast.LENGTH_LONG);
                            }

                        }
                    };

                    v_query.addListenerForSingleValueEvent(valueEventListener1);     //attaching the listener to v_query location
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i("TAG", "onCancelled: " + databaseError);
                mRegProgress.dismiss();
                Toast.makeText(RegisterActivity.this, databaseError.toString(), Toast.LENGTH_LONG);
                return;
            }
        });

        Log.i("isEmailAlreadyexist2", String.valueOf(isEmailAlreadyexist));
      //  return isEmailAlreadyexist;
    }

    private void showRegProgress()
    {
        mRegProgress.setTitle("Registering User");
        mRegProgress.setMessage("Please wait while we create your account!");
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.setCancelable(false);
        mRegProgress.show();
    }

    private void showLoginProgress()
    {
        mLoginProgress.setTitle("signing in...");
        mLoginProgress.setMessage("Please wait for a while");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.show();
    }

    private void register_user()
    {
        isEmailAlreadyexist = false;

        mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    current_uid = currentUser.getUid();
                    saveTOAuthID();


                    //Creating user login session
                    session.createLoginSession(email, user_identity, password1);




                    //First, Saving to firebase
                    save_TO_Firebase();




                    //Then, Saving to nodeServer first
                    try
                    {
                        save_To_nodeServer();
                        Log.i("save_To_nodeServer","saving request send to node server");
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                        Log.e("save to nodeServer Eror", e.toString());
                    }
                }
                else
                {
                    if (mRegProgress != null && !isEmailAlreadyexist )
                    {
                        mRegProgress.dismiss();
                        Toast.makeText(RegisterActivity.this, "unable to sign in.plz try again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void save_TO_Firebase()
    {
        if (user_identity.equalsIgnoreCase("Seller")) {
            seller();
        } else if (user_identity.equalsIgnoreCase("Seeker")) {
            seeker();
        }

        saveEmail_toAllEmail();

    }

    private void saveTOAuthID()
    {
        final DatabaseReference AuthIDRef = mDatabaseRef.child("OIDs").child(current_uid);
       // HashMap AuthId = (HashMap) new HashMap().put(current_uid,user_identity);
        AuthId userID = new AuthId(user_identity);
        AuthIDRef.setValue(userID).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "inserted saveTOAuthID ",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Failure in saveTOAuthID ",Toast.LENGTH_LONG).show();
                AuthIDRef.setValue(new HashMap<String,String>().put(current_uid,user_identity));
            }
        });
    }



    private void save_To_nodeServer() throws JSONException
    {
        //sending POST request to nodejs with new registration details
        NodeVolleyRequest nodeVolleyReq =   new NodeVolleyRequest();
        nodeVolleyReq.registerServiceCall(user_identity, name, email, number, password1);

        nodeVolleyReq.send_OTP_Data(RegisterActivity.this); // Binding the listener for recieving jsonResponse from MYVolley class to this actvity
    }


    private void seller()
    {
        seller = new Seller(name, email, number, password1);
        DatabaseReference sellerRef = mDatabaseRef.child("Users").child("Seller").child(current_uid);
        sellerRef.setValue(seller).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    // mRegProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "seller is saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void seeker() {
        seeker = new Seeker(name, email, number, password1);
        DatabaseReference seekerRef = mDatabaseRef.child("Users").child("Seeker").child(current_uid);

        seekerRef.setValue(seeker).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    // mRegProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "seeker is saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveEmail_toAllEmail() {
        allEmail1 = new AllEmail(email);

        DatabaseReference allEmailRef = mDatabaseRef.child("AllEmail").child(user_identity).child(current_uid);

        allEmailRef.setValue(allEmail1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RegisterActivity.this, "saved email", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void send_OTP_Data(String SERVER_OTP, String identity) throws JSONException {
        mRegProgress.dismiss();
        showOTP_Dialog(SERVER_OTP, identity);
    }

    @Override
    public void resend_OTP_Data(String SERVER_OTP, String identity) throws JSONException {
        mRegProgress.dismiss();
        showOTP_Dialog(SERVER_OTP, identity);
    }

    private void showOTP_Dialog(final String SERVER_OTP, final String identity)
    {
        Log.i("Server OTP in Dialog", SERVER_OTP);

        final String[] inserted_OTP = {new String()};

        View promptsView = LayoutInflater.from(this).inflate(R.layout.custom, null);          // get prompts.xml view
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);                        // set prompts.xml to alertdialog builder

        if (isEmailAlreadyexist)
        {
            alertDialogBuilder.setMessage("Email is already registered,resend OTP");
            isEmailAlreadyexist = false;
        }

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                inserted_OTP[0] = userInput.getText().toString().trim();
                Log.i("inserted otp[0]:", inserted_OTP[0]);
                if (inserted_OTP[0].equals(SERVER_OTP)) {
                    userChose("matched", identity);
                } else if (!(inserted_OTP[0].equals(SERVER_OTP))) {
                    userChose("not matched", " rwvrw");
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void userChose(String result, String identity) {
        Log.i("OTP result:", result);

        if (result.equals("matched")) {

            final DatabaseReference mAllEmailRef = FirebaseDatabase.getInstance().getReference().child("AllEmail");

            //<------------------making second request(AuthId + identity) to nodeServer------------------------>
            new NodeVolleyRequest().verifyOTPServiceCall(email, identity);

            Intent mainIntent =  new Intent(RegisterActivity.this, MainActivity.class) ;
            if (identity.equals("Seller"))
            {
                allEmail2 =  new AllEmail(email);
                mAllEmailRef.child("V_Seller").child(current_uid).setValue(allEmail2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAllEmailRef.child("Seller").child(current_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "seller is successfully shifted to V_Seller", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mAllEmailRef.child("Seller").child(current_uid).removeValue();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mAllEmailRef.child("V_Seller").child(current_uid).setValue(allEmail2);
                    }
                });

            } else if (identity.equals("Seeker")) {
            }


            /*// Storing loggedin_email &
            SharedPreferences.Editor editor = getSharedPreferences("logged_in_user", MODE_PRIVATE).edit();
            editor.putString("profile_name", name);
            editor.putString("profile_email", email);
            editor.commit();*/

            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);    //Clears the all activity tasks in task manager
            startActivity(mainIntent);
            finish();
        } else if (result.equals("not matched")) {
            Toast.makeText(this, "Incorrect OTP inserted!!!", Toast.LENGTH_LONG).show();
            Log.i("conclusion", "Incorrect OTP inserted!!!");
        }
    }

    public void show_resend_otp_dialog(String Server_OTP, String identity)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);


        dialog.getWindow().getAttributes().width = ActionBar.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().getAttributes().height = ActionBar.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button resend = (Button) dialog.findViewById(R.id.button1);
        Button verify = (Button) dialog.findViewById(R.id.button2);
        final EditText editText1 = (EditText) dialog.findViewById(R.id.edit1);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.edit2);
        final EditText editText3 = (EditText) dialog.findViewById(R.id.edit3);
        final EditText editText4 = (EditText) dialog.findViewById(R.id.edit4);
        final EditText editText5 = (EditText) dialog.findViewById(R.id.edit5);

        editText1.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText1.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText2.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText3.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText4.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText5.getText().toString().length() == 1)     //size as per your requirement
                {
                    editText1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "resend", Toast.LENGTH_LONG).show();
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
                editText5.setText("");
                editText1.requestFocus();
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1;
                s1 = editText1.getText().toString().trim();
                s1 = s1 + editText2.getText().toString().trim();
                s1 = s1 + editText3.getText().toString().trim();
                s1 = s1 + editText4.getText().toString().trim();
                s1 = s1 + editText5.getText().toString().trim();
                if (s1.length() == 5)
                {
                    Toast.makeText(getApplicationContext(), "verifying....... " + s1, Toast.LENGTH_LONG).show();
                }


                else
                    Toast.makeText(getApplicationContext(), "enter all the entries", Toast.LENGTH_LONG).show();

            }
        });


    }
}














