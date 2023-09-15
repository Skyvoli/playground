package io.skyvoli;

import io.skyvoli.dto.PdfData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class FetchSitemapWithFasterXmlExample {

    private static final Logger logger = LogManager.getRootLogger();

    private static final String GET_URL = "https://www.arbeitsagentur.de/sitemap.xml";

    public static void main(String[] args) {
        FetchSitemapWithFasterXmlExample fetchSitemapWithFasterXMLExample = new FetchSitemapWithFasterXmlExample();
        fetchSitemapWithFasterXMLExample.fetchWithFasterXml();
    }

    public void fetchWithFasterXml() {
        try {
            Document doc = Jsoup.connect(GET_URL)
                    .maxBodySize(0)
                    .get();

            List<PdfData> pdfs = doc.getElementsByTag("url")
                    .stream()
                    .map(pdf -> new PdfData(pdf.getElementsByTag("loc").text()))
                    .toList();

            FileUtil.saveIntoFile(pdfs, "jackson.json");
        } catch (IOException e) {
            logger.error("Couldn't fetch URLs");
        }
    }

}