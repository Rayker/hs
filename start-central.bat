call mvn clean
call mvn package -pl hs-central-web -am 
if ERRORLEVEL 1 (
	echo Error package:hs-central-web
	pause
	goto end
)
rem start java -jar hs-central-web/target/hs-central-web-1.0-SNAPSHOT.war 
start java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar hs-central-web/target/hs-central-web-1.0-SNAPSHOT.war 

:end