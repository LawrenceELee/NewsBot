/** *************************************************
 * Represents an USGS/Atom feeds.
 *
 * ************************************************** */

package com.nb;

import java.net.URL;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class FeedAtom{
    private SyndFeed feed = null;
    private SyndFeedOutput outFeed = null;

    Date lastUpdated = new Date(0);
    Date mostRecent  = new Date(1);

    private List entries = new ArrayList();

    //only RSS feeds work. no Atom feeds.
    String[] urls = {
        "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom",
        "http://hosted2.ap.org/atom/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305"
    };

    public FeedAtom(){
        feed = new SyndFeedImpl();
        feed.setFeedType("atom_0.3");
        /*
        valid values are:   rss_0.9, rss_0.91U, rss_0.91N, rss_0.92, rss_0.93,
                            rss_0.94, rss_1.0, rss_2.0 & atom_0.3
        */

        feed.setTitle("Atom Aggregated Feed");
        feed.setDescription("Combine Atom feeds for Twitter Bot.");
        feed.setAuthor("None");
        feed.setLink("http://www.example.com");
    }

    /** *************************************************
     * This creates a news list with all the newest entries since
     * last run of parseFeed().
     *
     * ************************************************** */
    public void parseFeed(){
        entries = new ArrayList();
        feed.setEntries(entries);

        for( int i=0; i < urls.length; ++i ){
            URL inputURL = null;
            SyndFeedInput input = null;
            SyndFeed inFeed = null;

            try{
                inputURL = new URL(urls[i]);
                input = new SyndFeedInput();
                inFeed = input.build(new XmlReader(inputURL));
                
                //create a whole new list and add the new items to it.
                for( SyndEntry entry: (List<SyndEntry>)inFeed.getEntries() ){
                    if(entry.getPublishedDate().after(lastUpdated)){
                        entries.add(entry);

                        //find the most recent entry (like finding the max value)
                        if(entry.getPublishedDate().after(mostRecent)){
                            mostRecent = entry.getPublishedDate();
                        }
                    }
                }
                lastUpdated = mostRecent;

            } catch( Exception ex ){
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
    }

    public boolean isUpdated(){
        return !entries.isEmpty();
    }

    public Date getLastUpdate(){
        return lastUpdated;
    }

    public List<SyndEntry> getEntries(){
        return entries;
    }

    //tester/client
    public static void main(String args[]){
        Feed myfeed = new Feed();
        DateFormat df = new SimpleDateFormat("M-d HH:mm:ss z");

        while(true){
            myfeed.parseFeed();
            if(myfeed.isUpdated()){
                System.out.println("==========");
                System.out.println("Most recent entry: " + df.format(myfeed.mostRecent));
                System.out.println("Most recent entry: " + df.format(myfeed.lastUpdated));
                for( SyndEntry e: myfeed.getEntries() ){
                    System.out.println("Title: " + e.getTitle());
                    System.out.println("URL: " + e.getUri());
                    System.out.println("Pub Date: " + df.format(e.getPublishedDate()));
                    System.out.println();
                }
            }
            try{
                Thread.sleep(10000);    //wait 10 seconds
            } catch (InterruptedException ie){
                ie.printStackTrace();
                System.out.println("ERROR: "+ie.getMessage());
            }
        }
    }



}
