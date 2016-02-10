package ir.assignments.two;

import ir.assignments.one.a.Frequency;
import ir.assignments.one.a.Utilities;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Comparator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;

public class Statistics
{
    // The Subdomains text file
    private static final String SUBDOMAINS_TXT = "Subdomains.txt";
    // The CommonWords text file
    private static final String COMMONWORDS_TXT = "CommonWords.txt";
    // The Answers text file
    private static final String ANSWERS_TXT = "Answers.txt";
    // The StopWords text file
    private static final String STOPWORDS_TXT = "StopWords.txt";
    // The location of the data logs
    private static final String LOGS = "data/logs";
    // The top 500 most common words constant
    private static final int NUM_COMMONWORDS = 500;
    // The list of subdomains found so far
    private ArrayList<String> subdomains;
    // The list of DataThreads
    private ArrayList<DataThread> dataThreads;
    // The hashmap containing <String:Frequency> pairs
    private HashMap<String,Frequency> frequencies;
    // The set of stop words
    private HashSet<String> stopWords;
    // The longest page's URL
    private String longestPageUrl;
    // The number of words found in the longest page
    private int maxWords;
    // The number of unique pages
    private int numPages;

    public Statistics()
    {
        subdomains = new ArrayList<String>();
        dataThreads = new ArrayList<DataThread>();
        frequencies = new HashMap<String,Frequency>();
        stopWords = new HashSet<String>();
        longestPageUrl = new String("");
        maxWords = 0;
        numPages = 0;
        getStopWords();
    }

    /**
     * Loads the stop words into the stopWords set from StopWords.txt.
     *
     * @exception IOException Unable to open the file for reading.
     */
    public void getStopWords()
    {
        String line;
        try ( BufferedReader br = new BufferedReader(new FileReader(STOPWORDS_TXT)) )
        {
            while ( (line = br.readLine()) != null )
            {
                if ( !line.isEmpty() )
                {
                    stopWords.add(line);
                }
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Iterates through a list of files or directories.
     * Subsequently calls the function getData() to strip the URL 
     * and text information from the file for further processing.
     * If the file is a directory, openFiles() recursively enters 
     * the new directory.
     *
     * This function assumes that the file structure is no more
     * than 1 level of depth. All folders found within the first
     * level passed 'logs' will be considered folders of a subdomain
     * and its text files.
     *
     * @param files The list of files/directories to be opened.
     */
    private void openFiles(File[] files)
    {
        for ( File file : files )
        {
            // If we have found a subdomain folder
            if ( file.isDirectory() )
            {
                if ( !file.getName().equals("logs") )
                {
                    subdomains.add(file.getName());
                    dataThreads.add(new DataThread(file));
                    dataThreads.get(dataThreads.size() - 1).start();
                }
                else
                {
                    openFiles(file.listFiles());
                }
            }
        }
    }

    /**
     * Pulls all the data calculated by the threads and saves them to
     * their appropriate files.
     * The data is processed to compute the longest page, subdomains,
     * and 500 most common words.
     *
     * Files created:
     *
     *      Subdomains.txt      - Contains all the subdomains found.
     *      CommonWords.txt     - Contains the top 500 common words.
     *      Answers.txt         - Contains number of unique pages and 
     *                            longest page info.
     * 
     */
    private void calculateAndStoreData()
    {
        combineThreadData();
        writeSubdomains();
        writeCommonWords();
        writeAnswers();
    }

    /**
     * Combines all the data processed by the threads.
     *
     * @exception InterruptedException The DataThread was unable to finish.
     */
    private void combineThreadData()
    {
        for ( DataThread dt : dataThreads )
        {
            try
            {
                dt.join(); 
                addFrequencies(dt);
                addLongestPage(dt);
                numPages += dt.getNumPages();
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add frequencies from the thread to the master hash map.
     *
     * @param dt The DataThread object.
     */
    private void addFrequencies(DataThread dt)
    {
        Map<String,Frequency> map = dt.getFrequencies();
        for ( Map.Entry<String,Frequency> entry : map.entrySet() )
        {
            int frequency = entry.getValue().getFrequency();
            if ( frequencies.containsKey(entry.getKey()) )
            {
                frequency += frequencies.get(entry.getKey()).getFrequency(); 
            }
            frequencies.put(entry.getKey(), new Frequency(entry.getKey(), frequency));
        }
    }

    /**
     * Checks if the DataThread's longest page is the longest page.
     *
     * @param dt The DataThread object.
     */
    private void addLongestPage(DataThread dt)
    {
        if ( dt.getMaxWords() > maxWords )
        {
            maxWords = dt.getMaxWords();
            longestPageUrl = dt.getLongestPageUrl();
        }
    }

    /**
     * Write the subdomains with the number of pages to Subdomains.txt.
     *
     * @exception IOException Unable to open the file for writing.
     */
    private void writeSubdomains()
    {
        Collections.sort(dataThreads, new DataThreadCompare());
        try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(SUBDOMAINS_TXT))) )
        {
            for ( DataThread dt : dataThreads )
            {
                pw.println(dt.getSubdomain() + ", " + dt.getNumPages());
                pw.flush();
            }
            pw.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Write out the top 500 common words and their frequency to CommonWords.txt.
     *
     * @exception IOException Unable to open the file for writing.
     */
    private void writeCommonWords()
    {
        ArrayList<Frequency> sortedFrequencies = new ArrayList<Frequency>();
        for ( Map.Entry<String,Frequency> entry : frequencies.entrySet() )
        {
            sortedFrequencies.add(entry.getValue());
        }
        Collections.sort(sortedFrequencies, new Utilities.FrequencyCompare());
        int count = 0;
        try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(COMMONWORDS_TXT))) )
        {
            for ( int i = 0; i < sortedFrequencies.size() && count < NUM_COMMONWORDS; i++ )
            {
                if ( isValidWord(sortedFrequencies.get(i).getText()) )
                {
                    pw.println(sortedFrequencies.get(i));
                    pw.flush();
                    count++;
                }
            }
            pw.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Write out the answer to the questions to Answers.txt.
     *
     * @exception IOException Unable to open the file for writing.
     */
    private void writeAnswers()
    {
        try ( PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(ANSWERS_TXT))) )
        {
            pw.println("Time: ");
            pw.println("Unique pages: " + numPages);
            pw.println("Longest Page URL: " + longestPageUrl);
            pw.println("Longest Page Word Count: " + maxWords);
            pw.flush();
            pw.close();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the words is valid.
     * In other words, no numbers or whitespace.
     *
     * @param word The word to verify.
     * @return True if the word is valid.
     */
    private boolean isValidWord(String word)
    {
        if ( !stopWords.contains(word) )
        {
            for ( char c : word.toCharArray() )
            {
                if ( !Character.isLetter(c) )
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Comparison class to compare two DataThread objects by subdomain name.
     * Tiebreaker is number of pages.
     */
    public static class DataThreadCompare implements Comparator<DataThread>
    {
        public int compare(DataThread d1, DataThread d2)
        {
            if ( d1.getName().equals(d2.getName()) )
            {
                return d2.getNumPages() - d1.getNumPages();
            }
            else
            {
                return d1.getSubdomain().compareTo(d2.getSubdomain());
            }
        }
    }

    /* Starts processing */
    public static void main(String[] args)
    {
        File[] files = new File[1];
        files[0] = new File(LOGS);
        Statistics stats = new Statistics();
        stats.openFiles(files);
        stats.calculateAndStoreData();
    }
}
