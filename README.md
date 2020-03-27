# URL Shortener

## Description

A simple URL shortener.

To put it bluntly, I'm a beginner when it comes to Java and I didn't have any
experience with Spring, so this was the first time I have
ever tried building something with it. Admittedly I had to rely on resources
provided online to get started, however if given this problem again (or a
similar one), I'm confident in my ability to be able to do it without as much
guidance.

I chose Spring because it is a well-documented and popular Java framework that
would allow me to build something quickly.

For my database, I chose Redis because there was plenty of documentation and
I needed to store a minimal amount of data. However, I realized (too late) that
my application should also verify if a URL already exists. Given the
restrictions of Redis, I might have chosen a different database in order
to mitigate this. Another option would be to have another data structure that
holds all of the urls given to me by the user and checking that before
saving a new URL.

The front end is in pure vanilla Javascript, CSS and was very easy to implement.

There are a lot of things I could improve upon but I learned a lot from building
this project. The technologies used were mostly new to me such as Spring and
Gradle (I have used Maven in the past).

## Files

#### *UrlShortenerApplication*
Main application file

### Controller

#### *URLController*
Handles posting the URL to database

#### *IndexController*
Renders index html

### Repository

#### *URLRepository*
Handles data management in Redis

### Service

#### *URLConversionService*
Handles all of the logic with converting urls and ids

### Common

#### *URLValidator*
Validates URLs passed from the user

#### *IDConverter*
Converts ids from base10 to base62 and vice-versa

## How to Use

* Clone the repo onto your computer
* Build the project with `gradle build`
* Run your Redis server `redis-server`
* Start the Spring server `./gradlew bootRun`


## Todo

* Add tests
* Fix null pointer error that is coming from the URLController
* Verify if a value within Redis already exists
