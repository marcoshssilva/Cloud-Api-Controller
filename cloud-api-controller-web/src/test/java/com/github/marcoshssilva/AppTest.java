package com.github.marcoshssilva;

import com.github.marcoshssilva.cloud.api.controller.web.App;
import com.github.marcoshssilva.cloud.api.controller.web.WebServer;
import com.github.marcoshssilva.cloud.api.controller.web.impl.UndertowServerWebServerImpl;

public class AppTest {
    private final WebServer webServer;
    private final App app;
    public AppTest() {
        this.webServer = new UndertowServerWebServerImpl();
        this.app = new App(webServer);
    }

    public void testApp() throws Exception {
        app.run(new String[]{});
        webServer.close();
    }
}
