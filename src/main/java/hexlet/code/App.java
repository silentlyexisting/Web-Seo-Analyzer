package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = getApp();
        app.start();
    }

    public static Javalin getApp() {
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World!"));
        return app;
    }
}
