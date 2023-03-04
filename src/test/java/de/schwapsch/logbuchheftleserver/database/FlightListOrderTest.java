package de.schwapsch.logbuchheftleserver.database;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightListOrderTest {

    @Test
    void testOrder() {
        try {
            File mockdata = new File("./src/test/java/de/schwapsch/logbuchheftleserver/database/mockdata.json");
            Scanner scanner = new Scanner(mockdata).useDelimiter("\\A");
            String contentFromFile = scanner.next();
            JSONObject data = new JSONObject(contentFromFile);
            //sorting happens here
            UpdateDatabase db = new UpdateDatabase(data, true);

            TreeSet<String> want = new TreeSet<>();
            int wantAmountOfFlights = 5;

            want.add("4037730");
            want.add("4037731");
            want.add("4037732");
            want.add("4037733");
            want.add("4037734");

            assertEquals(new JSONObject((Map) want).toString(), db.getCurrentData().toString());
            assertEquals(wantAmountOfFlights, db.getCurrentData().length());
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}