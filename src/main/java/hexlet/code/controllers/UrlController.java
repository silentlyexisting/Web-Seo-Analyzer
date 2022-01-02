package hexlet.code.controllers;

import hexlet.code.Url;
import hexlet.code.query.QUrl;
import io.ebean.PagedList;
import io.javalin.http.Handler;
import io.javalin.http.HttpCode;

import java.net.URL;
import java.util.List;

public class UrlController {
    public static Handler createUrl = ctx -> {
        String enteredUrl = ctx.formParam("url");
        URL checkUrl = new URL(enteredUrl);
        String url = checkUrl.getProtocol() + checkUrl.getAuthority();

        if (url == null) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.status(HttpCode.UNPROCESSABLE_ENTITY);
            ctx.redirect("/urls");
            return;
        }

        Url checkUrlInDb = new QUrl()
                .name.equalTo(url)
                .findOne();

        if (checkUrlInDb != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.status(HttpCode.UNPROCESSABLE_ENTITY);
            ctx.redirect("/urls");
            return;
        }

        Url newUrl = new Url(url);
        newUrl.save();
        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.redirect("/urls");
    };


    public static Handler showUrls = ctx -> {
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(null);
        final int rowsPerPage = 10;
        int offset = (page - 1) * rowsPerPage;

        PagedList<Url> pagedList = new QUrl()
                .setFirstRow(offset)
                .setMaxRows(rowsPerPage)
                .orderBy().id.asc()
                .findPagedList();

        List<Url> urls = pagedList.getList();

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
