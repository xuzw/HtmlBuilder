package builder;

/**
 * Created by Josh on 5/04/2016.
 */
public class Runner {
    public static void main(String[] args) {
        HtmlBuilder root = new HtmlBuilder();
        root
            .nest("!DOCTYPE").attr("html").parent()
            .nest("html")
                .append("head")
                .nest("body").attr("onload", "alert()")
                    .append("h1", "Heading")
                    .nest("p").cssClass("paragraph")
                        .text("Here is some bold text: ")
                        .append("b", "Bold Text")
                        .text(" The End.")
                        .parent()
                    .append("p", "next paragraph");

        System.out.println(root.build());
    }
}
