package com.wooki.services;

/**
 * Used to handle send error to the browser.
 * 
 * @author ccordenier
 */
public class HttpError
{

    private int status;

    private String message;

    public HttpError(int status, String message)
    {
        super();
        this.status = status;
        this.message = message;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int value)
    {
        this.status = value;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
