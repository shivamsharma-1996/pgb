package com.shivam.complete_dashboard.Database;

/**
 * Created by shivam sharma on 12/6/2017.
 */

public class AuthId
{
    private String identity;

    public AuthId()
    {
        //empty constructor reqired
    }
    public AuthId(String identity)
    {
        this.identity = identity;
    }

    public void setIdentity(String identity)
    {
        this.identity = identity;
    }

    public String getIdentity()
    {
        return identity;
    }
}
