package de.schwapsch.logbuchheftleserver.vfliegerRest;

import org.springframework.web.bind.annotation.RequestMethod;

public interface IRestMethods {

    void sessionGET();

    void loginPOST();

    void myFlightsPOST();

    void logoutDEL();

    String genericRequest(String url, RequestMethod method);
}
