package com.nb.examples;

import com.nb.NewsBot;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.Status;

import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Twitter4JEx{

    static String consumerKeyStr = null;
    static String consumerSecretStr = null;
    static String accessTokenStr = null;
    static String accessTokenSecretStr = null;

    public static void main(String[] args) {
        Properties p = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = null;

        //get credentials
        try{
            p = NewsBot.getCredentials();
            consumerKeyStr = p.getProperty("consumer_key");
            consumerSecretStr = p.getProperty("consumer_api");
            accessTokenStr = p.getProperty("access_token_key");
            accessTokenSecretStr = p.getProperty("access_token_secret");
        } catch(FileNotFoundException fnfe){
            fnfe.getMessage();
        }

        //post a status update
        try {
            //Twitter twitter = new TwitterFactory().getInstance();
            Twitter twitter = new TwitterFactory().getSingleton();
            Status status = null;

            twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
            AccessToken accessToken = new AccessToken(accessTokenStr,
                    accessTokenSecretStr);

            twitter.setOAuthAccessToken(accessToken);

            while(true){
                date = new Date();
                status = twitter.updateStatus("NewsBot " + dateFormat.format(date));
                System.out.println("Successfully updated the status to [" +
                        status.getText() + "].");
                Thread.sleep(600000); //wait 10 mins between each status update
            }

        } catch (TwitterException te){
            te.printStackTrace();
        } catch (InterruptedException ie){
            ie.printStackTrace();
        }


    }
}
