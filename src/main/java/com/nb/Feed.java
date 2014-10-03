/** *************************************************
 * Represents an aggregated list of RSS/Atom feeds into a single feed.
 *
 * ************************************************** */

package com.nb;

import java.net.URL;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class Feed{
    SyndFeed feed = null;
    SyndFeedOutput outFeed = null;
    Date lastUpdated = new Date(0); //set date to minimum value
    Date mostRecent  = new Date(1);
    Date lastUpdatedFeed = new Date(0);

    List entries = new ArrayList();

    public Feed(){
        feed = new SyndFeedImpl();
        feed.setFeedType("rss_1.0");
        /*
        valid values are:   rss_0.9, rss_0.91U, rss_0.91N, rss_0.92, rss_0.93,
                            rss_0.94, rss_1.0, rss_2.0 & atom_0.3
        */

        feed.setTitle("Aggregated Feed");
        feed.setDescription("Combine feeds for Twitter Bot.");
        feed.setAuthor("None");
        feed.setLink("http://www.example.com");
    }

    /** *************************************************
     * This creates a news list with all the newest entries since
     * last run of build().
     *
     * ************************************************** */
    public void build(){
        String[] urls = {
            //"http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom",
            "http://hosted2.ap.org/atom/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305"
            "http://rss.cnn.com/rss/cnn_topstories.rss",
            "http://www.reuters.com/rssFeed/topNews"
        };

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
                
                //entries.addAll(inFeed.getEntries());

                for( SyndEntry entry: (List<SyndEntry>)inFeed.getEntries() ){
                    if(entry.getPublishedDate().after(lastUpdated)){
                        entries.add(entry);

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

    public void output(){
        try{
            outFeed = new SyndFeedOutput();
            outFeed.output(feed, new PrintWriter(System.out), true);
        } catch( Exception ex ){
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }

    public List<SyndEntry> getEntries(){
        return entries;
    }

    //tester/client
    public static void main(String args[]){
        Feed myfeed = new Feed();

        while(true){
            myfeed.build();
            if(myfeed.isUpdated()){
                System.out.println("Most recent entry: " + myfeed.mostRecent);
                for( SyndEntry e: myfeed.getEntries() ){
                    System.out.println("URL: " + e.getTitle());
                    System.out.println("URL: " + e.getUri());
                    System.out.println("Publish Date: " + e.getPublishedDate());
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

    } //end main()
} //end class Feed
