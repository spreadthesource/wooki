package com.wooki.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;

import com.wooki.links.Link;
import com.wooki.links.PageLink;
import com.wooki.links.impl.EditLink;
import com.wooki.links.impl.ViewLink;

/**
 * Extend this class to design a page in pages.book package.
 * 
 * @author ccordenier
 */
public class BookBase extends PageBase
{

    @Property
    private List<Link> publicLinks;

    @Property
    private List<Link> adminLinks;

    @SetupRender
    public void setupMenus()
    {
        publicLinks = new ArrayList<Link>();
        adminLinks = new ArrayList<Link>();

        publicLinks.add(new ViewLink("book/index", "book-home", this.getBookId()));
        publicLinks.add(new ViewLink("book/issues", "all-feedback", this.getBookId()));
        publicLinks.add(new ViewLink("book/pdf", "print-pdf", this.getBookId()));
        publicLinks.add(new ViewLink("book/rss", "rss-feed", this.getBookId()));

        adminLinks.add(new EditLink(getBook(), "book/settings", "settings", getBookId()));
    }

    public void selectAdmin(int idx)
    {
        PageLink link = (PageLink) adminLinks.get(idx);
        link.setSelected(true);
    }

    public void selectMenuItem(int idx)
    {
        PageLink link = (PageLink) publicLinks.get(idx);
        link.setSelected(true);
    }

}
