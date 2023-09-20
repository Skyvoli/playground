package io.skyvoli;

import io.skyvoli.dto.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiDeutscheNationalBibliothek {

    private static final Logger logger = LogManager.getRootLogger();

    private static final String BASE_URL = "https://services.dnb.de/sru/dnb?version=1.1";
    private static final String OPERATION = "&operation=searchRetrieve";
    private static final String RECORD_NUMBER = "&maximumRecords=100";
    private static final String RECORD_SCHEMA = "&recordSchema=oai_dc";

    public static void main(String[] args) {
        new ApiDeutscheNationalBibliothek().start();
    }

    private void start() {
        String searchQuery = "Love is war";
        String url = this.buildUrl(searchQuery);
        Document xml = this.fetchXml(url);
        this.serializeXml(xml);
    }

    private String buildUrl(String searchQuery) {
        //TODO Make query adjustable
        return BASE_URL + OPERATION + "&query=WOE%3D" + searchQuery + RECORD_NUMBER + RECORD_SCHEMA;
    }

    private Document fetchXml(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            logger.error("Couldn't fetch URLs");
        }
        return doc;
    }

    private void serializeXml(Document document) {

        List<Elements> booksData = document.getElementsByTag("dc")
                .stream()
                .map(Element::children)
                .toList();

        List<Book> serializedBooks = new ArrayList<>();
        for (Elements bookData : booksData) {
            HashMap<String, String> map = new HashMap<>();
            for (Element data : bookData) {
                if (map.containsKey(data.tagName())) {
                    map.put(data.tagName() + data.attributes(), map.get(data.tagName()) + " + " + data.text());
                } else {
                    map.put(data.tagName() + data.attributes(), data.text());
                }
            }
            serializedBooks.add(new Book(map));
        }

        FileUtil.saveIntoFile(serializedBooks, "books.json");
    }
}
