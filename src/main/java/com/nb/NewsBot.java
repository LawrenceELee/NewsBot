package com.nb;

import java.lang.Exception;

//for getCredentials()
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

//for twitter4j
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.Status;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//for ROME library
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.feed.synd.SyndEntry;
import java.net.URL;
import java.io.InputStreamReader;
import java.util.List;

/** *************************************************
 * Twitter Bot.
 * @author Lawrence L
 *
 * ************************************************** */
public class NewsBot{

    /** *************************************************
     * Reads the keys/credentials from the properties file.
     * @return Properties object containing Twitter keys
     *
     * ************************************************** */
    public static Properties getCredentials() throws FileNotFoundException{
        String propFilename = "twitter.properties";
        InputStream inStream = null;
        Properties prop = new Properties();

        inStream = NewsBot.class.getClassLoader().getResourceAsStream(propFilename);
        if(inStream == null){
            throw new FileNotFoundException("property file '"+ propFilename +"' not found.");
        }

        try{
            prop.load(inStream);
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }

        return prop;
    }

    /** *************************************************
     * Uses the keys and access the Twtitter API vis Twitter4J.
     * @param consumerKey api key1
     * @param comsumerSecret api key2
     * @param token token key1
     * @param tokenSecret token key2
     *
     * ************************************************** */
    public static Twitter connectTwitterAPI(
        String consumerKey, String consumerSecret,
        String token, String tokenSecret){

            try {
                Twitter twitter = new TwitterFactory().getSingleton();

                twitter.setOAuthConsumer(consumerKey, consumerSecret);
                AccessToken accessToken =
                    new AccessToken(token, tokenSecret);

                twitter.setOAuthAccessToken(accessToken);

                return twitter;
            } catch (Exception ex){
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }

            return null;
    }


    public static SyndEntry readFeed(String urlString){
        try {
            URL feedUrl = new URL(urlString);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> entries = feed.getEntries();
            SyndEntry entry = entries.get(0);

            return entry;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }

        return null;
    }

    public static void main(String[] args){     
        Properties p = null;

        //get keys to access the api
        try{
            p = getCredentials();
        } catch(FileNotFoundException fnfe){
            fnfe.getMessage();
        }

        //connect to twitter using the credentials parsed earlier
        Twitter twit = connectTwitterAPI(
                p.getProperty("consumer_key"), p.getProperty("consumer_api"),
                p.getProperty("access_token_key"), p.getProperty("access_token_secret"));

        String url = "http://rss.cnn.com/rss/cnn_topstories.rss";
        //String url = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.atom";

        //read rss feed
        SyndEntry se = readFeed(url);

        //try to post a status update
        Status status = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;

        while(true){
            date = new Date();
            try{
                status = twit.updateStatus(
                        "TS: " + dateFormat.format(date) +
                        " Tit: " + se.getTitle() +
                        " ID: " + se.getUri() +
                        " Pub: " + se.getPublishedDate() );
                System.out.println("Successfully updated the status to [" + status.getText() + "].");
                Thread.sleep(60000);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("ERROR: "+te.getMessage());
            } catch (InterruptedException ie) {
                //call to Thread.sleep
                ie.printStackTrace();
                System.out.println("ERROR: "+ie.getMessage());
            }
        }
    } //end main
} //end NewsBot
