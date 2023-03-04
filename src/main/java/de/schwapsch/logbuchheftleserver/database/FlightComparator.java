package de.schwapsch.logbuchheftleserver.database;

import java.util.Comparator;

public class FlightComparator implements Comparator<SingleFlight> {

    @Override
    public int compare(SingleFlight singleFlight, SingleFlight t1) {
        return singleFlight.compareTo(t1);
    }
}
