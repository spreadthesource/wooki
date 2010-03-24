package com.wooki.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;

import com.wooki.BookMenuItem;
import com.wooki.LinkType;
import com.wooki.NavLinkPosition;

/**
 * This environmental service is responsible for generating menu elements in the
 * layout. It will use specific elements of the layout to append the different
 * links.
 * 
 * It will have no effect if container elements are not present in the resulting
 * HTML page.
 * 
 * @author ccordenier
 * 
 */
public interface LinkSupport {

	/**
	 * Create and append a new menu item. This item is used to link to another
	 * page.
	 * 
	 * @param type
	 * @param name
	 * @param pageName
	 * @param pageActivationContext
	 * 
	 * @return The corresponding BookItem that can be filled with confirm
	 *         message if needed.
	 */
	BookMenuItem createPageMenuItem(LinkType type, String name, String pageName, Object... pageActivationContext);

	/**
	 * Create and append a new menu item. This item is used to trigger an event.
	 * 
	 * @param type
	 * @param name
	 * @param pageName
	 * @param eventType
	 * @param context
	 * 
	 * @return The corresponding BookItem that can be filled with confirm
	 *         message if needed.
	 */
	BookMenuItem createEventMenuItem(LinkType type, String name, String pageName, String eventType, Object... context);

	/**
	 * Add a link in the menu with its corresponding image.
	 * 
	 * @param type
	 * @param name
	 * @param asset
	 * @param pageName
	 * @param eventType
	 * @param context
	 * 
	 * @return The corresponding BookItem that can be filled with confirm
	 *         message if needed.
	 */
	BookMenuItem createEventMenuItem(LinkType type, String name, String pageName, String nestedId, String eventType, Object... context);

	BookMenuItem createNavLink(NavLinkPosition position, String name, String pageName, Object... context);

	/**
	 * Commit the links element into the resulting markup. After a call to this
	 * method Menu will be created.
	 * 
	 * @param writer
	 */
	void commit(MarkupWriter writer);

}
