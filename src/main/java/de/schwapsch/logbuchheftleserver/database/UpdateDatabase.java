package de.schwapsch.logbuchheftleserver.database;

import de.schwapsch.logbuchheftleserver.LogbuchheftleServerApplication;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeSet;

public class UpdateDatabase {
    private final String pathOfMainJar = URLDecoder.decode(LogbuchheftleServerApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);
    private final JSONObject response;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UpdateDatabase(JSONObject response) {
        this.response = response;

        stripHttpCodeFromJson();
        createFileIfNotExists();
        JSONObject dataFromDisk = loadFromDisk();
        JSONObject logbookWithUpdatedFlights = appendNewFlights(dataFromDisk);
        writeToDisk(logbookWithUpdatedFlights);
    }

    // CTOR for testing purposes.
    public UpdateDatabase(JSONObject data, boolean __) {
        response = data;
    }


    private void createFileIfNotExists() {
        File logbookFile = new File(Paths.get(pathOfMainJar + "/logbook.json").toUri());
        if (!logbookFile.exists()) {
            try {
                logbookFile.createNewFile();
            } catch (IOException e) {
                logger.error(e + ": Couldn't Create File from " + UpdateDatabase.class.getSimpleName());
                throw new RuntimeException();
            }
        }
    }


    //* this also fixes every flight key with its corresponding flid
    private JSONObject appendNewFlights(JSONObject jsonFromFile) {
        Iterator<String> keys = response.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!key.equals(response.getJSONObject(key).get("flid"))) {//remove in prod
                final JSONObject singleFlight = response.getJSONObject(key);
                final String flid = response.getJSONObject(key).get("flid").toString();
                insertIfNecessary(jsonFromFile, singleFlight, flid);
            }
        }
        return jsonFromFile;
    }

    private void insertIfNecessary(JSONObject logbookWithNewFlights, JSONObject singleFlight, String flid) {
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

    private void stripHttpCodeFromJson() {
        Iterator<String> keys = response.keys();
        boolean foundKey = false;
        while (keys.hasNext() && !foundKey) {
            String key = keys.next();
            if (key.contains("httpstatuscode")) {
                response.remove(key);
                foundKey = true;
            }
        }
    }

    public JSONObject loadFromDisk() {
        String dataFromDisk;
        try {
            dataFromDisk = new String(Files.readAllBytes(Paths.get(pathOfMainJar + "/logbook.json")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataFromDisk.isEmpty() ? new JSONObject() : new JSONObject(dataFromDisk);
    }

    private void writeToDisk(JSONObject data) {
        FileWriter writer;
        try {
            createBackup();
            writer = new FileWriter(pathOfMainJar + "/logbook.json", false);
            writer.write(data.toString(4));//TODO: 4 necessary?
            writer.close();
        } catch (IOException e) {
            logger.error(e + ": Couldn't write to disk from " + UpdateDatabase.class.getSimpleName());
            throw new RuntimeException(e);
        }
    }

    public JSONArray sortJSON(JSONObject data) {
        TreeSet<SingleFlight> sortedFlightList = new TreeSet<>(new FlightComparator());
        Iterator<String> keys = data.keys();
        do {
            String currentFlight = keys.next();
            JSONObject jsonOfFlight = data.getJSONObject(currentFlight);
            sortedFlightList.add(new SingleFlight(jsonOfFlight, jsonOfFlight.getString("flid")));
        } while (keys.hasNext());
        return TreeSetSerializer.serialize(sortedFlightList);
    }

    private void createBackup() {
        //TODO: write backup func
    }
}
