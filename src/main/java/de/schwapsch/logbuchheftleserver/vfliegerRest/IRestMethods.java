package de.schwapsch.logbuchheftleserver.vfliegerRest;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RequestMethod;

public interface IRestMethods {

    HttpStatusCode sessionGET();

    HttpStatusCode loginPOST();

    JSONObject myFlightsPOST();

    HttpStatusCode logoutDEL();

    String genericRequest(String url, RequestMethod method);
}
