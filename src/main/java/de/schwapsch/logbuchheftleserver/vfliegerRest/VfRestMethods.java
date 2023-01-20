package de.schwapsch.logbuchheftleserver.vfliegerRest;

import org.json.JSONObject;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

public class VfRestMethods implements IRestMethods {
    private final ResourceBundle resource = ResourceBundle.getBundle("credentials");
    private String accesstoken;
    private final String appkey = resource.getString("cred.appkey");
    private final String vfLoginName = resource.getString("cred.vfLogin");
    private final String vfPwInMd5 = resource.getString("cred.vfPwMd5");
    private final String vFliegerBaseURL = "https://www.vereinsflieger.de/interface/rest/";
    private HttpStatusCode httpStatusCodeOfMyFlights;
    HttpClient client = HttpClient.newHttpClient();

    public JSONObject retrieveData() {
        JSONObject jsonResponse = null;
        if (sessionGET() == HttpStatusCode.valueOf(200)) {
            if (loginPOST() == HttpStatusCode.valueOf(200)) {
                jsonResponse = myFlightsPOST();
                if (httpStatusCodeOfMyFlights != HttpStatusCode.valueOf(200)) {
                    //TODO: logging
                }
                if (logoutDEL() != HttpStatusCode.valueOf(200)) {
                    //TODO: logging
                }
            } else {
                //TODO: logging
            }
        } else {
            //TODO: logging
        }
        return jsonResponse;
    }

    @Override
    public HttpStatusCode sessionGET() {
        String specific = vFliegerBaseURL + "auth/accesstoken";
        String response = genericRequest(specific, RequestMethod.GET);
        final HttpStatusCode httpStatusCode;
        if (response.isEmpty()) {
            httpStatusCode = HttpStatusCode.valueOf(400);
            //TODO: error handling, change code?
        } else {
            JSONObject responseInJson = new JSONObject(response);
            accesstoken = responseInJson.get("accesstoken").toString();
            httpStatusCode = HttpStatusCode.valueOf(responseInJson.getInt("httpstatuscode"));
        }
        return httpStatusCode;
    }

    @Override
    public HttpStatusCode loginPOST() {
        try {
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "auth/signin"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken)
                    .appendUri("username=" + vfLoginName)
                    .appendUri("password=" + vfPwInMd5)
                    .appendUri("appkey=" + appkey)
                    .getCurrentURI();
            String response = genericRequest(queryURL, RequestMethod.POST);
            JSONObject responseInJson = new JSONObject(response);
            return HttpStatusCode.valueOf(responseInJson.getInt("httpstatuscode"));
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
        return HttpStatusCode.valueOf(400);
    }

    @Override
    public JSONObject myFlightsPOST() {
        try {
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "flight/list/myflights"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken)
                    .appendUri("username=" + vfLoginName)
                    .appendUri("count=1000")
                    .getCurrentURI();
            String response = genericRequest(queryURL, RequestMethod.POST);
            JSONObject responseInJson = new JSONObject(response);
            httpStatusCodeOfMyFlights = HttpStatusCode.valueOf(responseInJson.getInt("httpstatuscode"));
            return responseInJson;
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    public HttpStatusCode logoutDEL() {
        try {//TODO: wrong URI?
            URIHelper helper = new URIHelper(new URI(vFliegerBaseURL + "auth/signout"));
            String queryURL = helper.appendUri("accesstoken=" + accesstoken).getCurrentURI();
            String response = genericRequest(queryURL, RequestMethod.DELETE);
            JSONObject responseInJson = new JSONObject(response);
            return HttpStatusCode.valueOf(responseInJson.getInt("httpstatuscode"));
        } catch (URISyntaxException e) {
            System.err.println("MalformedURISyntax!: ");
            e.printStackTrace();
        }
        return HttpStatusCode.valueOf(400);
    }

    public HttpStatusCode getHttpCodeOfMyFlights() {
        return httpStatusCodeOfMyFlights;
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
        return response == null ? null : response.body();
    }
}
