//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.pages.book;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.atom.Person;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import com.wooki.base.BookBase;
import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.biz.ChapterManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.Chapter;
import com.wooki.domain.model.Publication;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.pages.chapter.Edit;
import com.wooki.services.BookStreamResponse;
import com.wooki.services.HttpError;
import com.wooki.services.export.ExportService;
import com.wooki.services.feeds.ActivityFeed;
import com.wooki.services.feeds.AtomStreamResponse;
import com.wooki.services.security.WookiSecurityContext;

/**
 * This page displays a book with its table of contents.
 */
public class Index extends BookBase {

    @Inject
    private Messages messages;

    @Inject
    private BookManager bookManager;

    @Inject
    private ChapterManager chapterManager;

    @Inject
    private WookiSecurityContext securityCtx;

    @Inject
    private ExportService exportService;

    @Inject
    private ActivityManager activityManager;

    @Inject
    private ActivityFeed<Activity> feedWriter;

    @InjectPage
    private Edit editChapter;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private boolean printError;

    @Property
    private Long bookAbstractId;

    @Property
    private String bookAbstractTitle;

    @Property
    private User currentUser;

    @Property
    private int loopIdx;

    @Property
    private List<User> authors;

    @Property
    private List<Chapter> chaptersInfo;

    private Chapter currentChapter;

    /**
     * Will be set if the author tries to add a new chapter
     */
    @Property
    private String chapterName;

    private boolean showWorkingCopyLink;

    private boolean bookAuthor;

    /**
     * Setup all the data to display in the book index page.
     * 
     * @param bookId
     * @throws IOException
     */
    @OnEvent(value = EventConstants.ACTIVATE)
    public Object setupBookIndex(Long bookId, String revision) {

	this.setRevision(revision);
	this.setViewingRevision(true);

	// Only authors have access to the last revision
	if (ChapterManager.LAST.equalsIgnoreCase(revision) && !(this.securityCtx.isLoggedIn() && this.securityCtx.isAuthorOfBook(this.getBookId()))) {
	    return new HttpError(403, "Access denied");
	}

	return true;
    }

    /**
     * Prepare book display.
     */
    @SetupRender
    public void setupBookDisplay() {

	this.authors = this.getBook().getAuthors();
	this.bookAuthor = this.securityCtx.isAuthorOfBook(this.getBookId());

	// List chapter infos
	List<Chapter> chapters = chapterManager.listChaptersInfo(this.getBookId());
	this.bookAbstractId = chapters.get(0).getId();
	this.bookAbstractTitle = chapters.get(0).getTitle();

	if (chapters.size() > 0) {
	    this.chaptersInfo = chapters.subList(1, chapters.size());
	}

	// Get abstract publication
	Publication abstractPublication = this.isViewingRevision() ? this.chapterManager.getRevision(this.bookAbstractId, this.getRevision()) : this.chapterManager
		.getLastPublishedPublication(this.bookAbstractId);
	this.setPublication(abstractPublication);

	// Setup abstract content
	this.setupContent();

    }

    @OnEvent(value = EventConstants.SUCCESS, component = "addChapterForm")
    public Object addNewChapter() {
	Chapter chapter = bookManager.addChapter(this.getBook(), chapterName);
	editChapter.setBookId(this.getBookId());
	editChapter.setChapterId(chapter.getId());
	return editChapter;
    }

    /**
     * Simply export to PDF.
     * 
     * @return
     */
    @OnEvent(value = "print")
    public Object exportPdf() {
	try {
	    InputStream bookStream = this.exportService.exportPdf(this.getBookId());
	    return new BookStreamResponse(this.getBook().getSlugTitle(), bookStream);
	} catch (Exception ex) {
	    this.printError = true;
	    return this;
	}
    }

    /**
     * Create the Atom feed of the book activity
     * 
     * @throws IOException
     * @throws FeedException
     * @throws IllegalArgumentException
     */
    @OnEvent(value = "feed")
    public StreamResponse getFeed() throws IOException, IllegalArgumentException, FeedException {
	// all the feed construction is made by a third party library called
	// "ROME". It provides RSS & Atom support
	final Book book = bookManager.findById(getBookId());

	Feed f = new Feed();
	f.setTitle("Activity for " + book.getTitle());
	f.setId(book.getSlugTitle());
	f.setAlternateLinks(new ArrayList<Link>() {
	    {
		add(new Link() {
		    {
			setHref("http://????/" + book.getId().toString());
			setTitle(book.getTitle());
		    }
		});
	    }
	});

	List<Person> authors = new ArrayList<Person>();
	for (User user : book.getAuthors()) {
	    authors.add(toPerson(user));
	}

	f.setAuthors(authors);
	f.setUpdated(book.getLastModified());

	List<Entry> entries = new ArrayList<Entry>();
	for (final Activity activity : activityManager.listAllBookActivities(getBookId())) {
	    Entry e = new Entry();
	    e.setAuthors(new ArrayList<Person>() {
		{
		    add(toPerson(activity.getUser()));
		}
	    });

	    e.setTitle(feedWriter.getTitle(activity));
	    e.setPublished(activity.getLastModified());

	    e.setContents(new ArrayList<Content>() {
		{
		    Content c = new Content();
		    c.setType(Content.TEXT);
		    c.setValue(feedWriter.getSummary(activity));
		    add(c);
		}
	    });

	    entries.add(e);
	}

	f.setEntries(entries);

	// yes, it forces the feed type to atom
	f.setFeedType("atom_1.0");
	WireFeedOutput wireFeedOutput = new WireFeedOutput();
	Document feedDoc = wireFeedOutput.outputJDom(f);

	XMLOutputter outputter = new XMLOutputter();
	outputter.setFormat(Format.getPrettyFormat());

	// my question is, a Tapestry dispatcher responsible for producing feeds
	// should be able to do the same na ?

	return new AtomStreamResponse(outputter.outputString(feedDoc));
    }

    private Person toPerson(User user) {
	Person person = new Person();
	person.setName(user.getFullname());
	person.setUri("http://????/" + user.getUsername());
	person.setEmail(user.getEmail());
	return person;
    }

    public String[] getPrintErrors() {
	return new String[] { this.messages.get("print-error") };
    }

    @OnEvent(value = EventConstants.PASSIVATE)
    public Long retrieveBookId() {
	return this.getBookId();
    }

    public boolean isPublished() {
	long chapterId = currentChapter.getId();

	Publication publication = this.chapterManager.getLastPublishedPublication(chapterId);

	return (publication != null);
    }

    public boolean isAbstractHasWorkingCopy() {
	return hasWorkingCopy(this.bookAbstractId);
    }

    public boolean isShowWorkingCopyLink() {
	long chapterId = currentChapter.getId();
	return hasWorkingCopy(chapterId);
    }

    private final boolean hasWorkingCopy(long chapterId) {
	Publication publication = this.chapterManager.getRevision(chapterId, ChapterManager.LAST);
	if (publication != null) {
	    boolean workingCopy = !publication.isPublished();
	    return bookAuthor && workingCopy;
	}
	return false;
    }

    /**
     * Get edit context for chapter
     * 
     * @return
     */
    public Object[] getEditCtx() {
	return new Object[] { this.getBookId(), this.bookAbstractId };
    }

    public Object[] getAbstractWorkingCopyCtx() {
	return new Object[] { this.getBookId(), ChapterManager.LAST };
    }

    public Object[] getIssuesCtx() {
	return new Object[] { this.getBookId(), "all" };
    }

    /**
     * Get id to link to chapter display
     * 
     * @return
     */
    public Object[] getChapterCtx() {
	return new Object[] { this.getBookId(), this.currentChapter.getId() };
    }

    public Object[] getChapterWorkingCopyCtx() {
	return new Object[] { this.getBookId(), this.currentChapter.getId(), ChapterManager.LAST };
    }

    public Chapter getCurrentChapter() {
	return currentChapter;
    }

    public void setCurrentChapter(Chapter currentChapter) {
	this.currentChapter = currentChapter;
    }

}
