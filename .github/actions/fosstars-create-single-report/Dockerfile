FROM openjdk:8

RUN uname -a

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git maven jq

# TODO: check out a specific version, like "git checkout v1.1.0 "
#       or, allow setting the version as a parameter for the action
RUN mkdir -p /opt/stuff && cd /opt/stuff && \
    git clone https://github.com/SAP/fosstars-rating-core && \
    cd fosstars-rating-core && \
    mvn package -DskipTests

COPY entrypoint.sh /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]
