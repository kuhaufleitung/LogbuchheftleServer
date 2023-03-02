package de.schwapsch.logbuchheftleserver.database;

import org.joda.time.LocalTime;
import org.json.JSONObject;

public record SingleFlight(JSONObject fullFlightJSON, String flid, LocalTime dateOfFlight,
                           String departureTime) implements Comparable<SingleFlight> {

    public SingleFlight(JSONObject fullFlightJSON, String flid) {
        this(fullFlightJSON, flid, null, null);
    }

    public SingleFlight(JSONObject fullFlightJSON, String flid, LocalTime dateOfFlight, String departureTime) {

        this.fullFlightJSON = fullFlightJSON;
        this.dateOfFlight = LocalTime.parse(fullFlightJSON.getString("dateofflight"));
        this.departureTime = fullFlightJSON.getString("departuretime");
        this.flid = flid;
    }


    @Override
    public int compareTo(SingleFlight otherFlight) {
        //we need to compare Dates first, if there is multiple flights a day -> hours
        //if still same hour -> minutes
        if (this.dateOfFlight != otherFlight.dateOfFlight) {
            return this.dateOfFlight.compareTo(otherFlight.dateOfFlight);
        } else {
            String[] depFirst = this.departureTime.split(":");
            String[] depSecond = otherFlight.departureTime.split(":");
            if (!depFirst[0].equals(depSecond[0])) {
                return depFirst[0].compareTo(depSecond[0]);
            } else {
                return depFirst[1].compareTo(depSecond[1]);
            }
        }
    }
}
