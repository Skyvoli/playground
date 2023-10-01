package io.skyvoli.fetchdata;

import io.skyvoli.FileUtil;
import io.skyvoli.dto.Book;
import org.apache.commons.text.CaseUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ApiDeutscheNationalBibliothek {

    private static final Logger logger = LogManager.getRootLogger();
    private static final String BASE_URL = "https://services.dnb.de/sru/dnb?version=1.1";
    private static final String OPERATION = "&operation=searchRetrieve";
    private static final String QUERY = "&query=";
    private static final String MAXIMUM_RECORDS = "&maximumRecords=";
    private static final String RECORD_SCHEMA = "&recordSchema=oai_dc";

    public static void main(String[] args) {
        ApiDeutscheNationalBibliothek apiDNB = new ApiDeutscheNationalBibliothek();
        apiDNB.getAll("Love is war", 100);
        apiDNB.getAll("Hell's paradise", 50);
        apiDNB.getAll("Das Voynich Hotel", 40);
        apiDNB.getIsbn("Love is war");
        apiDNB.getIsbn("Hell's paradise");
        apiDNB.getBookFromIsbn("3-7555-0244-5");
        apiDNB.getBookFromIsbn("2-88921-092-8");
    }

    private void getAll(String searchQuery, int recordNumber) {
        String url = this.buildUrl(searchQuery, recordNumber);
        Document xml = this.fetchXml(url);
        List<Book> books = this.serializeXml(xml);
        FileUtil.saveIntoFile(books, generateFilename(searchQuery)  + ".json");
    }

    private void getIsbn(String searchQuery) {
        List<String> keys = Arrays.asList("dc:title",  "dc:identifier xsi:type=\"tel:ISBN\"");
        String url = this.buildUrl(searchQuery, 30);
        Document xml = this.fetchXml(url);
        List<Book> books = this.serializeXml(xml, keys);
        FileUtil.saveIntoFile(books, generateFilename(searchQuery) + "ISBN.json");
    }

    private void getBookFromIsbn(String searchQuery) {
        String url = this.buildUrl(searchQuery, 5);
        Document xml = this.fetchXml(url);
        List<Book> books = this.serializeXml(xml);
        FileUtil.saveIntoFile(books, "ISBN_" + searchQuery + ".json");
    }

    private String buildUrl(String searchQuery, int recordNumber) {
        return BASE_URL + OPERATION + QUERY + searchQuery + MAXIMUM_RECORDS + recordNumber + RECORD_SCHEMA;
    }

    private Document fetchXml(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            logger.error("Couldn't fetch document");
        }
        return doc;
    }

    private List<Book> serializeXml(Document document, List<String> tags) {

        List<Elements> booksData = document.getElementsByTag("dc")
                .stream()
                .map(Element::children)
                .toList();

        List<Book> serializedBooks = new ArrayList<>();
        for (Elements bookData : booksData) {
            HashMap<String, String> map = new HashMap<>();
            for (Element data : bookData) {
                data.removeAttr("xmlns:tel");
                if (tags.isEmpty() || tags.contains(data.tagName() + data.attributes())) {
                    if (map.containsKey(data.tagName())) {
                        map.put(data.tagName() + data.attributes(), map.get(data.tagName()) + " + " + data.text());
                    } else {
                        map.put(data.tagName() + data.attributes(), data.text());
                    }
                } else {
                    logger.debug(data.tagName() + " couldn't be added");
                }
            }
            serializedBooks.add(new Book(map));
        }
        return serializedBooks;
    }

    private List<Book> serializeXml(Document document) {
        return serializeXml(document, new ArrayList<>());
    }

    private String generateFilename(String name) {
        return CaseUtils.toCamelCase(name.trim(), true, ' ');
    }
}
