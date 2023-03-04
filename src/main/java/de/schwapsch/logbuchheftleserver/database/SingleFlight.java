package de.schwapsch.logbuchheftleserver.database;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public record SingleFlight(JSONObject fullFlightJSON, String flid, LocalDate dateOfFlight,
                           String departureTime) implements Comparable<SingleFlight> {

    public SingleFlight(JSONObject fullFlightJSON, String flid) {
        this(fullFlightJSON, flid, null, null);
    }

    public SingleFlight(JSONObject fullFlightJSON, String flid, LocalDate dateOfFlight, String departureTime) {

        this.fullFlightJSON = fullFlightJSON;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateOfFlight = LocalDate.parse(fullFlightJSON.getString("dateofflight"), formatter);
        this.departureTime = fullFlightJSON.getString("departuretime");
        this.flid = flid;
    }


    @Override
    public int compareTo(SingleFlight otherFlight) {
        //we need to compare Dates first, if there is multiple flights a day -> hours
        //if still same hour -> minutes
        //minus sign is returned because we need the reverse order

        //same instance?
        if (this.equals(otherFlight)) {
            return 0;
        }
        if (!this.dateOfFlight.isEqual(otherFlight.dateOfFlight)) {
            return -this.dateOfFlight.compareTo(otherFlight.dateOfFlight);
        } else {
            String[] depFirst = this.departureTime.split(":");
            String[] depSecond = otherFlight.departureTime.split(":");
            if (!depFirst[0].equals(depSecond[0])) {
                return -depFirst[0].compareTo(depSecond[0]);
            } else {
                return -depFirst[1].compareTo(depSecond[1]);
            }
        }
    }
}
