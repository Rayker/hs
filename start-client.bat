call mvn clean
call mvn package -pl hsclient -am 
if ERRORLEVEL 1 (
	echo Error package:hsclient
	pause
	goto end
)
start java -jar hsclient/target/hsclient-1.0-SNAPSHOT.jar -Dorg.apache.activemq.SERIALIZABLE_PACKAGES="*" 

:end