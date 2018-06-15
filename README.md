# Introduction
This program is made by STS based on Maven. The aim is for extracting and parsing resources, methods and parameters of Google APIs (JSON) and APIGee APIs (WADL). After then, insert data into MS SQL after tokenisation.

Unfortunately, APIGee console service has not been available since April 2018. (Link: https://apigee.com/about/blog/api-technology/were-sunsetting-classic-api-consoles)

### Links
- Google DIscovery API: https://developers.google.com/discovery/
- APIGee.com: https://apigee.com

### dependencies
gson, jdom2, commons-io, mysql-connector-java

# Getting Started
- Resources directory has SQL queries for creating tables.
- GoogleAPIParser.java has downLoadAPIs() and parsingJson(). First of all, download API documents, and then parsing them.
- APIGeeParser.java has only parsing process.
- private static final String dirPath is the path of directory for saving APIs.

# Build and Test
- Each module has the main() method.
- RUN AS Java application in STS because the outcome is JAR file based on stand alone.
