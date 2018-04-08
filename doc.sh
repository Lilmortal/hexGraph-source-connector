#!/bin/bash

echo "Building java class"

gradlew build

echo "Creating kafka connect source docker image"

docker build -t source_kafka .

echo "Image build"

docker run -it --rm source_kafka

echo "Run container"

#docker exec -it source_kafka bash

#echo "Executing container"