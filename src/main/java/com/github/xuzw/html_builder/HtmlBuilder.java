package com.github.xuzw.html_builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年4月10日 上午10:58:50
 */
public class HtmlBuilder {
    private HtmlBuilder parent;
    private final int depth;
    private final String tagName;
    private final String textNode;
    private static final List<String> IMMEDIATE_CLOSE = Arrays.asList("link", "img", "meta", "!DOCTYPE");
    private final List<HtmlBuilder> children = new ArrayList<>();
    private final Map<String, String> attributes = new HashMap<>();

    public HtmlBuilder() {
        this(null, null, null);
    }

    private HtmlBuilder(HtmlBuilder parent, String tagName, String textNode) {
        this.parent = parent;
        this.tagName = tagName;
        this.textNode = textNode;
        this.depth = parent != null ? parent.depth + 1 : -1;
    }

    public HtmlBuilder child(String tagName) {
        HtmlBuilder builder = new HtmlBuilder(this, tagName, null);
        children.add(builder);
        return builder;
    }

    public HtmlBuilder append(String tagName) {
        child(tagName);
        return this;
    }

    public HtmlBuilder append(String tagName, String text) {
        child(tagName).text(text);
        return this;
    }

    public HtmlBuilder appendAll(List<HtmlBuilder> builders) {
        builders.forEach(builder -> append(builder));
        return this;
    }

    public HtmlBuilder append(HtmlBuilder builder) {
        children.add(builder.setParent(this));
        return this;
    }

    private HtmlBuilder setParent(HtmlBuilder parent) {
        this.parent = parent;
        return this;
    }

    public HtmlBuilder parent() {
        return parent;
    }

    public HtmlBuilder attr(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public HtmlBuilder attr(String key) {
        attributes.put(key, null);
        return this;
    }

    public HtmlBuilder id(String value) {
        return attr("id", value);
    }

    public HtmlBuilder cssClass(String value) {
        return attr("class", value);
    }

    public HtmlBuilder src(String value) {
        return attr("src", value);
    }

    public HtmlBuilder href(String value) {
        return attr("href", value);
    }

    public HtmlBuilder text(String value) {
        children.add(new HtmlBuilder(this, null, value));
        return this;
    }

    public String build() {
        return build(true);
    }

    private String build(boolean indent) {
        final StringBuilder html = new StringBuilder();
        final String indentation = getIndentation();
        final boolean indentChildren = !hasTextChildren();
        if (tagName != null) {
            if (indent) {
                html.append(indentation);
            }
            html.append('<').append(tagName);
            for (String attrKey : attributes.keySet()) {
                html.append(' ').append(attrKey);
                final String value = attributes.get(attrKey);
                if (value != null) {
                    html.append('=').append('"').append(value).append('"');
                }
            }
            if (IMMEDIATE_CLOSE.contains(tagName)) {
                return html.append("/>").toString();
            }
            html.append('>');
        } else if (textNode != null) {
            return html.append(textNode).toString();
        }
        for (HtmlBuilder builder : children) {
            if (indentChildren) {
                html.append('\n');
            }
            html.append(builder.build(indentChildren));
        }
        if (tagName != null) {
            if (indentChildren) {
                html.append('\n').append(indentation);
            }
            html.append("</").append(tagName).append('>');
        }
        return html.toString();
    }

    private boolean hasTextChildren() {
        return children.stream().filter(c -> c.textNode != null).findFirst().isPresent();
    }

    private String getIndentation() {
        String indent = "";
        for (int i = 0; i < depth; i++) {
            indent += "    ";
        }
        return indent;
    }
}
