FROM python:3

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y git jupyter python3-pandas python3-yaml cowsay

ENV PATH $PATH:/usr/games

COPY entrypoint.sh /opt/entrypoint.sh

ENTRYPOINT [ "/opt/entrypoint.sh" ]
