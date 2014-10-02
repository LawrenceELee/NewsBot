package com.nb;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

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

        try{
            p = NewsBot.getPropValues();
            consumerKeyStr = p.getProperty("consumer_key");
            consumerSecretStr = p.getProperty("consumer_api");
            accessTokenStr = p.getProperty("access_token_key");
            accessTokenSecretStr = p.getProperty("access_token_secret");
        } catch(FileNotFoundException fnfe){
            fnfe.getMessage();
        }

        try {
            Twitter twitter = new TwitterFactory().getInstance();

            twitter.setOAuthConsumer(consumerKeyStr, consumerSecretStr);
            AccessToken accessToken = new AccessToken(accessTokenStr,
                    accessTokenSecretStr);

            twitter.setOAuthAccessToken(accessToken);

            while(true){
                date = new Date();
                twitter.updateStatus("NewsBot " + dateFormat.format(date));
                System.out.println("NewsBot " + dateFormat.format(date));
                Thread.sleep(600000);
            }

        } catch (TwitterException te){
            te.printStackTrace();
        } catch (InterruptedException ie){
            ie.printStackTrace();
        }


    }
}
