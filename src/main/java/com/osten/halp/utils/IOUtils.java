package com.osten.halp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-17
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class IOUtils {

    public static String urlToString(URL url){
        File file = null;
        try{
            file = new File ( url.toURI() );
        }catch(URISyntaxException e){
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        try {
            FileReader reader = new FileReader( file );
            BufferedReader buffer = new BufferedReader(reader);
            String fileContent;
            while ((fileContent = buffer.readLine()) != null) {
               sb.append( fileContent + "\n" );
            }
        } catch(Exception e) {
            // do nothing
        }
        return sb.toString();
    }
}
