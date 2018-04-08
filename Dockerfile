# Use alternative Linux to ubuntu? As ubuntu can be more heavier than other Linux
# Is it better to do FROM java:latest? What else am I going to use Linux for? I don't think I will be installing anything
# else with apt-get
FROM ubuntu:latest
LABEL maintainer="Jack Tan"

# Install OpenJDK-8
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/

# Setup JAVA classpath, needed to run kafka command lines
ENV CLASSPATH /app

RUN export JAVA_HOME
RUN export CLASSPATH

# Start at /app folder
WORKDIR /app

# copy properties file
COPY build/ build
COPY config/ config
COPY bin/ bin
COPY libs/ libs

ENTRYPOINT ["/bin/bash", "bin/connect-distributed.sh", "config/connect-distributed.properties", "config/hexgraph-source.properties"]