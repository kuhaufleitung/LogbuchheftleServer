package de.schwapsch.logbuchheftleserver.vfliegerRest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class RestMethodsTest {
    @Test void allOperations() {
        RestMethods methods = new RestMethods();
        HttpStatusCode sessionCode = methods.sessionGET();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), sessionCode);
        HttpStatusCode loginCode = methods.loginPOST();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), loginCode);
        methods.myFlightsPOST();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), methods.getHttpCodeOfMyFlights());
        HttpStatusCode logoutCode = methods.logoutDEL();
        Assertions.assertEquals(HttpStatusCode.valueOf(200), logoutCode);
    }

}