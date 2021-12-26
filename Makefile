install:
	./gradlew clean install
	
build:
	./gradlew clean build
	
run-dist:
	./build/install/app/bin/app
	
reports:
	./gradlew test
	./gradlew jacocoTestReport
