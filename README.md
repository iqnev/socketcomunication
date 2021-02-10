### About
It is a multithreaded client/server applications. The server  is  based on console app which uses Java Socket programming. On the other hand, the client is based on Qt with a real GUI.

A server listens for connection requests from clients across the network or even from the same machine. Clients know how to connect to the server via a host and port number. After connecting to the server, the client recives the certain message. After receiving the message from the server, the client shows the message.

------------

###  Design
The overall design of the program was split up into two components, one for the server and one for the client. These two components interact with each other using a socket. 

##### Server
The server is Java based console application.  Тhere is an implemented message generator on the server side. For each client that is connected to the server corresponds to a message queue. Thus, each message is stored in a separate queue. This ensures that each message will be sent to the relevant client.

The UML diagram in the figure below shows how the main components of the software interact.

[![UML](https://github.com/iqnev/socketcomunication/blob/master/resources/UML_diagram.jpg "UML")](https://github.com/iqnev/socketcomunication/blob/master/resources/UML_diagram.jpg "UML")


UML Sequence Diagrams are interaction diagrams that detail how operations are carried out

[![UML Sequence](https://github.com/iqnev/socketcomunication/blob/master/resources/SequenceDiagram.png "UML Sequence")](https://github.com/iqnev/socketcomunication/blob/master/resources/SequenceDiagram.png "UML Sequence")

##### Client
I mentioned above that the Client is based on Qt and there is a GUI.


------------

### Usage

The Server application can be packaged using:
```
mvn clean install
```
It produces the server-0.0.1-SNAPSHOT-jar-with-dependencies.jar file in the /target directory
Start the server by running the following command:
```java
java -jar server.jar <port number>
```

In order to build the Client application you can use Qt editor. Also there is deployment tool for Windows or Unix systems and etc. It is designed to automate the process of creating a deployable folder containing the Qt-related dependencies (libraries, QML imports, plugins, and translations) required to run the application from that folder. [Deploying Qt Applications](https://doc.qt.io/qt-5/deployment.html "Deploying Qt Applications")


The client applications in action:
[![Clients](https://github.com/iqnev/socketcomunication/blob/master/resources/Qt_socket_client.JPG "Clients")](https://github.com/iqnev/socketcomunication/blob/master/resources/Qt_socket_client.JPG "Clients")


------------


### Requirements
The project has the following  requirements:
- Java Open JDK version 8 or Oracle jdk
- Maven version 2 or 3
- Qt 5.14.2
------------

### Development status

Current released version: **0.0.1(pre-alpha)**

------------

### License
This project is licensed under the Apache License 2.0. Check out the license text inside the `LICENSE` file.

------------

### TODO:

- Creation of unit and performance  tests for the Service and Client app¶
- Restart the Server  if a problem occurs
- Creation of complete technical documentation
- Fix potential existing problems
