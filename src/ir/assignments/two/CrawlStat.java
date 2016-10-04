/*
 * Assignment 2, CS 121
 * Written by:  Jacob Armentrout     66598462
 *              Gillian Bendicio,    56433482
 *              Jennifer Chew,       53649288
 *              Vinh Vu              21775557
 *
 * Last Modified Date: 02/09/16
 */

package ir.assignments.two;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;
import java.io.File;

public class CrawlStat
{
	/**
	 * Creates and saves the page as a file with its URL and text data.
	 *
	 * @param subdomain The subdomain of the URL.
	 * @param docId The DOC-ID of the page.
	 * @param url The URL.
	 * @param text The text data stripped from the HTML.
	 * @exception IOException An error opening the File/Buffer Writer.
	 */
	public static void addPage(String subdomain, String docId, String url, String text)
	{
		String domainPath = "../logs/" + subdomain;
		String filePath = domainPath + "/" + docId + ".txt";
		File dir = new File(domainPath);
		try 
		{
			if ( !dir.exists() )
			{
				dir.mkdirs();
			}
			BufferedWriter page = new BufferedWriter(new FileWriter(filePath, false));
			page.write(url + "\n" + text);
			page.flush();
			page.close();
		}
		catch (IOException e)
		{
			printError(e.getMessage());
		}
		catch (Exception e)
		{
			printError(e.getMessage());
		}
	}

	/**
	 * Prints the formatted error.
	 */
	public static void printError(String message)
    {
        System.out.println("\n\n=============================\n\n");
        System.out.println(message);
        System.out.println("\n\n=============================\n\n");
    }
}
