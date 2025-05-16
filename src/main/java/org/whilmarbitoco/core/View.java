package org.whilmarbitoco.core;

import org.whilmarbitoco.core.utils.Config;
import org.whilmarbitoco.core.utils.File;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class View {

    protected String viewPath = Config.viewPath();
    protected String template;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(.*?)}}");

    public View() {}

    public View(String absolute) {
        this.template = absolute;
    }

    public void template(String template) {
        this.template = viewPath + template;
    }

    public String render(String view) {
        if (view == null) return "";

        if (view.endsWith(".html") && template != null) {
            String temp = File.loadContent(template);
            String content = Config.viewPath() + view;

            return temp.replace("{{content}}", content);
        }

        if (!view.endsWith(".html") && template != null) {
            String temp = File.loadContent(template);

            return temp.replace("{{content}}", view);
        }

        return view;
    }

    public String render(String view, Map<String, Object> context) {
        String template = render(view);
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            String variableName = matcher.group(1).trim();
            Object variableValue = context.get(variableName);
            String replacement = (variableValue != null) ? Matcher.quoteReplacement(variableValue.toString()) : "";
            matcher.appendReplacement(buffer, replacement);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String getStackTraceString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
