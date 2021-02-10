### About
It is a multithreaded client/server applications. The server  is  based on console app which uses Java Socket programming. On the other hand, the client is based on Qt with a real GUI.

A server listens for connection requests from clients across the network or even from the same machine. Clients know how to connect to the server via a host and port number. After connecting to the server, the client recives the certain message. After receiving the message from the server, the client shows the message.

------------

###  Design
The overall design of the program was split up into two components, one for the server and one for the client. These two components interact with each other using a socket. 
##### Server
The server is Java based console application.  Тhere is an implemented message generator on the server side. For each client that is connected to the server corresponds to a message queue. Thus, each message is stored in a separate queue. This ensures that each message will be sent to the relevant client.
