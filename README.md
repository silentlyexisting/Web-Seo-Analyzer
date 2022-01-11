### Hexlet tests and linter status:
[![Actions Status](https://github.com/silentlyexisting/java-project-lvl4/workflows/hexlet-check/badge.svg)](https://github.com/silentlyexisting/java-project-lvl4/actions)
![example workflow](https://github.com/silentlyexisting/java-project-lvl4/actions/workflows/java-ci.yml/badge.svg)
<a href="https://codeclimate.com/github/silentlyexisting/java-project-lvl4/maintainability"><img src="https://api.codeclimate.com/v1/badges/ecf1b1c23447564f268d/maintainability" /></a>
<a href="https://codeclimate.com/github/silentlyexisting/java-project-lvl4/test_coverage"><img src="https://api.codeclimate.com/v1/badges/ecf1b1c23447564f268d/test_coverage" /></a>

### <b>Веб Анализатор страниц | Web Seo Analyzer</b>

Project demo: https://stormy-anchorage-29897.herokuapp.com/

Frontend (Bootstrap, CDN). Framework Javalin (Routing, Presentation). Database (ORM Ebean, Migrations, query builders). Deploy (PaaS, Heroku). HTTP (including execution of requests). Integration testing. Logging.

Фронтенд (Bootstrap, CDN). Фреймворк Javalin (Маршрутизация, Представление). База данных (ORM Ebean, Миграции, query builders). Деплой (PaaS, Heroku). HTTP (в том числе выполнение запросов). Интеграционное тестирование. Логгирование.

<b>Пример использования локально:</b>
```java
1. Склонируйте репозиторий: "git clone https://github.com/silentlyexisting/java-project-lvl4.git" или "git clone https://git.heroku.com/stormy-anchorage-29897.git"
2. Для запуска проекта используйте команду "make start" или "APP_ENV=development ./gradlew run". В проекте в качестве development переменной окружения используется база данных h2.
3. Локально по дефолту проект запускается на порту 3400, но можно изменить в классе "App" методе "getPort".
```