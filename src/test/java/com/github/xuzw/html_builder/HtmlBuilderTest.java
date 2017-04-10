package com.github.xuzw.html_builder;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年4月10日 上午11:06:17
 */
public class HtmlBuilderTest {
    public static void main(String[] args) {
        HtmlBuilder root = new HtmlBuilder();
        root.child("!DOCTYPE").attr("html");
        HtmlBuilder html = root.child("html");
        HtmlBuilder head = html.child("head");
        HtmlBuilder body = html.child("body");
        body.attr("onload", "alert()").append("h1", "Heading").child("p").cssClass("paragraph").text("Here is some bold text: ").append("b", "Bold Text").text(" The End.").parent().append("p", "next paragraph");
        System.out.println(root.build());
    }
}
