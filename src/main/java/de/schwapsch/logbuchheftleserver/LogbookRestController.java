package de.schwapsch.logbuchheftleserver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogbookRestController {
    @RequestMapping(value = "/rest/logbook", method = RequestMethod.GET)
    @ResponseBody
    public String logbook() {
        return "";
    } //TODO: update logbook periodically
    //TODO: return json with only: flid, callsign, pilotName, copilotName, dateOfFlight, departureTime, arrivalTime, flightDuration, startType;

    @RequestMapping(value = "/rest/logbook/manualupdate", method = RequestMethod.PUT)
    public void manualupdate() {

    }
}
