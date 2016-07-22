call mvn clean package -pl hs-city -am
if ERRORLEVEL 1 (
	echo Error package:hs-city
	pause
	goto end
)
start java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5011 -jar hs-city/target/hs-city-1.0-SNAPSHOT.jar --server.port=8081 --application.city.id=1
start java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5012 -jar hs-city/target/hs-city-1.0-SNAPSHOT.jar --server.port=8082 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db_2 --application.city.id=2
start java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5013 -jar hs-city/target/hs-city-1.0-SNAPSHOT.jar --server.port=8083 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db_2 --application.city.id=3

:end