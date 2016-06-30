call mvn clean
call mvn package -pl hsclient -am 
call java -jar hsclient/target/hsclient-1.0-SNAPSHOT.jar 
pause