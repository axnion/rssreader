# RSSReader
## About
RSSReader is an open source Java application to aggregate RSS feeds. You can compose your own lists and select which feeds should be included in each list. So feeds can be added globaly to the application and can then be added to each list we want the feed to be included in. Each list can then be sorted by date and by title, both ascending and descending. The configuration can then be saved as a .json file to the file system.

## Platforms
Since the application is writen in Java it is platform agnostic, so it can be used on any platform by using the jar version of the application. But there are installers for both Windows and Linux.

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
