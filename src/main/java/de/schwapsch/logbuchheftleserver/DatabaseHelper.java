package de.schwapsch.logbuchheftleserver;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class DatabaseHelper {
    static void fixFlightKeys() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("./logbook.json"))));
        } catch (IOException e) {
            System.err.println("Problem with reading File: " + e);

        }
        assert jsonObject != null;
        Iterator<String> keys = jsonObject.keys();
        JSONObject fixedJson = new JSONObject();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!key.equals(jsonObject.getJSONObject(key).get("flid"))) {
                final JSONObject singleFlight = jsonObject.getJSONObject(key);
                final String flid = jsonObject.getJSONObject(key).get("flid").toString();
                fixedJson.put(flid, singleFlight);
            }
        }
        FileWriter writer;
        try {
            writer = new FileWriter("./logbook.json", false);
            writer.write(fixedJson.toString(4));
            writer.close();
        } catch (IOException e) {
            System.err.println("Problem with Filewriter: " + e);
        }
    }
}
