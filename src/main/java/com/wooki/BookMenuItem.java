package com.wooki;

import org.apache.tapestry5.Link;

public class BookMenuItem
{
    private String name;

    private Link link;

    private boolean confirm;

    private String confirmMsg;

    private String assetPath;

    public BookMenuItem(String name, Link link)
    {
        this.name = name;
        this.link = link;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Link getLink()
    {
        return link;
    }

    public void setLink(Link link)
    {
        this.link = link;
    }

    public void setConfirm(boolean confirm)
    {
        this.confirm = confirm;
    }

    public boolean isConfirm()
    {
        return confirm;
    }

    public void setConfirmMsg(String confirmMsg)
    {
        this.confirmMsg = confirmMsg;
    }

    public String getConfirmMsg()
    {
        return confirmMsg;
    }

    public String getAssetPath()
    {
        return assetPath;
    }

    public void setAssetPath(String assetPath)
    {
        this.assetPath = assetPath;
    }
}
