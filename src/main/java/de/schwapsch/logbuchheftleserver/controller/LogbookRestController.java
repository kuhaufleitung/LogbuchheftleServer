package de.schwapsch.logbuchheftleserver.controller;

import de.schwapsch.logbuchheftleserver.database.UpdateDatabase;
import de.schwapsch.logbuchheftleserver.vfliegerRest.VfRestMethods;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class LogbookRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/rest/logbook")
    @ResponseBody
    public String logbook() {
        logger.info("logbook() called");
        VfRestMethods connectionVF = new VfRestMethods();
        JSONObject response;
        response = connectionVF.retrieveData();

        //update server logbook
        UpdateDatabase database = new UpdateDatabase(response);
        return database.sortJSON(database.loadFromDisk()).toString();
    } //TODO: update logbook periodically
    //TODO: return json with only: flid, callsign, pilotName, copilotName, dateOfFlight, departureTime, arrivalTime, flightDuration, startType;

    @PutMapping(value = "/rest/logbook/manualupdate")
    public void manualupdate() {
        logger.info("manualupdate() called");
    }

    @PutMapping(value = "/rest/logbook/remarks")
    public String addRemarks(@RequestBody String remarkJson) {
        logger.info("addRemarks() called");
        //TODO: parse remark + add to flight + save flight
        return null;
    }

    @GetMapping(value = "/")
    public String logCheck() {
        logger.info("logCheck() called");
        return "Logged in!";
    }
}
