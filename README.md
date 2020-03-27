# NCLI
#### A primitive take on a classic feature

## Description

A simple URL shortener.

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
