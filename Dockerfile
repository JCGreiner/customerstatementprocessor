FROM nexus.taf.nl:18079/official/amazoncorretto:17.0.1
ARG JAR_FILE=/target/*.jar
COPY ${JAR_FILE} /opt/app.jar
ADD entrypoint.sh entrypoint.sh

ENV JAVAAPP_OPTS="-Xmx512m -XX:+HeapDumpOnOutOfMemoryError -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9009 -XX:HeapDumpPath=/dump_location/memleakqs.hprof"
ENTRYPOINT ["sh", "entrypoint.sh"]