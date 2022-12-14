package de.schwapsch.logbuchheftleserver.Database;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class DatabaseHelper {
    static void fixFlightKeys(JSONObject response) {
        Iterator<String> keys = response.keys();
        String contentOfLogbook;
        try {
            contentOfLogbook = new String(Files.readAllBytes(Paths.get("./logbook.json")));
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

                    //search if insertion is necessary
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
            }
            FileWriter writer;
            writer = new FileWriter("./logbook.json", false);
            writer.write(logbookWithNewFlights.toString(4));
            writer.close();
        } catch (IOException e) {
            System.err.println("Problem: " + e);
        }
    }
}
