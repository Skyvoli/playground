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
    private static final String QUERY = "&query=";
    private static final String MAXIMUM_RECORDS = "&maximumRecords=";
    private static final String RECORD_SCHEMA = "&recordSchema=oai_dc";

    public static void main(String[] args) {
        ApiDeutscheNationalBibliothek apiDeutscheNationalBibliothek = new ApiDeutscheNationalBibliothek();
        apiDeutscheNationalBibliothek.start("Love is war", 100, "LoveIsWar.json");
        apiDeutscheNationalBibliothek.start("Hell's paradise", 50, "HellsParadise.json");
        apiDeutscheNationalBibliothek.getIsbn("Love is war", "LoveIsWarISBN.json");
        apiDeutscheNationalBibliothek.getIsbn("Hell's paradise", "HellsParadiseISBN.json");
    }

    private void start(String searchQuery, int recordNumber, String filename) {
        String url = this.buildUrl(searchQuery, recordNumber);
        Document xml = this.fetchXml(url);
        //TODO Parameter to decide which data should be added
        List<Book> books = this.serializeXml(xml);
        FileUtil.saveIntoFile(books, filename);
    }

    private void getIsbn(String searchQuery, String filename) {
        String url = this.buildUrl(searchQuery, 10);
        Document xml = this.fetchXml(url);
        List<Book> books = this.serializeXml(xml);
        FileUtil.saveIntoFile(this.reduceBookData(books), filename);
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

    private List<Book> serializeXml(Document document) {

        List<Elements> booksData = document.getElementsByTag("dc")
                .stream()
                .map(Element::children)
                .toList();

        List<Book> serializedBooks = new ArrayList<>();
        for (Elements bookData : booksData) {
            HashMap<String, String> map = new HashMap<>();
            for (Element data : bookData) {
                data.removeAttr("xmlns:tel");
                if (map.containsKey(data.tagName())) {
                    map.put(data.tagName() + data.attributes(), map.get(data.tagName()) + " + " + data.text());
                } else {
                    map.put(data.tagName() + data.attributes(), data.text());
                }
            }
            serializedBooks.add(new Book(map));
        }
        return serializedBooks;
    }

    private List<Book> reduceBookData(List<Book> books) {
        books.forEach(book -> book.getData().keySet()
                .removeIf(key -> !(key.equals("dc:title") || key.equals("dc:identifier xsi:type=\"tel:ISBN\""))));
        return books;
    }
}
