package com.shivam.complete_dashboard.Start;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.shivam.complete_dashboard.Nodejs.NodeVolleyRequest;
import com.shivam.complete_dashboard.R;


public class ForgotPassword extends AppCompatActivity {

    //toolbar
    private Toolbar mToolbar;
    private EditText et_forgotEmail;
    private Button btn_sendPWD;

    private RadioButton mSellerRadioBtn;
    private RadioButton mSeekerRadioBtn;

    String identity="",forgotEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //toolbar set
        mToolbar = (Toolbar) findViewById(R.id.forgotpwd_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_forgotEmail = (EditText) findViewById(R.id.et_forgotEmail);
        mSellerRadioBtn = (RadioButton) findViewById(R.id.forgotPWD_seller);
        mSeekerRadioBtn = (RadioButton) findViewById(R.id.forgotPWD_seeker);
        btn_sendPWD = (Button) findViewById(R.id.btn_sendPWD);

        btn_sendPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotEmail = et_forgotEmail.getText().toString().trim();
                validate();
            }

            private void validate() {
                if (!identityValidation()) {
                    return;
                }
                if (!emailValidation()) {
                    return;
                }

                //<------------------making forgot_password request------------------------>
                new NodeVolleyRequest().forgotPwdServiceCall(forgotEmail, identity);
                Toast.makeText(ForgotPassword.this, "password request has been sent to Server", Toast.LENGTH_SHORT).show();
            }


            private boolean identityValidation()
            {
                identity = "Seller";
                /*if (mSellerRadioBtn.isChecked())
                {

                 return true;
                } else if (mSeekerRadioBtn.isChecked()) {
                    identity = "Seeker";
                    return true;
                }*/

                Toast.makeText(ForgotPassword.this, "Please select your identity", Toast.LENGTH_SHORT).show();
                return false;

            }

            private boolean emailValidation() {
                if (forgotEmail.isEmpty()) {
                    et_forgotEmail.setError("Email Required");
                    et_forgotEmail.requestFocus();
                    return false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches()) {
                    et_forgotEmail.setError("Invalid Email");
                    et_forgotEmail.requestFocus();
                    return false;
                } else if (Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches()) {
                    et_forgotEmail.setText("");
                    return true;
                }
                return true;
            }
        });
    }
}









