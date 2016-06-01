# RSSReader
## About
RSSReader is an open source Java application to aggregate RSS feeds. You can compose your own lists and select which feeds should be included in each list. So feeds can be added globaly to the application and can then be added to each list we want the feed to be included in. Each list can then be sorted by date and by title, both ascending and descending. The configuration can then be saved as a .json file to the file system.

## Platforms
Since the application is writen in Java it is platform agnostic, so it can be used on any platform by using the jar version of the application. But there are installers for both Windows and Linux.

## Release
Latest release is RSSReader 1.0 and is available [here](https://github.com/1dv430/an222yp-project/releases/tag/1.0).

# Project Setup
## Requirements
There are a couple of requirements necessary to be able to contribute or do changes in the source code. Java 8 and JDK 8 is required because I have used parts of Java introduces in Java 8.

Gradle is the build system used in this project. It is required as it handles dependencies, but it also makes testing, compiling and packaging easyer.

* Java 8
* Java Development Kit 8
* Gradle

## IDE with Gradle support
Some IDEs like JetBains IntelliJ Idea have support for Gradle. You should then be able to create a Gradle project with the existing files or open the folder as a Gradle project. You should then be able from the IDE run the tasks.

## Standalone Gradle
If you are not using an IDE with Gradle support you can install Gradle to be used in the Terminal or Command Promt. You can download Gradle from their website [here](http://gradle.org/gradle-download/) or if you are on Linux it is available using APT and DNF.

## Dependencies
#### JUnit
JUnit is a unit testing framework for Java. I use it in all automated tests for this application.

Link: http://junit.org
Version used: 4.11

#### Mockito
Mockito is a testing framework I've used in addition to Junit as it provides the ability to mock objects and methods. This means I can test a specific unit without having to depend on other objects and resources.

Link: http://mockito.org/
Version used: Mockito-core 1.+ (Latest 1. release)

#### Jackson
Jackson is a JSON processing API to convert json to Java objects. This is used to save and load the save files. When saving a configuration it will take all objects and convert them to JSON, and when the user loads a configuration the json is converted to Java objects.

Link: https://github.com/FasterXML/jackson
Version used: Jackson-databind 2.0.1

#### JavaFX Gradle Plugin
JavaFx Gradle Plugin is a plugin for Gradle to run and package JavaFX application. Using this plugin I can now package the application as native installers for Windows, Linux and Mac. You do however need to be on the operating system you are packaging for.

Link: https://github.com/FibreFoX/javafx-gradle-plugin
Version used: 8.4.1

## Gradle Tasks
When using Gradle the user will run tasks to preform what they want done. Here are some examples on tasks that I use often when working on this project, the command you have to run if you are using Gradle standalone and a short description on what the task does. When using standalone Gradle you'll have to be in the root directory for the project.

PS. When running Gradle standalone keep in mind that you will have to have write privileges in the build folder so gradle can create files there.

|Task|Command|Description|
|----|----|----|
|jfxRun|gradle jfxRun|This will build the application and then start the application|
|jfxNative|gradle jfxNative|This task will compile and build the application. It will then look if you have build tools installed on your machine to produce native installers for the operating system you are on. If it can't find any it will tell you so. But if the tools are found it will run them and produce installers for you operating system.
|test|gradle test|This task will run all tests in the test directory|
|jacocoTestReport|gradle jacocoTestReport|This command will produce an HTML site where you can see statistics on the testing. For example line coverage and branch coverage. Important! You should run test task before running this|
|javadoc|gradle javadoc|This task will produce javadoc documentation on the application|
|clean|gradle clean|This task will remove build folder and everything in it|
