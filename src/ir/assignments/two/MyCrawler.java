package ir.assignments.two;

import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import org.apache.http.Header;

public class MyCrawler extends WebCrawler {

    private final static Pattern BAD_DATA = Pattern.compile(".*eppstein/.*/Data.*");

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|jpeg|wav|zip|rar|wmv|doc|docx|tar|gz"
                                                    + "|png|mp3|mp4|zip|gz|bmp|pdf|ppt|pptx|ps|mov|bigwig|bw|txt|xls|xlsx))$");
    private final static Pattern DOMAINS = Pattern.compile("^http://.*\\.ics\\.uci\\.edu/.*");
    private final static Pattern BAD_DOMAINS = Pattern.compile("^http://(archive|calendar|duttgroup|drzaius"
                                                        + "|fano|ftp|kdd|wics)\\.ics\\.uci\\.edu/.*");
    private final static Pattern QUERIES = Pattern.compile(".*[\\?@=].*");
    private final static Pattern PROSPECT = Pattern.compile(".*ics\\.uci\\.edu.*prospective.*");
    private final static Pattern PIX = Pattern.compile(".*eppstein/pix.*");
    private final static Pattern REPO = Pattern.compile(".*lucicoderepository.*");
    
    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && !QUERIES.matcher(href).matches()
                && DOMAINS.matcher(href).matches() && !BAD_DATA.matcher(href).matches() 
                && !BAD_DOMAINS.matcher(href).matches() && !PROSPECT.matcher(href).matches()
                && !PIX.matcher(href).matches() && !REPO.matcher(href).matches();
    }

     /**
      * This function is called when a page is fetched and ready
      * to be processed by your program.
      */
     @Override
     public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        logger.info("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.info("Domain: '{}'", domain);
        logger.info("Sub-domain: '{}'", subDomain);
        logger.debug("Path: '{}'", path);
        logger.debug("Parent page: {}", parentUrl);
        logger.debug("Anchor text: {}", anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();

            CrawlStat.addPage(subDomain, Integer.toString(docid), url, text);

            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());

            Header[] responseHeaders = page.getFetchResponseHeaders();
            if (responseHeaders != null) {
              logger.debug("Response headers:");
              for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
              }
            }

            logger.debug("=============");
         }
    }
}