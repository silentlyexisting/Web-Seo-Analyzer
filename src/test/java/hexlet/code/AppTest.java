package hexlet.code;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class AppTest {
    private static Javalin app;
    private static String baseUrl;
    private static Url url;
    private static Transaction transaction;
    private static MockWebServer mockServer;

    @BeforeAll
    public static void beforeAll() throws IOException {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;

        url = new Url("https://examplesite.com");
        url.save();

//        mockServer.start();
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest
                .get(baseUrl)
                .asString();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).contains("Анализатор страниц");
    }

    @Test
    void testShowUrls() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();

        String body = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains("Сайты");
        assertThat(body).contains("Код ответа");
    }

    @Test
    void testCreateNewUrl() {
        HttpResponse<String> postRequest = Unirest
                .post(baseUrl + "/urls")
                .field("url", "https://testsite.com")
                .asString();

        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls")
                .asString();

        String body = response.getBody();

        Url url = new QUrl()
                .name.equalTo("https://testsite.com")
                .findOne();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(url).isNotNull();
        assertThat(body).contains("Страница успешно добавлена");
        assertThat(body).contains("https://testsite.com");
    }

    @Test
    void testShowUrl() {
        HttpResponse<String> response = Unirest
                .get(baseUrl + "/urls/" + url.getId())
                .asString();

        String body = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(body).contains(url.getName());
        assertThat(body).contains(String.valueOf(url.getId()));
    }

    @Test
    void invalidUrlTest() {
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/urls")
                .field("url", "youtube.net")
                .asString();

        String body = response.getBody();

        Url url = new QUrl()
                .name.equalTo("youtube.net")
                .findOne();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(url).isNull();
        assertThat(body).contains("Некорректный URL");
        assertThat(body).doesNotContain("youtube.net");
    }

    @Test
    void duplicateUrlTest() {
        HttpResponse<String> response = Unirest
                .post(baseUrl + "/urls")
                .field("url", "https://testsite.com")
                .asString();
        String body = response.getBody();

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(body).contains("Страница уже существует");
    }

//    @Test
//    void mockTest() throws IOException {
//        String mockUrl = mockServer.url("/").toString();
//
//        Path path = Paths.get("src/test/resources/fixtures/test.html").toAbsolutePath().normalize();
//        String data = Files.readString(path);
//
//        MockResponse mockResponse = new MockResponse()
//                .addHeader("Content-Type", "text/html; charset=utf-8")
//                .setResponseCode(200)
//                .setBody(data);
//    }
}
