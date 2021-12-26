install:
	./gradlew clean install
	
build:
	./gradlew clean build
	
.PHONY: build
	
run-dist:
	./build/install/app/bin/app
	
reports:
	./gradlew test
	./gradlew jacocoTestReport
