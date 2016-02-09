package ir.assignments.two;

import ir.assignments.one.a.Frequency;
import ir.assignments.one.a.Utilities;
import ir.assignments.one.b.WordFrequencyCounter;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class DataThread extends Thread
{
    // The folder of the subdomain
    private File subdomain;
    // The map of word frequencies
    private HashMap<String,Frequency> frequencies;
    // The URL of the longest page found so far
    private String longestPage;
    // The max of words found in a page so far
    private int maxWords;
    // The number of pages found
    private int numPages;

    public DataThread(File file)
    {
        subdomain = file;
        frequencies = new HashMap<String,Frequency>();
        longestPage = new String("");
        maxWords = 0;
        numPages = 0;
    }

    /**
     * Returns the frequencies map.
     */
    public Map<String,Frequency> getFrequencies()
    {
        return frequencies;
    }

    /**
     * Returns the longest page's URL.
     */
    public String getLongestPageUrl()
    {
        return longestPage;
    }

    /**
     * Returns the max number of words found from the
     * longest page.
     */
    public int getMaxWords()
    {
        return maxWords;
    }

    /**
     * Returns the number of unique pages found.
     */
    public int getNumPages()
    {
        return numPages;
    }

    /**
     * Begins processing all the files within the subdomain's folder.
     */
    @Override
    public void run()
    {
        if ( subdomain.isDirectory() )
        {
            for ( File file : subdomain.listFiles() )
            {
                if ( file.isFile() )
                {
                    StringBuilder url = new StringBuilder();
                    List<String> words = Utilities.tokenizeFile(file, url);
                    List<Frequency> newFrequencies = WordFrequencyCounter.computeWordFrequencies(words);
                    addFrequencies(newFrequencies, frequencies);
                    isLongestPage(newFrequencies, url.toString());
                    numPages++;
                }
            }
        }
    }

    /**
     * Adds the new list of frequencies to the frequency map.
     * If the the frequency is already in the map, the frequency
     * will be replaced with an updated frequency containing the sum
     * of the frequencies.
     *
     * @param newFrequencies The new list of frequencies to be added.
     * @param frequencies The original map of frequencies to be updated.
     */
    private void addFrequencies(List<Frequency> newFrequencies, Map<String,Frequency> frequencies)
    {
        for ( Frequency f : newFrequencies )
        {
            int frequency = f.getFrequency();
            if ( frequencies.containsKey(f.getText()) )
            {
                frequency += frequencies.get(f.getText()).getFrequency();
            }
            frequencies.put(f.getText(), new Frequency(f.getText(), frequency));
        }
    }

    /**
     * Checks if the given list of frequencies contains the most
     * amount of words.
     * If so, the longestPage and maxWords variables are updated.
     *
     * @param newFrequencies The list of frequencies.
     * @param url The URL of the page.
     */
    private void isLongestPage(List<Frequency> newFrequencies, String url)
    {
        int numWords = 0;
        for ( Frequency f : newFrequencies )
        {
            numWords += f.getFrequency();
        }
        if ( numWords > maxWords )
        {
            maxWords = numWords;
            longestPage = url;
        }
    }
}
