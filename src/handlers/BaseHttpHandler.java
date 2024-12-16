package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected void sendText(HttpExchange h, String text, Integer code) {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void sendInternalError(HttpExchange h) {
        try {
            h.sendResponseHeaders(500, 0);
            h.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
