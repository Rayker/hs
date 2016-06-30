call mvn clean
call mvn package -pl hsapi -am 
call java -jar hsapi/target/hsapi-1.0-SNAPSHOT.jar 
pause