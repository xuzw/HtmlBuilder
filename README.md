# HtmlBuilder
Simple HTML Builder for Java

# How to use
Create a new HTML Builder:
```
HtmlBuilder root = new HtmlBuilder();
```

Add the 'html' tag, and display output:
```
HtmlBuilder root = new HtmlBuilder();
root.append("html");
System.out.println(root.build());
```

Create a slightly more complex structure. See that indentation (or non-indentation) is handled automatically. For certain elements (img, meta, !DOCTYPE) - these are even auto-closed, such that you do not have (for example) `<img src='image.jpg'></img>` but rather: `<img src='image.jpg'/>`

Note: `nest(x)` will return the nested element (to add more elements under it), wheras `append(x)` will simply append the element as a child, allowing you to append multiple children to the same element.

```
HtmlBuilder root = new HtmlBuilder();
root.nest("html")
      .nest("!DOCTYPE").attr("html").parent()           // .parent() will return the parent of the nested element (ie. "html")
      .append("head")
      .nest("body").attr("onload", "alert('loaded')")
        .append("h1", "Initial Heading")                // Shortcut for .nest("h1").text("Initial Heading").parent()
        .append("p", "Paragraph one")
        .nest("p").cssClass("paragraph-class")          // Shortcut for .attr("class", "paragraph-class")
          .text("Here is some bold text: ")             // A text node
          .append("b", "BOLD TEXT")
          .text(" The End")
          .parent()
        .append("p", "Paragraph Three");
System.out.println(root.build());
```

This outputs (with indentation):

```
<html>
    <!DOCTYPE html/>
    <head>
    </head>
    <body onload="alert('loaded')">
        <h1>Initial Heading</h1>
        <p>Paragraph one</p>
        <p class="paragraph-class">Here is some bold text: <b>BOLD TEXT</b> The End</p>
        <p>Paragraph Three</p>
    </body>
</html>
```
