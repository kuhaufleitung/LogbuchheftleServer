package de.schwapsch.logbuchheftleserver.database;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

class TreeSetSerializer implements Serializable {

    private TreeSetSerializer() {
    }

    //we are creating a JSON array, not! an object to preserve the order
    static JSONArray serialize(TreeSet<SingleFlight> set) {
        JSONArray result = new JSONArray();
        Iterator<SingleFlight> flightIt = set.iterator();
        do {
            SingleFlight currentFlight = flightIt.next();
            result.put(currentFlight.fullFlightJSON());
        } while (flightIt.hasNext());

        return result;
    }
}
