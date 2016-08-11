# RSSReader
## About
RSSReader is an open source Java application to aggregate RSS feeds. You can compose your own lists and add feeds to these lists by providing a link to the RSS XML file. Each list can  be sorted by date and by title, both ascending and descending. All user created data can be saved to SQLite databases.

## Platforms
Since the application is written in Java it is platform agnostic, so it can be used on any platform by using the jar version of the application. But there are installers for both Windows and Linux.

## Release
No release is currently available.

# Project Setup
## Requirements
There are a couple of requirements necessary to be able to contribute or do changes in the source code. Java 8 and JDK 8 is required because I have used parts of Java introduces in Java 8.

Gradle is the build system used in this project. It is required as it handles dependencies, but it also makes testing, compiling and packaging easier.

* Java 8
* Java Development Kit 8
* Gradle

## IDE with Gradle support
Some IDEs like JetBains IntelliJ Idea have support for Gradle. You should then be able to create a Gradle project with the existing files or open the folder as a Gradle project. You should then be able from the IDE run the tasks.

## Standalone Gradle
If you are not using an IDE with Gradle support you can install Gradle to be used in the Terminal or Command Promt. You can download Gradle from their website [here](http://gradle.org/gradle-download/) or if you are on Linux it is available using APT and DNF.

## Dependencies
### Application dependencies
#### Javafx-gradle-plugin
Link: https://github.com/FibreFoX/javafx-gradle-plugin
Version: 8.4.1

#### Fontawesomefx
Commons, controls, and materialicons
Link: https://bitbucket.org/Jerady/fontawesomefx
Version: 8.11

#### SQLite JDBC
Link: https://bitbucket.org/xerial/sqlite-jdbc
Version: 3.8.11.2

#### Apache Commons IO
Link: https://commons.apache.org/proper/commons-io/
Version: 1.3.2

#### Jsoup
Link: https://jsoup.org/
Version: 1.7.2


### Testing dependencies
#### JUnit
Link: http://junit.org
Version: 4.11

#### Mockito
Link: http://mockito.org/
Version: 1.10.19

## Gradle Tasks
When using Gradle the user will run tasks to preform what they want done. Here are some examples on tasks that I use often when working on this project, the command you have to run if you are using Gradle standalone and a short description on what the task does. When using standalone Gradle you'll have to be in the root directory for the project.

PS. When running Gradle standalone keep in mind that you will have to have write privileges in the build folder so gradle can create files there.

|Task|Command|Description|
|----|----|----|
|myRun|gradle myRun|Runs clean, jfxNative and then jfxRun. This is so all resources are updated before running the application|
|myTest|gradle myTest|Runs clean, test, jacocoTestReport and javadoc tasks. So build folder is reset and all tests are executed. Lastly all test and javadoc documentation is created|
|jfxRun|gradle jfxRun|This will build the application and then start the application|
|jfxNative|gradle jfxNative|This task will compile and build the application. It will then look if you have build tools installed on your machine to produce native installers for the operating system you are on. If it can't find any it will tell you so. But if the tools are found it will run them and produce installers for you operating system.
|test|gradle test|This task will run all tests in the test directory|
|jacocoTestReport|gradle jacocoTestReport|This command will produce an HTML site where you can see statistics on the testing. For example line coverage and branch coverage. Important! You should run test task before running this|
|javadoc|gradle javadoc|This task will produce javadoc documentation on the application|
|clean|gradle clean|This task will remove build folder and everything in it|
