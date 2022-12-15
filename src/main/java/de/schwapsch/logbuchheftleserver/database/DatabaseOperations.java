package de.schwapsch.logbuchheftleserver.database;

import de.schwapsch.logbuchheftleserver.LogbuchheftleServerApplication;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class DatabaseOperations {
    private static final String pathOfMainJar = URLDecoder.decode(LogbuchheftleServerApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);

    static void fixFlightKeys(JSONObject response) {
        Iterator<String> keys = response.keys();
        String contentOfLogbook;
        try {
            contentOfLogbook = new String(Files.readAllBytes(Paths.get(pathOfMainJar + "/logbook.json")));
            JSONObject logbookWithNewFlights;
            if (contentOfLogbook.isEmpty()) {
                logbookWithNewFlights = new JSONObject();
            } else {
                logbookWithNewFlights = new JSONObject(contentOfLogbook);
            }
            while (keys.hasNext()) {
                String key = keys.next();
                if (!key.equals(response.getJSONObject(key).get("flid"))) {//remove in prod
                    final JSONObject singleFlight = response.getJSONObject(key);
                    final String flid = response.getJSONObject(key).get("flid").toString();

                    insertIfNecessary(logbookWithNewFlights, singleFlight, flid);
                }
            }
            FileWriter writer;
            writer = new FileWriter(pathOfMainJar + "/logbook.json", false);
            writer.write(logbookWithNewFlights.toString(4));
            writer.close();
        } catch (IOException e) {
            System.err.println("Problem: " + e);
        }
    }

    private static void insertIfNecessary(JSONObject logbookWithNewFlights, JSONObject singleFlight, String flid) {
        if (!logbookWithNewFlights.isEmpty()) {
            Iterator<String> keysFileIt = logbookWithNewFlights.keys();
            while (keysFileIt.hasNext()) {
                if (!keysFileIt.next().equals(flid)) {
                    logbookWithNewFlights.put(flid, singleFlight);
                    break;
                }
            }
        } else {
            logbookWithNewFlights.put(flid, singleFlight);
        }
    }

    void refreshJsonData(JSONObject data) {
        try {
            File logbookFile = new File(new File(".").getCanonicalPath());
            if (logbookFile.exists()) {
                //TODO: update JSON at flid > latest flid
            }
        } catch (IOException e) {
            System.err.println("logbookfile path threw exep: ");
            e.printStackTrace();
        }
    }
}
