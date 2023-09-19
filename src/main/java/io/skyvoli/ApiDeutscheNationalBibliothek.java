package io.skyvoli;

import io.skyvoli.dto.FullRecord;
import io.skyvoli.dto.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class ApiDeutscheNationalBibliothek {

    private static final Logger logger = LogManager.getRootLogger();

    private static final String BASE_URL = "https://services.dnb.de/sru/dnb?version=1.1&operation=searchRetrieve";

    public static void main(String[] args) {
        new ApiDeutscheNationalBibliothek().start();
    }

    private void start() {
        String url = this.buildUrl();
        Document xml = this.fetchXml(url);
        this.serializeXml(xml);
    }

    private String buildUrl() {
        //TODO Make query adjustable
        return  BASE_URL + "&query=WOE%3D" + "Mika's%20Magic%20Market" + "&maximumRecords=100";
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
        List<Record> records = document.getElementsByTag("record")
                .stream()
                .map(record -> new Record(record.getElementsByTag("dc:title").text(), record.getElementsByTag("dc:publisher").text()))
                .toList();

        //TODO pretty printing into file
        List<FullRecord> recordsComplete = document.getElementsByTag("record")
                .stream()
                //TODO Children Tag included
                .map(element -> new FullRecord(element.children().eachText()))
                .toList();

        FileUtil.saveIntoFile(records, "books.json");
        FileUtil.saveIntoFile(recordsComplete, "fullRecord.json");
    }
}
