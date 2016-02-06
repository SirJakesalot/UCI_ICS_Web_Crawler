package ir.assignments.two;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(MyCrawler.class);
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "data";
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        config.setPolitenessDelay(3000);
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDownloadSize(100000000);
        config.setResumableCrawling(true);
        config.setMaxPagesToFetch(-1);
        config.setUserAgentString("UCI Inf141-CS121 crawler 56433482 66598462 21775557 53649288");

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /**
         * Do you want crawler4j to crawl also binary data ?
         * example: the contents of pdf, or the metadata of images etc
         */
        config.setIncludeBinaryContentInCrawling(false);

        /*
         * To avoid really deep links from filling up the frontier to quickly
         */
        config.setMaxDepthOfCrawling(10);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        // controller.addSeed("http://asterix.ics.uci.edu/");
        // controller.addSeed("http://asterixdb.ics.uci.edu/");
        // controller.addSeed("http://auge.ics.uci.edu/");
        // controller.addSeed("http://ccsw.ics.uci.edu/");
        // controller.addSeed("http://cert.ics.uci.edu/");
        // controller.addSeed("http://cgvw.ics.uci.edu/");
        // controller.addSeed("http://chime.ics.uci.edu/");
        // controller.addSeed("http://cml.ics.uci.edu/");
        // controller.addSeed("http://codeexchange.ics.uci.edu/");
        // controller.addSeed("http://computableplant.ics.uci.edu/");
        // controller.addSeed("http://cradl.ics.uci.edu/");
        // controller.addSeed("http://dblp.ics.uci.edu/");
        // controller.addSeed("http://emj.ics.uci.edu/");
        // controller.addSeed("http://emme.ics.uci.edu/");
        // controller.addSeed("http://esl.ics.uci.edu/");
        // controller.addSeed("http://evoke.ics.uci.edu/");
        // controller.addSeed("http://flamigo.ics.uci.edu/");
        // controller.addSeed("http://frost.ics.uci.edu/");
        // controller.addSeed("http://graphics.ics.uci.edu/");
        // controller.addSeed("http://graphmod.ics.uci.edu/");
        // controller.addSeed("http://hcc.ics.uci.edu/");
        // controller.addSeed("http://hobbes.ics.uci.edu/");
        // controller.addSeed("http://hombao.ics.uci.edu/");
        // controller.addSeed("http://honors.ics.uci.edu/");
        // controller.addSeed("http://i-sensorium.ics.uci.edu/");
        // controller.addSeed("http://ipubmed.ics.uci.edu/");
        // controller.addSeed("http://ironwood.ics.uci.edu/");
        // controller.addSeed("http://isg.ics.uci.edu/");
        // controller.addSeed("http://jujube.ics.uci.edu/");
        // controller.addSeed("http://luci.ics.uci.edu/");
        // controller.addSeed("http://malek.ics.uci.edu/");
        // controller.addSeed("http://mondego.ics.uci.edu/");
        // controller.addSeed("http://ngs.ics.uci.edu/");
        // controller.addSeed("http://pregelix.ics.uci.edu/");
        // controller.addSeed("http://psearch.ics.uci.edu/");
        // controller.addSeed("http://radicle.ics.uci.edu/");
        // controller.addSeed("http://rscit.ics.uci.edu/");
        // controller.addSeed("http://sconce.ics.uci.edu/");
        // controller.addSeed("http://sdcl.ics.uci.edu/");
        // controller.addSeed("http://seal.ics.uci.edu/");
        // controller.addSeed("http://sherlock.ics.uci.edu/");
        // controller.addSeed("http://sli.ics.uci.edu/");
        // controller.addSeed("http://sourcerer.ics.uci.edu/");
        // controller.addSeed("http://sprout.ics.uci.edu/");
        // controller.addSeed("http://student-council.ics.uci.edu/");
        // controller.addSeed("http://tastier.ics.uci.edu/");
        // controller.addSeed("http://testlab.ics.uci.edu/");
        // controller.addSeed("http://vision.ics.uci.edu/");
        // controller.addSeed("http://woda15.ics.uci.edu/");
        // controller.addSeed("http://www-db.ics.uci.edu/");
        // controller.addSeed("http://xtune.ics.uci.edu/");


        // controller.addSeed("http://www.ics.uci.edu/~eppstein/");
        // controller.addSeed("http://www.ics.uci.edu/~lopes/");
        // controller.addSeed("http://www.ics.uci.edu/~welling/");
        controller.addSeed("http://www.ics.uci.edu/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}