package com.github.xuzw.html_builder;

/**
 * Created by Josh on 5/04/2016.
 */
public class Runner {
    public static void main(String[] args) {
        HtmlBuilder root = new HtmlBuilder();
        root
            .child("!DOCTYPE").attr("html").parent()
            .child("html")
                .append("head")
                .child("body").attr("onload", "alert()")
                    .append("h1", "Heading")
                    .child("p").cssClass("paragraph")
                        .text("Here is some bold text: ")
                        .append("b", "Bold Text")
                        .text(" The End.")
                        .parent()
                    .append("p", "next paragraph");

        System.out.println(root.build());
    }
}
