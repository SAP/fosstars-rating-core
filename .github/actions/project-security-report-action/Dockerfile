FROM openjdk:8

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git jq

RUN wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
    HASH=c35a1803a6e70a126e80b2b3ae33eed961f83ed74d18fcd16909b2d44d7dada3203f1ffe726c17ef8dcca2dcaa9fca676987befeadc9b9f759967a8cb77181c0 && \
    echo "$HASH apache-maven-3.6.3-bin.tar.gz" | sha512sum --check --status && \
    tar xf apache-maven-3.6.3-bin.tar.gz -C /opt

ENV M2_HOME="/opt/apache-maven-3.6.3"
ENV MAVEN_HOME="/opt/apache-maven-3.6.3"
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

RUN mvn -version

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]
