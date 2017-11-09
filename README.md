# MemoryInfoApp
Simple web application (.war) that can be deployed to the application server 
(e.g EAP, Wildfly, ...) to monitor the Java memory from outside.

## Using MemoryInfoApp
Just drop the downloaded/built memoryinfo.war file into the deployment folder of 
your application server that you would like to monitor. Restart the application server
in order to load the newly deployed application.

To access the application just open http://_server:port_**/mem** there you will find
a simple start page. The REST-Endpoint that delivers the measured values as JSON can
be found under http://_server:port_**/mem/rest/json**

## Building MemoryInfoApp
To build MemoryInfoApp a simple 
```
mvn clean install
```
is enough. Java 8 and Maven 3.3 is required. 