package com.osten.halp.api.statistics;

import com.osten.halp.impl.io.CSVReader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
public class DataReaderTest {

    private File readableFile;
    private CSVReader reader;

    //GIVEN
    @Before
    public void prepare() throws IOException, URISyntaxException{
        readableFile = new File( getClass().getResource("/csv/data.csv").toURI() );
        reader = new CSVReader( readableFile );

        assert( readableFile != null );
    }


    @Test
    public void readHeader() throws IOException{

        //WHEN
        String[] firstLine = reader.readHeader();

        System.out.println( "First line:" );
        printStringArray( firstLine );

        //THEN
        assert( firstLine[0].compareTo("Timestamp") == 0 );
    }

    //GIVEN
    @Test
    public void readThirdLine() throws IOException{

        //WHEN
        String[] firstLine = reader.readLine(3);

        System.out.println( "Third line:" );
        printStringArray( firstLine );

        //THEN
        assert(firstLine != null);
    }

    //GIVEN
    @Test
    public void readEverything() throws IOException{

        ArrayList<String[]> list = new ArrayList<String[]>();

        list.addAll( reader.readFile() );

        System.out.println( "All the data: " );
        printCsvArray( list );

        assert( list.size() == 7 );

    }

    private void printCsvArray( ArrayList<String[]> list){

        for( String[] line : list ){
            System.out.print("{ ");
            for(String s : line){
                System.out.print( s + "\t ");
            }
        System.out.println("}\n");
        }
    }

    private void printStringArray(String[] line){
        System.out.print("{ ");
        for(String s : line){
            System.out.print( s + "\t ");
        }
        System.out.println("}\n");
    }
}
