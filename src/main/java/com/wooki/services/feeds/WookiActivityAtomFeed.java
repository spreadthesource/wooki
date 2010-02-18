package com.wooki.services.feeds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.StreamResponse;
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
import com.wooki.domain.model.User;
import com.wooki.domain.model.activity.Activity;

public class WookiActivityAtomFeed extends Feed
{
    private static final long serialVersionUID = 8827267236916160709L;

    public WookiActivityAtomFeed(String title, String id, List<Link> alternateLinks,
            List<Activity> activities, final ActivityFeedWriter<Activity> activityFeed)
            throws IOException
    {
        super();

        this.setTitle(title);
        this.setId(id);
        this.setAlternateLinks(alternateLinks);

        if (activities.size() > 0)
        {
            this.setUpdated(activities.get(0).getLastModified());
        }

        List<Entry> entries = new ArrayList<Entry>();

        for (final Activity activity : activities)
        {
            Entry e = new Entry();

            // title, date
            e.setTitle(activityFeed.getTitle(activity));
            e.setPublished(activity.getLastModified());

            // adding author
            List<Person> author = new ArrayList<Person>();
            author.add(toPerson(activity.getUser()));
            e.setAuthors(author);

            // setting content
            Content c = new Content();
            c.setType(Content.TEXT);
            c.setValue(activityFeed.getSummary(activity));

            List<Content> content = new ArrayList<Content>();
            content.add(c);

            e.setContents(content);

            // add this entry
            entries.add(e);
        }

        this.setEntries(entries);

        // setting the atom type
        this.setFeedType("atom_1.0");
    }

    public StreamResponse toStreamResponse() throws IllegalArgumentException, FeedException
    {
        WireFeedOutput wireFeedOutput = new WireFeedOutput();
        Document feedDoc = wireFeedOutput.outputJDom(this);

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());

        return new AtomStreamResponse(outputter.outputString(feedDoc));
    }

    private Person toPerson(User user)
    {
        Person person = new Person();
        person.setName(user.getFullname());
        person.setUri("http://????/" + user.getUsername());
        person.setEmail(user.getEmail());
        return person;
    }
}
