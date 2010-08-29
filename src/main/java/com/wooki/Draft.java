package com.wooki;

import java.util.Date;

/**
 * This class is used to represent work in progress of the current logged user on a give book.
 * 
 * @author ccordenier
 */
public class Draft
{
    private Date timestamp;

    private String data;

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

}
