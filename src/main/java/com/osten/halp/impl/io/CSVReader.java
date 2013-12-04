package com.osten.halp.impl.io;

import com.osten.halp.api.model.io.DataReader;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: server
 * Date: 2013-10-22
 * Time: 12:26
 * To change this template use File | Settings | File Templates.
 */
public class CSVReader implements DataReader {

    private File file;
    private BufferedReader reader;
    private StringBuilder sb;
    private String splitregexp;

    public CSVReader(File file) throws IOException {
        this(file, ",");
    }

    public CSVReader(File file, String splitregexp) throws IOException {
        this.file = file;
        this.splitregexp = splitregexp;
        reader = new BufferedReader(new FileReader(file));
        sb = new StringBuilder();
    }

    private void resetReader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
    }

    @Override
    public String[] readLine(int whatline) {

        int realLine = whatline - 1;
        String line = "";
        try {

            resetReader();

            for (int i = 0; line != null && i <= realLine; i++) {
                line = reader.readLine();
            }

            sb.append(line);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvSplit(sb.toString());
    }

    private String[] csvSplit(String csvString) {
        if(csvString.endsWith(",")){
            String[] splitString = csvString.split( splitregexp );
            String[] aLittleLongerString = new String[csvString.length()+1];
            for( int i = 0; i < splitString.length; i++ ){
                aLittleLongerString[i] = splitString[i];
            }
            aLittleLongerString[splitString.length] = "";
            return aLittleLongerString;
        }else{
            return csvString.split(splitregexp);
        }

    }

    public boolean isFileWellFormed(){
        for( String[] s : readFile() ){
            for( String value : s ){
                if( value.equals( "" )){
                    return false;
                }
            }
        }
        return true;
    }

    /*******************************************
     * Method only reads header, does not read content, use readFile for that.
     *
     * @return
     *******************************************/
    public String[] readHeader() {
        String[] line = null;
        try {
            line = readLine(1);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    @Override
    /**
     * This method does not read the header, use "readHeader" to do that.
     */
    public ArrayList<String[]> readFile() {

        ArrayList<String[]> csvList = new ArrayList<String[]>();
        String line = "";

        try {
            resetReader();

            while ( line != null ) {
                line = reader.readLine();

                if( line != null){
                    csvList.add( csvSplit(line) );
                }


            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvList;
    }

}
