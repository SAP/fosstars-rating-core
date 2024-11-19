FROM maven:3.9.5-sapmachine-17

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git jq

COPY build_fosstars.sh /opt/build_fosstars.sh
COPY cleanup_for_config_if_necessary.sh /opt/cleanup_for_config_if_necessary.sh
COPY entrypoint.sh /opt/entrypoint.sh

ENTRYPOINT [ "/opt/entrypoint.sh" ]
