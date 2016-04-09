package builder;

import java.util.*;

/**
 * Created by Josh on 5/04/2016.
 */
public class HtmlBuilder {
    private HtmlBuilder parent;
    private final int depth;
    private final String tagname;
    private final String textNode;
    private static final List<String> IMMEDIATE_CLOSE = Arrays.asList("img", "meta", "!DOCTYPE");
    private final List<HtmlBuilder> children = new ArrayList<>();
    private final Map<String, String> attributes = new HashMap<>();

    public HtmlBuilder() {
        this(null, null, null);
    }

    private HtmlBuilder(HtmlBuilder parent, String tagname, String textNode) {
        this.parent = parent;
        this.tagname = tagname;
        this.textNode = textNode;
        this.depth = parent != null ? parent.depth + 1 : -1;
    }

    public HtmlBuilder nest(String tagname) {
        HtmlBuilder builder = new HtmlBuilder(this, tagname, null);
        children.add(builder);
        return builder;
    }

    public HtmlBuilder append(String tagname) {
        nest(tagname);
        return this;
    }

    public HtmlBuilder append(String tagname, String text) {
        nest(tagname).text(text);
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
        if (tagname != null) {
            if (indent) {
                html.append(indentation);
            }
            html.append('<').append(tagname);
            for (String attrKey : attributes.keySet()) {
                html.append(' ').append(attrKey);
                final String value = attributes.get(attrKey);
                if (value != null) {
                    html.append('=').append('"').append(value).append('"');
                }
            }
            if (IMMEDIATE_CLOSE.contains(tagname)) {
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
        if (tagname != null) {
            if (indentChildren) {
                html.append('\n').append(indentation);
            }
            html.append("</").append(tagname).append('>');
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
