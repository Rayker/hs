call mvn clean
call mvn package -pl hsapi -am 
start java -jar hsapi/target/hsapi-1.0-SNAPSHOT.jar --server.port=8090 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
start java -jar hsapi/target/hsapi-1.0-SNAPSHOT.jar --server.port=8091 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db_2
pause