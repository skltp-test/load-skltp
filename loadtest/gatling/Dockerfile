FROM eclipse-temurin:11-alpine

# gating version
ENV GATLING_VERSION=3.9.5

# create directory for gatling install
RUN mkdir -p /opt/gatling

# install gatling
RUN apk add --update wget bash libc6-compat && \
  mkdir -p /tmp/downloads && \
  wget -q -O /tmp/downloads/gatling-$GATLING_VERSION.zip \
  https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/$GATLING_VERSION/gatling-charts-highcharts-bundle-$GATLING_VERSION-bundle.zip && \
  mkdir -p /tmp/archive && cd /tmp/archive && \
  unzip /tmp/downloads/gatling-$GATLING_VERSION.zip && \
  mv /tmp/archive/gatling-charts-highcharts-bundle-$GATLING_VERSION/* /opt/gatling/ && \
  rm -rf /tmp/* && \ 
  adduser -HD -u 1000 gatling && \
  chown 1000:1000 -R /opt/gatling

# change context to gatling directory
WORKDIR  /opt/gatling

# set directories below to be mountable from host
VOLUME ["/opt/gatling/results", "/opt/gatling/cert"]

# set environment variables
ENV PATH=/opt/gatling/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
ENV GATLING_HOME=/opt/gatling

# some default values
ENV TRUSTSTORE_TYPE=pkcs12
ENV TRUSTSTORE_FILE=""
ENV TRUSTSTORE_PASSWORD=""
ENV KEYSTORE_TYPE=pkcs12
ENV KEYSTORE_FILE=""
ENV KEYSTORE_PASSWORD=""

USER 1000

ENTRYPOINT ["gatling.sh"]