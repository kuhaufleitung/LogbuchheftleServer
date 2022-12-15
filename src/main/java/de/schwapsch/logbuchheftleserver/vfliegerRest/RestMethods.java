package de.schwapsch.logbuchheftleserver.vfliegerRest;

import de.schwapsch.logbuchheftleserver.auth.Credentials;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestMethods implements IRestMethods {

    private final String accesstoken = "";
    private final String vFliegerBaseURL = "https://www.vereinsflieger.de/interface/rest/";
    HttpClient client;

    @Override
    public void sessionGET() {
        String body = null;
        String specific = vFliegerBaseURL + "auth/accesstoken";
        String response = genericRequest(specific, RequestMethod.GET);
        if (response.isEmpty()) {
            //TODO: error handling
        } else {
            //TODO: assign accesstoken
        }
    }

    @Override
    public void loginPOST() {
        try {
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "auth/signin"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken)
                    .appendUri("username=" + Credentials.username)
                    .appendUri("password=" + Credentials.passwordInMD5)
                    .appendUri("appkey=" + Credentials.APPKEY)
                    .getCurrentURI();
            String response = genericRequest(queryURL, RequestMethod.POST);
            //TODO: write accesstoken to class
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
    }

    @Override
    public void myFlightsPOST() {
        try {
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "flight/list/myflights"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken)
                    .appendUri("username=" + Credentials.username)
                    .appendUri("count=1000")
                    .getCurrentURI();
            genericRequest(queryURL, RequestMethod.POST);
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
    }

    @Override
    public void logoutDEL() {
        try {
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "flight/list/myflights"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken).getCurrentURI();
            genericRequest(queryURL, RequestMethod.DELETE);
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
    }

    @Override
    public String genericRequest(String url, RequestMethod method) {
        //TODO: rewrite with HTTPClient
        HttpRequest.Builder requestWithoutMethod = HttpRequest.newBuilder(URI.create(url));
        HttpRequest request;
        HttpResponse<String> response = null;
        request = switch (method) {
            case GET -> requestWithoutMethod.GET().build();
            case PUT -> requestWithoutMethod.PUT(HttpRequest.BodyPublishers.ofString("")).build();
            case POST -> requestWithoutMethod.POST(HttpRequest.BodyPublishers.ofString("")).build();
            case DELETE -> requestWithoutMethod.DELETE().build();
            case HEAD, PATCH, OPTIONS, TRACE -> throw new UnsupportedOperationException();
        };
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            System.err.println("IO Exception: ");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Interrupted Exception: ");
            e.printStackTrace();
        }
        //TODO: log http code
        return response == null ? null : response.body();
    }
}
