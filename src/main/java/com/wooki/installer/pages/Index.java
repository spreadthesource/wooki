package com.wooki.installer.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.spreadthesource.tapestry.installer.services.ApplicationSettings;
import com.spreadthesource.tapestry.installer.services.Restart;

/**
 * Simple page.
 * 
 * @author ccordenier
 */
public class Index
{
    @Inject
    private ApplicationSettings settings;

    @OnEvent(value = EventConstants.ACTION)
    public Object addConfiguration()
    {
        settings.put("hello", "hi");
        return new Restart();
    }

}
