
.PHONY: compile run build-jar run-jar docker-up docker-down clean

compile:
	mvn clean compile

run:
	mvn spring-boot:run

build-jar:
	mvn clean package -DskipTests

run-jar:
	java -jar target/prueba_tecnica_accenture-0.0.1-SNAPSHOT.jar

docker-up:
	docker compose up -d

docker-down:
	docker compose down

clean:
	mvn clean