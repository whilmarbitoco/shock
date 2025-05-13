package org.whilmarbitoco.core.http;

public enum MimeType {
    HTML("text/html"),
    JSON("application/json"),
    XML("application/xml"),
    PLAIN("text/plain"),
    JAVASCRIPT("application/javascript"),
    CSS("text/css"),
    PNG("image/png"),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    SVG("image/svg+xml"),
    PDF("application/pdf"),
    ZIP("application/zip"),
    OCTET_STREAM("application/octet-stream"),
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data");

    private final String type;

    MimeType(String contentType) {
        type = contentType;
    }

    public String getType() {
        return type;
    }

    public static MimeType fromExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "html":
            case "htm":
                return HTML;
            case "json":
                return JSON;
            case "xml":
                return XML;
            case "txt":
                return PLAIN;
            case "js":
                return JAVASCRIPT;
            case "css":
                return CSS;
            case "png":
                return PNG;
            case "jpeg":
            case "jpg":
                return JPEG;
            case "gif":
                return GIF;
            case "svg":
                return SVG;
            case "pdf":
                return PDF;
            case "zip":
                return ZIP;
            case "form":
                return FORM_URLENCODED;
            case "multipart":
                return MULTIPART_FORM_DATA;
            default:
                return OCTET_STREAM;
        }
    }
}
