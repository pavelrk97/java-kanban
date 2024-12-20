package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseHttpHandler implements HttpHandler {

    protected static final Logger logger = Logger.getLogger(BaseHttpHandler.class.getName());

    protected void sendText(HttpExchange h, String text, Integer code) {
        try (h) {   // вынесен (h) тк HttpExchange реализует auto closable
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending response", e);
        }
    }

    protected void sendInternalError(HttpExchange h) {
        try (h) {
            h.sendResponseHeaders(500, 0);
        } catch (Exception e) {
            logger.log(Level.WARNING, "error while send internal error", e);
        }
    }
}
