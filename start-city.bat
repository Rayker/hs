call mvn clean
call mvn package -pl hs-city -am
if ERRORLEVEL 1 (
	echo Error package:hs-city
	pause
	goto end
)
rem start java -jar hs-city/target/hs-city-1.0-SNAPSHOT.jar --server.port=8090	--application.city.id=1
start java -jar hs-city/target/hs-city-1.0-SNAPSHOT.jar --server.port=8091 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db_2 	--application.city.id=3

:end