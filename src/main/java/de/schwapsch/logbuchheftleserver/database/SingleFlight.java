package de.schwapsch.logbuchheftleserver.database;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public record SingleFlight(JSONObject fullFlightJSON, String flid, LocalDate dateOfFlight,
                           String departureTime, String createTime) implements Comparable<SingleFlight> {

    public SingleFlight(JSONObject fullFlightJSON, String flid) {
        this(fullFlightJSON, flid, null, null, null);
    }

    public SingleFlight(JSONObject fullFlightJSON, String flid, LocalDate dateOfFlight, String departureTime, String createTime) {

        this.fullFlightJSON = fullFlightJSON;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateOfFlight = LocalDate.parse(fullFlightJSON.getString("dateofflight"), formatter);
        this.departureTime = fullFlightJSON.getString("departuretime");
        this.createTime = fullFlightJSON.getString("createtime").substring(11);
        this.flid = flid;
    }


    @Override
    public int compareTo(SingleFlight otherFlight) {
        // Compare dates first
        int dateComparison = this.dateOfFlight.compareTo(otherFlight.dateOfFlight);
        if (dateComparison != 0) {
            return dateComparison;
        }

        // If dates are the same, compare departure times
        String[] depFirstFlight = this.departureTime.split(":");
        String[] depSecondFlight = otherFlight.departureTime.split(":");
        int hourComparison = depFirstFlight[0].compareTo(depSecondFlight[0]);
        if (hourComparison != 0) {
            return hourComparison;
        }

        // If hours are the same, compare minutes
        int minutesComparison = depFirstFlight[1].compareTo(depSecondFlight[1]);
        if (minutesComparison != 0) {
            return minutesComparison;
        }

        //we ignore seconds because 2 starts in 1min is impossible
        //for duplicates return creation time
        String[] createFirst = this.createTime.split(":");
        String[] createSecond = otherFlight.createTime.split(":");

        hourComparison = createFirst[0].compareTo(createSecond[0]);
        if (hourComparison != 0) {
            return hourComparison;
        }

        // If hours are the same, compare minutes
        minutesComparison = createFirst[1].compareTo(createSecond[1]);
        if (minutesComparison != 0) {
            return minutesComparison;
        }

        // If minutes still the same, return seconds
        return createFirst[2].compareTo(createSecond[2]);
    }
}
