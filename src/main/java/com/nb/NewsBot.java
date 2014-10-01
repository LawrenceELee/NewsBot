package com.nb;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Twitter Bot.
 * <p>
 * @author Lawrence L
 *
 */
public class NewsBot{

    public static Properties getPropValues() throws FileNotFoundException{
        String propFilename = "config.properties";
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

    public static void main(String[] args){     
        Properties p = null;

        try{
            p = getPropValues();
        } catch(Exception e){
            e.getMessage();
        }

        System.out.println(p.getProperty("property1"));
        System.out.println(p.getProperty("property2"));
        System.out.println(p.getProperty("property3"));
    }
}

