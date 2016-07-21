call mvn clean
call mvn package -pl hs-central-web -am 
if ERRORLEVEL 1 (
	echo Error package:hs-central-web
	pause
	goto end
)
start java -jar hs-central-web/target/hs-central-web-1.0-SNAPSHOT.jar 

:end