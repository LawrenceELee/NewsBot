package com.nb;

//for getPropValues()
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

//for twitter/run
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/** *************************************************
 * Twitter Bot.
 * <p>
 * @author Lawrence L
 *
 * ************************************************** */
public class NewsBot{

    /** *************************************************
     * Reads the keys from the properties file.
     * @return Properties object containing Twitter keys
     *
     * ************************************************** */
    public static Properties getPropValues() throws FileNotFoundException{
        String propFilename = "twitter.properties";
        InputStream inStream = null;
        Properties prop = new Properties();

        inStream = NewsBot.class.getClassLoader().getResourceAsStream(propFilename);
        if(inStream == null){
            throw new FileNotFoundException("property file '"+ propFilename +"' not found.");
        }

        try{
            prop.load(inStream);
        } catch (IOException e){
            e.printStackTrace();
        }

        return prop;
    }

    /** *************************************************
     * Uses the keys and access the Twtitter API.
     * @param consumerKey api key1
     * @param comsumerSecret api key2
     * @param token token key1
     * @param secret token key2
     *
     * ************************************************** */
    public static void run(String consumerKey, String consumerSecret, String token, String secret) throws InterruptedException {
        // Create an appropriately sized blocking queue
        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(100);

        // Define our endpoint: By default, delimited=length is set (we need this for our processor)
        // and stall warnings are on.
        StatusesSampleEndpoint endpoint = new StatusesSampleEndpoint();
        endpoint.stallWarnings(false);

        Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);

        // Create a new BasicClient. By default gzip is enabled.
        BasicClient client = new ClientBuilder()
            .name("sampleExampleClient")
            .hosts(Constants.STREAM_HOST)
            .endpoint(endpoint)
            .authentication(auth)
            .processor(new StringDelimitedProcessor(queue))
            .build();

        // Establish a connection
        client.connect();

        // Do whatever needs to be done with messages
        for (int msgRead = 0; msgRead < 5; msgRead++) {
            if (client.isDone()) {
                System.out.println("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
                break;
            }

            String msg = queue.poll(5, TimeUnit.SECONDS);
            if (msg == null) {
                System.out.println("Did not receive a message in 5 seconds");
            } else {
                System.out.println(msg);
            }
        }
    }


    public static void main(String[] args){     
        Properties p = null;

        //get properties
        try{
            p = getPropValues();
        } catch(FileNotFoundException fnfe){
            fnfe.getMessage();
        }

        //connect to twitter
        try{
            NewsBot.run(    p.getProperty("consumer_key"),
                            p.getProperty("consumer_api"),
                            p.getProperty("access_token_key"),
                            p.getProperty("access_token_secret")   );
        } catch(InterruptedException ie){
            System.out.println(ie);
        }
    }
}
