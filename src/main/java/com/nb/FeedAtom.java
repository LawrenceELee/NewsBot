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

//extra stuff needed by atom feed
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContentImpl;


public class FeedAtom{
    private SyndFeed feed = null;
    private SyndFeedOutput outFeed = null;

    Date lastUpdated = new Date(0);
    Date mostRecent  = new Date(1);

    private List entries = new ArrayList();

    //only Atom feeds work. no RSS feeds. The spec for Atom doesn't
    //have Published Date. It uses Updated Date instead.
    String[] urls = {
        "http://hosted2.ap.org/atom/APDEFAULT/3d281c11a96b4ad082fe88aa0db04305",

        //all mag earthquakes from the past hour
        //"http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom"

        //4.5+ mag earthquakes from the past day
        "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_day.atom"
    };

    public FeedAtom(){
        feed = new SyndFeedImpl();
        feed.setFeedType("atom_0.3"); //not sure of v0.3 or v1.0
        /*
        valid values are:   
        rss_0.9, rss_0.91U, rss_0.91N, rss_0.92, rss_0.93,
        rss_0.94, rss_1.0, rss_2.0, atom_0.3, atom_1.0
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

                //entries.addAll(inFeed.getEntries());
                
                //create a whole new list and add the new items to it.
                for( SyndEntry entry: (List<SyndEntry>)inFeed.getEntries() ){
                    if(entry.getUpdatedDate().after(lastUpdated)){
                        entries.add(entry);

                        //find the most recent entry (like finding the max value)
                        if(entry.getUpdatedDate().after(mostRecent)){
                            mostRecent = entry.getUpdatedDate();
                        }
                    }
                }

            } catch( Exception ex ){
                ex.printStackTrace();
                System.out.println("ERROR (line 95): "+ex.getMessage());
            }
        }

        lastUpdated = mostRecent;
    } //end parseFeed()

    public boolean isUpdated(){
        return !entries.isEmpty();
    }

    public Date getLastUpdate(){
        return lastUpdated;
    }

    public List<SyndEntry> getEntries(){
        return entries;
    }

    /*
    public List<String> buildTweets(){
        List<Strings> newTweets = List<>();

        FeedAtom myfeed = new FeedAtom();
        DateFormat df = new SimpleDateFormat("M-d-yyyy HH:mm:ss z");
        List<SyndLinkImpl> links = null;

        while(true){
            myfeed.parseFeed();
            if(myfeed.isUpdated()){
                System.out.println("==========");
                System.out.println("Most recent entry: " + df.format(myfeed.lastUpdated));
                for( SyndEntry entry: myfeed.getEntries() ){
                    System.out.println("Title: " + entry.getTitle());
                    System.out.println("URI: " + entry.getUri());
                    System.out.println("Updated Date: " + df.format(entry.getUpdatedDate()));

                    //One Atom entry might have multiple URL links
                    links = entry.getLinks();
                    SyndLinkImpl firstLink = links.get(0);
                    System.out.println("URL: " + firstLink.getHref() );

                    System.out.println();
                    System.out.print("<a href=\"" + firstLink.getHref() + "\">");
                    System.out.print(entry.getTitle());
                    System.out.print("</a>");
                    System.out.println();

                    System.out.println();
                }
                return newTweets;
            }
        }
    }
    */

    //tester/client
    public static void main(String args[]){
        FeedAtom myfeed = new FeedAtom();
        DateFormat df = new SimpleDateFormat("M-d-yyyy HH:mm:ss z");
        List<SyndLinkImpl> links = null;

        while(true){
            myfeed.parseFeed();
            if(myfeed.isUpdated()){
                System.out.println("==========");
                //System.out.println("Most recent entry: " + df.format(myfeed.mostRecent));
                System.out.println("Most recent entry: " + df.format(myfeed.lastUpdated));
                for( SyndEntry entry: myfeed.getEntries() ){
                    System.out.println("Title: " + entry.getTitle());
                    System.out.println("URI: " + entry.getUri());
                    System.out.println("Updated Date: " + df.format(entry.getUpdatedDate()));

                    //One Atom entry might have multiple URL links
                    links = entry.getLinks();
                    SyndLinkImpl firstLink = links.get(0);
                    System.out.println("URL: " + firstLink.getHref() );
                    System.out.print("<a href=\"" + firstLink.getHref() + "\">");
                    System.out.print(entry.getTitle());
                    System.out.print("</a>");
                    System.out.println();

                    // Get the Links
                    /*
                    for( SyndLinkImpl link: (List<SyndLinkImpl>) entry.getLinks() ){
                        System.out.println("URL: " + link.getHref());
                    }

                    // Get the Contents
                    for (SyndContentImpl content : (List<SyndContentImpl>) entry.getContents()) {
                        System.out.println("Content: " + content.getValue());
                    }
                    // Get the Categories
                    for (SyndCategoryImpl category : (List<SyndCategoryImpl>) entry.getCategories()) {
                        System.out.println("Category: " + category.getName());
                    }
                    */

                    System.out.println();
                }
            }
            try{
                Thread.sleep(60000);    //wait 60 seconds
            } catch (InterruptedException ie){
                ie.printStackTrace();
                System.out.println("ERROR: " + ie.getMessage());
            }
        }
    }
}
