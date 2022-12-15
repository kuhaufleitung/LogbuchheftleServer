package de.schwapsch.logbuchheftleserver.vfliegerRest;

import java.net.URI;
import java.net.URISyntaxException;

public class URIHelper {
    URI currentURI;

    public URIHelper(URI currentURI) {
        this.currentURI = currentURI;
    }

    public URIHelper appendUri(String appendQuery) {
        URI oldUri;
        try {
            oldUri = currentURI;
            currentURI = new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(),
                    oldUri.getQuery() == null ? appendQuery : oldUri.getQuery() + "&" + appendQuery, oldUri.getFragment());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    String getCurrentURI() {
        return this.currentURI == null ? null : this.currentURI.toString();
    }
}