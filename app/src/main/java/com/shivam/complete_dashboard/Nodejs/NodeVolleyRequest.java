package com.shivam.complete_dashboard.Nodejs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shivam.complete_dashboard.Other.ApplicationController;
import com.shivam.complete_dashboard.Other.MyCustomInterface;
import com.shivam.complete_dashboard.Start.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shivam sharma on 8/24/2017.
 */


/*
    *Volley sets default Socket & ConnectionTImeout to 5 secs for all requests.
*/


/*
        Volley uses a request queue and you can add multiple requests to that queue.
        The method addToRequestQueue is used for that. By-default,
        upto 4 requests are executed in parallel by Volley,
        so if you make more than 4 requests, they would wait for other requests to finish execution.
*/

public class NodeVolleyRequest
{
    //private String server_url1 = "http://192.168.43.122:3000/json";
    private String server_url1 = "https://pgee-demo.herokuapp.com/json";              //iske response me otp mil rha h
    private String server_url2 = "https://pgee-demo.herokuapp.com/verifyEmail";
    private String server_url3 = "https://pgee-demo.herokuapp.com/forgotPassword";
    private String server_url4 = "https://pgee-demo.herokuapp.com/resendOTP";
    private String server_url5 = "https://pgee-demo.herokuapp.com/sendMail";


    JSONObject jsonBodyObj1, jsonBodyObj2,jsonBodyObj3,jsonBodyObj4,jsonBodyObj5;

    RegisterActivity reg = new RegisterActivity();



    //declare interface object
    private MyCustomInterface myCustomInterface,myCustomInterface1;


    public NodeVolleyRequest()
    {
      //empty COnstructor
    }

    //At the end of the  class, making one method to assign myCustomInterface to interface
    public void send_OTP_Data(MyCustomInterface myCustomInterface)
    {
        this.myCustomInterface=myCustomInterface;
    }

    public void resend_OTP_Data(MyCustomInterface myCustomInterface)
    {
        this.myCustomInterface1=myCustomInterface;
    }


    public void registerServiceCall(String identity, String name, String email, String number, String password) {
        try
         {
             jsonBodyObj1 = new JSONObject();

             jsonBodyObj1.put("identity", identity);
             jsonBodyObj1.put("fullName", name);
             jsonBodyObj1.put("email", email);
             jsonBodyObj1.put("number", number);
             jsonBodyObj1.put("password", password);
             jsonBodyObj1.put("token", "12345678");
         }
         catch (JSONException e)
         {
             Log.i("jsnBodyObj1 cnt created",e.toString());
             e.printStackTrace();
         }

         final String requestBody1 = jsonBodyObj1.toString();

         JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST,
                 server_url1, null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response)
             {
                 try
                 {
                     VolleyLog.v("Response1", response);
                     Log.e("Response1:",response.toString());

                     String status = response.getString("status");
                     String identity = response.getString("identity");
                     String OTP = response.getString("random");

                     if(status.equals("Seller email sent") || status.equals("Seeker email sent"))
                     {
                         myCustomInterface.send_OTP_Data(OTP, identity);
                     }
                     else
                     {
                         Log.e("error in regServiceCall","mail not sent");
                     }
                 }

                 catch (Exception e)
                 {
                     e.printStackTrace();
                     Log.e("Exception1:",e.toString());
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
              //   VolleyLog.e("Error: ", error.getMessage());
                 VolleyLog.e("Error1: ", error);
                 Log.e("eError1:",error.toString());

             }
         }) {
             @Override
             public Map<String, String> getHeaders() throws AuthFailureError
             {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("Content-Type", "application/json");
                 return headers;
             }


             @Override
             public byte[] getBody()
             {
                 try
                 {
                     return requestBody1 == null ? null : requestBody1.getBytes("utf-8");
                 } catch (UnsupportedEncodingException uee) {
                     VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                             requestBody1, "utf-8");
                     return null;
                 }
             }
         };

        //formula-> time = time + (time * Back Off Multiplier);
        jsonObjectRequest1.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1));    //timeout-> 12000 + (12000*1) = 24,000ms(Request dispatched with Socket Timeout of 24 Secs)

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest1);
     }

    public void verifyOTPServiceCall(String email, String identity) {
        try
        {
            jsonBodyObj2 = new JSONObject();

            jsonBodyObj2.put("email",email);
            jsonBodyObj2.put("identity",identity);
        }
        catch (JSONException e)
        {
            Log.i("jsnBodyObj2 cnt created",e.toString());
            e.printStackTrace();
        }

        final String requestBody2 = jsonBodyObj2.toString();

        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST,
                server_url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    VolleyLog.v("Response2", response);
                    Log.e("Response2:",response.toString());
                    //myCustomInterface.send_OTP_Data(response);

                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("Exception2:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   VolleyLog.e("Error: ", error.getMessage());
                VolleyLog.e("Error2: ", error);
                Log.e("eError2:",error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody()
            {
                try {
                    return requestBody2 == null ? null : requestBody2.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody2, "utf-8");
                    return null;
                }
            }
        };

        jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1));   //24 secs timeout time

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest2);
    }

    public void forgotPwdServiceCall(String emailID, String identity) {

        Log.e("emailID" , emailID);
        Log.e("identity" , identity);

        try
        {
            jsonBodyObj3 = new JSONObject();

            jsonBodyObj3.put("email",emailID);
            jsonBodyObj3.put("identity",identity);

            Log.e("jsonBodyObj3" , jsonBodyObj3.toString());

        }
        catch (JSONException e)
        {
            Log.i("jsnBodyObj3 cnt created",e.toString());
            e.printStackTrace();
        }


        final String requestBody3 = jsonBodyObj3.toString();
        Log.e("requestBody3" , requestBody3.toString());

        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.POST,
                server_url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    VolleyLog.v("Response3", response);
                    Log.e("Response3:",response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Exception3:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   VolleyLog.e("Error: ", error.getMessage());
                Log.e("eError3:",error.toString());


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody()
            {
                try {
                    return requestBody3 == null ? null : requestBody3.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody3, "utf-8");
                    return null;
                }
            }
        };

        jsonObjectRequest3.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1));   //24 secs timeout time

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest3);
    }

    public void resendOTPServiceCall(String identity, String email)
    {
        try
        {
            jsonBodyObj4 = new JSONObject();
            jsonBodyObj4.put("identity", identity);
            jsonBodyObj4.put("email", email);
        }
        catch (JSONException e)
        {
            Log.i("jsnBodyObj4 cnt created",e.toString());
            e.printStackTrace();
        }

        final String requestBody4 = jsonBodyObj4.toString();

        JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest(Request.Method.POST,
                server_url4, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    VolleyLog.v("Response4", response);
                    Log.e("Response4:",response.toString());

                    String status = response.getString("status");             //exception handling krni hai  jsonResponse per
                    String OTP = response.getString("random");
                    String identity = response.getString("identity");

                    if(status.equals("Seller email sent") || status.equals("Seeker email sent"))
                    {
                        myCustomInterface1.resend_OTP_Data(OTP, identity);
                    }
                    else
                    {
                        Log.e("error in regServiceCall","mail not sent");
                    }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("Exception4:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   VolleyLog.e("Error: ", error.getMessage());
                VolleyLog.e("Error4: ", error);
                Log.e("eError4:",error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody()
            {
                try
                {
                    return requestBody4 == null ? null : requestBody4.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody4, "utf-8");
                    return null;
                }
            }
        };

        //formula-> time = time + (time * Back Off Multiplier);
        jsonObjectRequest4.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1));    //timeout-> 12000 + (12000*1) = 24,000ms(Request dispatched with Socket Timeout of 24 Secs)

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest4);
    }

    public void send_Ad_Notification_ServiceCall(String email, final Context context) {
        try
        {
            jsonBodyObj5 = new JSONObject();
            jsonBodyObj5.put("email", email);
        }
        catch (JSONException e)
        {
            Log.i("jsnBodyObj5 cnt created",e.toString());
            e.printStackTrace();
        }

        final String requestBody5 = jsonBodyObj5.toString();

        JsonObjectRequest jsonObjectRequest5 = new JsonObjectRequest(Request.Method.POST,
                server_url5, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    VolleyLog.v("Response5", response);
                    Log.e("Response5:",response.toString());

                    String status = response.getString("status");

                    if(status.equals("email sent"))
                    {
                        Toast.makeText(context, "email notification has been sent to seller", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.e("error in regServiceCall","mail not sent");
                    }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e("Exception1:",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   VolleyLog.e("Error: ", error.getMessage());
                VolleyLog.e("Error5: ", error);
                Log.e("eError5:",error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }


            @Override
            public byte[] getBody()
            {
                try
                {
                    return requestBody5 == null ? null : requestBody5.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody5, "utf-8");
                    return null;
                }
            }
        };

        //formula-> time = time + (time * Back Off Multiplier);
        jsonObjectRequest5.setRetryPolicy(new DefaultRetryPolicy(15000, 0, 1));    //timeout-> 12000 + (12000*1) = 24,000ms(Request dispatched with Socket Timeout of 24 Secs)

        ApplicationController.getInstance().addToRequestQueue(jsonObjectRequest5);
    }


}
