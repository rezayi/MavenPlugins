# Maven Plugins
In this repository I want to add some useful maven plugins.

List of them is as bellow:

### 1- Java to Protobuf (under develop yet)
Using this plugin you can flag a Java class by `@ConvertToProto` annotation.<br> 
Then this plugin will convert that to .proto file and makes a transferable version of these classes and their connected classes using protobuf.<br>


### 2- Remote Service (not started yet)  
Using this plugin you can define an interface and flag it with `@RemoteService`.<br>
Then It will define protobuf messages and services in `.proto` files.
Finally, you can define a remote service. 
It means that you need just to define an interface in one microservice app and implement it in another one.
This library will connect them to each other. <br>
enjoy it ;-)
