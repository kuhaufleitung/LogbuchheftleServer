package de.schwapsch.logbuchheftleserver.database;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
            UpdateDatabase db = new UpdateDatabase(null, true);
            JSONArray have = db.sortJSON(data);

            final String[] want = {"4037734", "4037733", "4037732", "4037731", "4037730"};
            final int wantAmountOfFlights = 5;

            for (int i = 0; i < wantAmountOfFlights; i++) {
                assertEquals(want[i], have.getJSONObject(i).getString("flid"));
            }

            assertEquals(wantAmountOfFlights, have.length());
        } catch (JSONException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}