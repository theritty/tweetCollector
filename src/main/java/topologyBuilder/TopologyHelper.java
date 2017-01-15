package topologyBuilder;

import backtype.storm.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by ceren on 10.05.2016.
 */
public class TopologyHelper {

    public static void writeToFile(String fileName, String tweet)
    {
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(
                    new File(fileName),
                    true /* append = true */));

                writer.println(tweet);
//        System.out.println(tweet);

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> splitInteger( String sentence )
    {
        List<Integer> list = new ArrayList<>();
        String[] parts = sentence.split(",");

        for ( final String part : parts )
        {
            list.add( Integer.parseInt( part ) );
        }
        return list;
    }

    public static List<String> splitString( String sentence )
    {
        List<String> list = new ArrayList<>();
        String[] parts = sentence.split(",");

        for ( final String part : parts )
        {
            list.add( part );
        }
        return list;
    }

    protected static synchronized Config copyPropertiesToStormConfig( Properties properties )
    {
        Config config = new Config();
        for (String name : properties.stringPropertyNames()) {
            config.put(name, properties.getProperty(name));
        }
        return config;
    }

    protected static synchronized Properties loadProperties( String propertiesFile ) throws IOException
    {
        try {
            Properties properties = new Properties();
            InputStream inputStream = TopologyHelper.class.getClassLoader().getResourceAsStream( propertiesFile );
            properties.load( inputStream );
            inputStream.close();
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void createFolder(String fileName)
    {
        File theDir = new File(fileName);

        if (!theDir.exists()) {
            try{
                theDir.mkdir();
            }
            catch(SecurityException se){
            }
        }
    }
}
