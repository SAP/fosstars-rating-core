FROM openjdk:8

RUN uname -a

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git maven

RUN mkdir -p /opt/stuff && cd /opt/stuff && \
    git clone https://github.com/SAP/fosstars-rating-core && \
    cd fosstars-rating-core && \
    git checkout v1.1.0 && \
    mvn package -DskipTests

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]
