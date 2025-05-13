package org.whilmarbitoco.core.http;

public class Response {
    private final StringBuilder body = new StringBuilder();
    private boolean handled = false;
    private MimeType mimeType = MimeType.HTML;

    public void send(String content) {
        body.append(content);
        handled = true;
    }

    public boolean isHandled() {
        return handled;
    }

    public void contentType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + mimeType.getType() + "\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "\r\n" +
                body.toString();
    }
}

