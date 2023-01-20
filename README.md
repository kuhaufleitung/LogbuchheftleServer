# LogbuchheftleServer
Serverapp mit RestAPI als "Logbuchdatenbank" zwischen Vereinsflieger und der logbuchheftle_flutter App.


# How to build

## Keypair für JWT Tokens generieren:
  - Keypair in folgendem Verzeichnis generieren: src/main/resources/certs\
    `openssl genrsa -out keypair.pem 4096`
  - Extrahieren des public-keys:\
    `openssl rsa -in keypair.pem -pubout -out public.pem`
  - Extrahieren des private-keys in topk8 Format:\
    `openssl pkcs8 -topk8 -inform PEM -nocrypt -in keypair.pem -out private.pem`

## credentials.properties füllen unter src/main/resources/credentials.properties:

```
# personalized app key from vereinsflieger
cred.appkey=123456abcdef
# server login name
cred.serverUser=admin
# server password
cred.serverPw=sicheresadminpasswort
# vereinsflieger login name
cred.vfLogin=al1337
# vereinsflieger password als md5 hash
# PW to md5 in terminal: echo -n 'meinsicherespasswort' | md5sum
cred.vfPwMd5=acdaa32411eaa123

```

## Port festlegen:

`src/main/resources/application.properties` öffnen und unter `server.port=8080` den gewünschten Port eintragen.

## Server als .jar packen:
`mvn install`




# Troubleshooting

## pom.xml Plugins werden nicht erkannt

- File -> Settings -> Build,Execution,Deployment -> Build Tools -> Maven -> Haken bei `Use Plugin Registry`