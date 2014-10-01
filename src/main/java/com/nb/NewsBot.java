package com.nb;

import java.net.URL;
import java.io.InputStreamReader;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.util.List;
import java.util.ArrayList;

/**
 * It Reads and prints any RSS/Atom feed type.
 * <p>
 * @author Lawrence L
 *
 */
public class NewsBot{

    public static void main(String[] args) {        
        try {
            String urlString = "http://feeds.reuters.com/reuters/topNews";
            URL feedUrl = new URL(urlString);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            System.out.println(feed.getUri());

            List entries = new ArrayList();
            entries = feed.getEntries();

            System.out.println(entries.get(0));
            /*
               for(Object entry: entries){
               System.out.println(entry);
               }
            */
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }
}

