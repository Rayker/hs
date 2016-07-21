call mvn clean
call mvn package -pl hsapi -am 
if ERRORLEVEL 1 (
	echo Error package:hsapi
	pause
	goto end
)
start java -jar hsapi/target/hsapi-1.0-SNAPSHOT.jar --server.port=8090 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db 		--application.city.id=1
rem start java -jar hsapi/target/hsapi-1.0-SNAPSHOT.jar --server.port=8091 --spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db_2 	--application.city.id=2

:end