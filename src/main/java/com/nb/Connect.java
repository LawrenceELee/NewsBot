/** *************************************************
 * Twitter connection code.
 *
 * ************************************************** */

package com.nb;

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

public class Connect{
    static Properties prop = new Properties();

    public static void getCredentials() throws FileNotFoundException{
        String propFilename = "twitter.properties";
        static InputStream inStream = null;

        //'this' is optional
        inStream = this.getClass().getClassLoader().getResourceAsStream(propFilename);
        if(inStream == null){
            throw new FileNotFoundException("property file '"+ propFilename +"' not found.");
        }

        try{
            prop.load(inStream);
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }

    /*
    public Twitter connectTwitterAPI(
        String consumerKey, String consumerSecret, String token, String tokenSecret){
    */
    public Twitter connectTwitterAPI(){
            Twitter twitter = null;
                
            Connect.getCredentials();

            String consumerKey = prop.getProperty("consumer_key");
            String consumerSecret = prop.getProperty("consumer_api");
            String token = prop.getProperty("access_token_key");
            String tokenSecret = prop.getProperty("access_token_secret");

            try {
                twitter = new TwitterFactory().getSingleton();

                twitter.setOAuthConsumer(consumerKey, consumerSecret);
                AccessToken accessToken =
                    new AccessToken(token, tokenSecret);

                twitter.setOAuthAccessToken(accessToken);
            } catch (Exception ex){
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }

            return twitter;
    }


    //test/driver
    public static void main(String args[]){
        Connect conn = new Connect();

        Twitter myTwitter = conn.connectTwitterAPI();

        try{
            Status status = myTwitter.updateStatus("Successfully connection with Connect.java");
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("ERROR: "+te.getMessage());
        }
    }
}
