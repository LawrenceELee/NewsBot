/** *************************************************
 * Basic code to read a RSS feed and output to console
 * to testing if working.
 *
 * Dependencies: jdom-1.1.1.jar
 *               rome-1.0.jar
 *
 * ************************************************** */

package com.nb.examples;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class RomeEx3{
    public static void main(String[] args) throws Exception {       
        URL url = new URL("http://feeds.reuters.com/reuters/topNews");
        HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
        // Reading the feed
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(httpcon));
        List<SyndEntry> entries = feed.getEntries();
        Iterator<SyndEntry> itEntries = entries.iterator();

        while (itEntries.hasNext()) {
            SyndEntry entry = itEntries.next();
            System.out.println("Title: " + entry.getTitle());
            System.out.println("Link: " + entry.getLink());
            System.out.println("Author: " + entry.getAuthor());
            System.out.println("Publish Date: " + entry.getPublishedDate());
            System.out.println("Description: " + entry.getDescription().getValue());
            System.out.println();
        }
    }
}
