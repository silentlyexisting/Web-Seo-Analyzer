package hexlet.code.controllers;

import hexlet.code.domain.Url;
import hexlet.code.domain.UrlCheck;
import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.query.QUrlCheck;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class UrlController {
    public static Handler createUrl = ctx -> {
        String flash = "";
        try {
            URL enteredUrl = new URL(ctx.formParam("url"));
            String url = enteredUrl.getProtocol() + "://" + enteredUrl.getAuthority();

            boolean isUrlExist = new QUrl()
                    .name.equalTo(url)
                    .exists();

            if (!isUrlExist) {
                new Url(url).save();
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.redirect("/urls");
                return;
            }
            flash = "Страница уже существует";
            ctx.status(HttpCode.UNPROCESSABLE_ENTITY);
            ctx.sessionAttribute("flash", flash);
            ctx.render("index.html");
        } catch (MalformedURLException exception) {
            flash = "Некорректный URL";
            ctx.status(HttpCode.UNPROCESSABLE_ENTITY);
            ctx.sessionAttribute("flash", flash);
            ctx.render("index.html");
        }

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
}
