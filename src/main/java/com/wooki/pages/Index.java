//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.pages;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.io.FeedException;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.UserManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.services.HttpError;
import com.wooki.services.activity.ActivitySourceType;
import com.wooki.services.feeds.FeedSource;
import com.wooki.services.security.WookiSecurityContext;

/**
 * Display an index page for wooki application. If no user logged in or requested, then a default
 * signup block will be displayed. If not, then the requested user book list will be displayed.
 * 
 * @author ccordenier
 */
public class Index
{

    @Inject
    private BookManager bookManager;

    @Inject
    private WookiSecurityContext securityCtx;

    @Inject
    private UserManager userManager;

    @Inject
    private Block homeBlock;

    @Inject
    private Block presBlock;

    @Inject
    private Block userBlock;

    @Inject
    private Messages messages;

    @Inject
    private FeedSource feedSource;

    @Inject
    private Request request;

    @Property
    private List<Book> userBooks;

    @Property
    private List<Book> userCollaborations;

    @Property
    private Book currentBook;

    @Property
    private User user;

    @Property
    private int loopIdx;

    @Property
    private DateFormat sinceFormat = new SimpleDateFormat("MMMMM dd, yyyy");

    @Property
    private Block userCtx;

    @Property
    private String flashMessage;

    /**
     * Set current user if someone has logged in.
     * 
     * @return
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public boolean setupListBook()
    {
        if (securityCtx.isLoggedIn())
        {
            this.userCtx = this.homeBlock;
        }
        else
        {
            this.userCtx = this.presBlock;
        }
        return true;
    }

    /**
     * If the user requested is not the user logged in, simply display his list of book.
     * 
     * @param username
     * @return
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupBookList(String username)
    {
        this.user = this.userManager.findByUsername(username);
        if (this.user == null) { return new HttpError(404, "User not found"); }
        this.userCtx = userBlock;
        this.userBooks = this.bookManager.listByOwner(username);
        this.userCollaborations = this.bookManager.listByCollaborator(username);
        return true;
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public String getCurrentUser()
    {
        if (user != null) { return user.getUsername(); }
        return null;
    }

    @OnEvent(value = "userfeed")
    public Feed getUserFeed(Long userId) throws IOException, IllegalArgumentException,
            FeedException
    {
        return feedSource.produceFeed(ActivitySourceType.USER_PUBLIC, userId);
    }

    @OnEvent(value = "feed")
    public Feed getFeed() throws IOException, IllegalArgumentException, FeedException
    {
        return feedSource.produceFeed(ActivitySourceType.BOOK_CREATION);
    }

    @SetupRender
    public void setupIEMessage()
    {
        if (isIeFamily())
        {
            flashMessage = "Wooki will be a lot more comfortable to use with Chrome, Firefox or Safari !";
        }
    }

    public String getTitle()
    {
        if (this.user != null) { return messages.format("profile-title", this.user.getUsername()); }
        return messages.get("index-message");
    }

    public boolean isIeFamily()
    {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? (userAgent.toLowerCase().contains(" msie ") && this.user == null)
                : false;
    }

    public String getStyle()
    {
        return this.loopIdx == 0 ? "first" : null;
    }

}
