package ir.assignments.two;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;
import java.io.File;

public class CrawlStat
{
	public static void addPage(String subdomain, String docId, String url, String text)
	{
		String domainPath = "data/logs/" + subdomain;
		String filePath = domainPath + "/" + docId + ".txt";
		File dir = new File(domainPath);
		try 
		{
			if ( !dir.exists() )
			{
				dir.mkdir();
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

	public static void printError(String message)
    {
        System.out.println("\n\n=============================\n\n");
        System.out.println(message);
        System.out.println("\n\n=============================\n\n");
    }
}