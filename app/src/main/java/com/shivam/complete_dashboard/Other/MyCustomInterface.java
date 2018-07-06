package com.shivam.complete_dashboard.Other;

import org.json.JSONException;

public interface MyCustomInterface
{
    void send_OTP_Data(String SERVER_OTP, String identity) throws JSONException;
    void resend_OTP_Data(String SERVER_OTP, String identity) throws JSONException;
}