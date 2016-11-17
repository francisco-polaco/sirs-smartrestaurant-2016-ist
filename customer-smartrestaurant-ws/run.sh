#!/bin/bash

mvn clean
mvn generate-sources
mvn install
mvn exec:java
