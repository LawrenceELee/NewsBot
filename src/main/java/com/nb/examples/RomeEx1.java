package com.nb.examples;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.feed.synd.SyndEntry;

import java.net.URL;
import java.io.InputStreamReader;
import java.util.List;


public class RomeEx1{

    public static void main(String[] args) {
        try {
            String urlString = "http://rss.cnn.com/rss/cnn_topstories.rss";
            URL feedUrl = new URL(urlString);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();

            System.out.println("Feed Title: " + feed.getTitle());

            for( SyndEntry entry: entries ){
                System.out.println("Entry Title: " + entry.getTitle());
                System.out.println("Unique ID: " + entry.getUri());
                System.out.println();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }
}
