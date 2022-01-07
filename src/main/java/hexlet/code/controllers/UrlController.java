package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.query.QUrlCheck;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class UrlController {
    public static Handler createUrl = ctx -> {
        try {
            URL enteredUrl = new URL(ctx.formParam("url"));
            String url = enteredUrl.getProtocol() + "://" + enteredUrl.getAuthority();

            boolean isUrlExist = new QUrl()
                    .name.equalTo(url)
                    .exists();

            if (!isUrlExist) {
                new Url(url).save();
                ctx.sessionAttribute("flash-type", "success");
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.redirect("/urls");
                return;
            }

            ctx.sessionAttribute("flash", "Страница уже существует");
        } catch (MalformedURLException exception) {
            ctx.sessionAttribute("flash", "Некорректный URL");
        }
        ctx.status(HttpCode.UNPROCESSABLE_ENTITY);
        ctx.sessionAttribute("flash-type", "danger");
        ctx.render("index.html");

    };

    public static Handler showUrls = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
        final int rowsPerPage = 10;
        int offset = (page - 1) * rowsPerPage;

        List<Url> urls = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy()
                .id.asc()
                .findList();

        Map<Long, UrlCheck> checks = new QUrlCheck()
                .url.id.asMapKey()
                .statusCode.desc()
                .findMap();

        ctx.attribute("checks", checks);
        ctx.attribute("page", page);
        ctx.attribute("urls", urls);
        ctx.render("urls/index.html");
    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl()
                .id.equalTo(id)
                .findOne();

        if (url == null) {
            ctx.sessionAttribute("flash", "Неверный id");
            ctx.redirect("/urls");
            return;
        }
        ctx.attribute("url", url);
        ctx.render("urls/show.html");
    };

    public static Handler checkUrl = ctx -> {
        Long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = new QUrl()
                .id.equalTo(id)
                .findOne();

        UrlCheck urlCheck = new UrlCheck();
        try {
            HttpResponse<String> response = Unirest
                    .get(url.getName())
                    .asString();

            String body = response.getBody();
            Document doc = Jsoup.parse(body);

            urlCheck.setTitle(doc.title());

            Element h1Element = doc.selectFirst("h1");
            String h1 = h1Element != null ? h1Element.text() : "";
            urlCheck.setH1(h1);

            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = descriptionElement != null ? descriptionElement.attr("content") : "";
            urlCheck.setDescription(description);

            urlCheck.setUrl(url);
            urlCheck.setStatusCode(response.getStatus());

            urlCheck.save();
        } catch (UnirestException exception) {
            ctx.sessionAttribute("flash-type", "danger");
            ctx.sessionAttribute("flash", "Ой, что-то пошло не так");
            ctx.redirect("/urls");
        }
        ctx.attribute("urlCheck", urlCheck);
        ctx.redirect("/urls/" + id);
    };
}
