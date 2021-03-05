FROM openjdk:8

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git maven

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]
