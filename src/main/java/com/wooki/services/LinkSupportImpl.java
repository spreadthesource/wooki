package com.wooki.services;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.services.RequestPageCache;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import com.wooki.BookMenuItem;
import com.wooki.LinkType;
import com.wooki.NavLinkPosition;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This environmental service is responsible for generating menu elements in the layout. It will use
 * specific elements of the layout to append the different links. It will have no effect if
 * container elements are not present in the resulting HTML page.
 * 
 * @author ccordenier
 */
public class LinkSupportImpl implements LinkSupport
{

    private final LinkSource linkSource;

    private final PageRenderLinkSource pageLinkSource;

    private final RequestPageCache pageCache;

    private final RenderSupport renderSupport;

    private List<BookMenuItem> adminLinks = CollectionFactory.newLinkedList();

    private List<BookMenuItem> actionLinks = CollectionFactory.newLinkedList();;

    private Map<NavLinkPosition, BookMenuItem> navLinks = CollectionFactory.newMap();

    public LinkSupportImpl(LinkSource linkSource, PageRenderLinkSource pageLinkSource,
            RequestPageCache pageCache, RenderSupport renderSupport,
            WookiSecurityContext securityCtx)
    {
        this.linkSource = linkSource;
        this.pageLinkSource = pageLinkSource;
        this.pageCache = pageCache;
        this.renderSupport = renderSupport;
    }

    public BookMenuItem createEventMenuItem(LinkType type, String name, String pageName,
            String eventType, Object... context)
    {
        return this.createEventMenuItem(type, name, pageName, null, eventType, context);
    }

    public BookMenuItem createEventMenuItem(LinkType type, String name, String pageName,
            String nestedId, String eventType, Object... context)
    {
        Link link = this.linkSource.createComponentEventLink(
                pageCache.get(pageName),
                nestedId,
                eventType,
                false,
                context);
        return addToList(type, name, link);
    }

    public BookMenuItem createPageMenuItem(LinkType type, String name, String pageName,
            Object... pageActivationContext)
    {
        Link link = this.pageLinkSource.createPageRenderLinkWithContext(
                pageName,
                pageActivationContext);
        return addToList(type, name, link);
    }

    public BookMenuItem createNavLink(NavLinkPosition position, String name, String pageName,
            Object... context)
    {
        Link link = this.pageLinkSource.createPageRenderLinkWithContext(pageName, context);
        BookMenuItem item = new BookMenuItem(name, link);
        this.navLinks.put(position, item);
        return item;
    }

    private BookMenuItem addToList(LinkType type, String name, Link link)
    {
        BookMenuItem item = new BookMenuItem(name, link);
        if (type == LinkType.ADMIN)
        {
            this.adminLinks.add(item);
        }
        else
        {
            this.actionLinks.add(item);
        }
        return item;
    }

    public void commit(MarkupWriter writer)
    {

        // Write admin menu items
        Element content = writer.getDocument().getElementById("content");

        if (content != null)
        {
            if (this.actionLinks.size() > 0)
            {
                Element actionsMenu = content.elementAt(0, "ul");
                actionsMenu.addClassName("book-menu", "shadowed");
                addItems(this.actionLinks, actionsMenu);
            }

            if (this.adminLinks.size() > 0)
            {
                Element adminMenu = content.elementAt(1, "ul", "id", "adminMenu");
                adminMenu.addClassName("book-admin", "book-menu", "shadowed");
                addItems(this.adminLinks, adminMenu);
            }
        }

        // Add navigation bar
        Element book = writer.getDocument().getElementById("book");
        if (book != null)
        {
            Element navBar = book.elementAt(0, "ul").addClassName("chapter-nav");

            Element current = navBar.element("li", "id", "nav-left", "class", "chapter-nav-item");
            this.addNavItem(NavLinkPosition.LEFT, current);

            current = navBar.element("li", "id", "nav-right", "class", "chapter-nav-item");
            this.addNavItem(NavLinkPosition.RIGHT, current);

            current = navBar.element("li", "id", "book-root", "class", "chapter-nav-item").element(
                    "h1");
            this.addNavItem(NavLinkPosition.CENTER, current);
        }

    }

    /**
     * Write navigation to the menu.
     * 
     * @param position
     * @param current
     */
    private void addNavItem(NavLinkPosition position, Element current)
    {

        if (this.navLinks.containsKey(position))
        {
            BookMenuItem item = this.navLinks.get(position);

            String id = renderSupport.allocateClientId("chapter-nav-item-link");

            Element navLink = current.element("a", "href", item.getLink().toString(), "id", id);
            navLink.raw(item.getName());

            if (item.isConfirm())
            {

                JSONObject params = new JSONObject();
                params.put("lnkId", id);

                if (item.getConfirmMsg() != null) params.put("message", item.getConfirmMsg());

                renderSupport.addInit("initConfirm", params);
            }

        }
        else
        {
            current.raw("&nbsp;");
        }

    }

    /**
     * Helper method to fill an existing element with all its items.
     * 
     * @param items
     * @param root
     */
    private void addItems(List<BookMenuItem> items, Element root)
    {
        int i = 0;
        for (BookMenuItem item : items)
        {
            i++;
            Element line = root.element("li");
            Element link = line.element(
                    "a",
                    "href",
                    item.getLink().toString(),
                    "class",
                    "bookmenuitem");

            if (item.isConfirm())
            {
                String id = renderSupport.allocateClientId("bookmenuitem");
                link.attributes("id", id);

                JSONObject params = new JSONObject();
                params.put("lnkId", id);

                renderSupport.addInit("initConfirm", params);
            }

            link.raw(item.getName());

            // Add an image after text if requested
            if (item.getAssetPath() != null)
            {
                link.element("img", "src", item.getAssetPath());
            }

            if (i == items.size())
            {
                line.addClassName("last");
            }
        }
    }

}
