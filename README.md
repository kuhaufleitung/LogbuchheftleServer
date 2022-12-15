# LogbuchheftleServer
Serverapp mit RestAPI als "Logbuchdatenbank" zwischen Vereinsflieger und der logbuchheftle_flutter App.


# How to build

## Keypair für JWT Tokens:
  - Keypair in folgendem Verzeichnis generieren: src/main/resources/certs\
    `openssl genrsa -out keypair.pem 4096`
  - Extrahieren des public-keys:\
    `openssl rsa -in keypair.pem -pubout -out public.pem`
  - Extrahieren des private-keys in topk8 Format:\
    `openssl pkcs8 -topk8 -inform PEM -nocrypt -in keypair.pem -out private.pem`

## Credentials Klasse mit username und password füllen:

```
package de.schwapsch.logbuchheftleserver.auth;

public class Credentials {
    private Credentials() {
    }

    public static final String APPKEY = "hier_appkey_von_vereinsflieger";
    public static final String username = "vereinsflieger_username";
    public static final String passwordInMD5 = "passwort_in_md5";
    // PW to md5 in terminal: echo -n 'meinsicherespasswort' | md5sum

    public static final String serverAuthUsername = "admin";
    public static final String serverAuthPassword = "sicheresadminpasswort";
}
```
