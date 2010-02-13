package com.wooki.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.atom.Link;
import com.sun.syndication.feed.atom.Person;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import com.wooki.domain.biz.ActivityManager;
import com.wooki.domain.biz.BookManager;
import com.wooki.domain.model.Book;
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;
import com.wooki.domain.model.activity.BookActivity;
import com.wooki.domain.model.activity.ChapterActivity;
import com.wooki.domain.model.activity.CommentActivity;

@Path("/book/{id}/feed")
@Component
public class BookFeedResource {

	@Autowired
	private ActivityManager activityManager;

	@Autowired
	private BookManager bookManager;

	@GET
	@Produces("application/atom+xml")
	public Feed getRSS(@PathParam("id") String id) throws IOException, IllegalArgumentException, FeedException {
		// all the feed construction is made by a third party library called
		// "ROME". It provides RSS & Atom support
		// (note that producing JSON is done by simply creating a JSONObject)
		Long bookId = Long.parseLong(id);
		final Book book = bookManager.findById(bookId);

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
		for (final Activity activity : activityManager.listAllBookActivities(bookId)) {
			Entry e = new Entry();
			e.setAuthors(new ArrayList<Person>() {
				{
					add(toPerson(activity.getUser()));
				}
			});

			e.setTitle("new event #" + activity.getId().toString() + " by " + activity.getUser().getUsername());
			e.setPublished(activity.getLastModified());

			e.setContents(new ArrayList<Content>() {
				{
					Content c = new Content();
					c.setType(Content.TEXT);
					c.setValue(activityMessage(activity));
					add(c);
				}
			});

			entries.add(e);
		}

		f.setEntries(entries);

		/**
		 * Here it is what jersey atom is only doing. is it really
		 */
		// yes, it forces the feed type to atom
		f.setFeedType("atom_1.0");
		WireFeedOutput wireFeedOutput = new WireFeedOutput();
		Document feedDoc = wireFeedOutput.outputJDom(f);

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());

		// that's it, you just need to ouput then...
		System.out.println(outputter.outputString(feedDoc));

		// my question is, a Tapestry dispatcher responsible for producing feeds
		// should be able to do the same na ?

		return f;
	}

	private Person toPerson(User user) {
		Person person = new Person();
		person.setName(user.getFullname());
		person.setUri("http://????/" + user.getUsername());
		person.setEmail(user.getEmail());
		return person;
	}

	private String activityMessage(Activity activity) {
		if (activity instanceof ChapterActivity) {
			ChapterActivity a = (ChapterActivity) activity;
			return a.getChapter().getTitle() + " " + a.getType();
		} else if (activity instanceof BookActivity) {
			BookActivity a = (BookActivity) activity;
			return a.getBook().getTitle() + " has just been created";
		} else if (activity instanceof CommentActivity) {
			CommentActivity a = (CommentActivity) activity;
			return a.getUser().getUsername() + " has posted a comment \n" + a.getComment().getContent();
		} else
			return "dummy";
	}
}