package ir.assignments.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller 
{
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private static final String DATA_FOLDER = "data";
    private static final int NUM_CRAWLERS = 7;
    private static final int POLITENESS_DELAY = 1200;
    private static final boolean RESUMABLE_CRAWLING = true;
    private static final int DOWNLOAD_SIZE = 100000000;
    private static final String USER_AGENT_STRING = "UCI Inf141-CS121 crawler 56433482 66598462 21775557 53649288";

    public static void main(String[] args) throws Exception 
    {
        CrawlConfig config = new CrawlConfig();
        config.setPolitenessDelay(POLITENESS_DELAY);
        config.setCrawlStorageFolder(DATA_FOLDER);
        // Fixes crawling from stopping short.
        config.setMaxDownloadSize(DOWNLOAD_SIZE);
        config.setResumableCrawling(RESUMABLE_CRAWLING);
        config.setUserAgentString(USER_AGENT_STRING);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /**
         * Configures crawler to ignore binary content.
         */
        config.setIncludeBinaryContentInCrawling(false);

        /*
         * Our starting link.
         */
        controller.addSeed("http://www.ics.uci.edu/");

        /*
         * Start the crawl.
         */
        controller.start(Crawler.class, NUM_CRAWLERS);
    }
}
