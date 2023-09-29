package io.skyvoli.fetchdata;

import io.skyvoli.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SendHttpExample {

    private static final Logger logger = LogManager.getRootLogger();
    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String GET_URL = "https://www.arbeitsagentur.de/sitemap.xml";

    public static void main(String[] args) {
        SendHttpExample sendHttpExample = new SendHttpExample();
        sendHttpExample.fetchSitemap();
    }

    public void fetchSitemap() {
        try {
            URL url = new URL(GET_URL);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            logger.info("GET Response Code :: " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                FileUtil.saveIntoFile(response, "pdfs.json");
            } else {
                logger.error("GET request did not work.");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

