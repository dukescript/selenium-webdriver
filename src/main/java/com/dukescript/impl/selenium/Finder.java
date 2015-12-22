/*
 * #%L
 * DukeScriptScriptBrowser - a file from the "selenium webdriver" project.
 * Visit http://dukescript.com for support and commercial license.
 * %%
 * Copyright (C) 2015 Dukehoff GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package com.dukescript.impl.selenium;

import java.util.ArrayList;
import java.util.List;
import net.java.html.BrwsrCtx;
import net.java.html.js.JavaScriptBody;
import org.openqa.selenium.WebElement;

/**
 *
 * @author antonepple
 */
final class Finder {

    public static WebElement findElementByCssSelector(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByCSSSelector_impl(nativeElement, using), ctx);
    }

    public static List<WebElement> findElementsByCssSelector(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByCSSSelector_impl(nativeElement, using), ctx);
    }

    public static WebElement findElementByClassName(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByClassName_impl(nativeElement, using), ctx);
    }

    public static List<WebElement> findElementsByClassName(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByClassName_impl(nativeElement, using), ctx);
    }

    public static WebElement findElementById(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByCSSSelector_impl(nativeElement, "#" + using), ctx);
    }

    public static List<WebElement> findElementsById(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByCSSSelector_impl(nativeElement, "#" + using), ctx);
    }

    public static WebElement findElementByXPath(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(nativeElement, using), ctx);
    }

    public static List<WebElement> findElementsByXPath(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByXPath_impl(nativeElement, using), ctx);
    }

    public static WebElement findElementByLinkText(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(nativeElement, "//a[text()='" + using + "']"), ctx);
    }

    public static List<WebElement> findElementsByLinkText(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByXPath_impl(nativeElement, "//a[text()='" + using + "']"), ctx);
    }

    public static WebElement findElementByPartialLinkText(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(nativeElement, "//a[contains(text(), '" + using + "')]"), ctx);
    }

    public static List<WebElement> findElementsByPartialLinkText(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByXPath_impl(nativeElement, "//a[contains(text(), '" + using + "')]"), ctx);
    }

    public static WebElement findElementByName(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByName_impl(nativeElement, using), ctx);
    }

    public static List<WebElement> findElementsByName(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByName_impl(nativeElement, using), ctx);
    }

    public static WebElement findElementByTagName(Object nativeElement, String using, BrwsrCtx ctx) {
        return new DomNodeWebElement(Finder.findElementByTagName_impl(nativeElement, using), ctx);
    }

    public static List<WebElement> findElementsByTagName(Object nativeElement, String using, BrwsrCtx ctx) {
        return Finder.wrap(Finder.findElementsByTagName_impl(nativeElement, using), ctx);
    }

    static List<WebElement> wrap(Object[] findElementsByXPath_impl, BrwsrCtx ctx) {
        ArrayList<WebElement> arrayList = new ArrayList<>();
        for (Object object : findElementsByXPath_impl) {
            arrayList.add(new DomNodeWebElement(object, ctx));
        }
        return arrayList;
    }

    @JavaScriptBody(args = {"element", "using"}, body = "console.log('selector '+using);"
            + "return element.querySelector(using);\n")
    static native Object findElementByCSSSelector_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var nodeList = element.querySelectorAll(using);\n"
            + "var arr = [];\n"
            + "for (var i = 0; i < nodeList.length; i++) {\n"
            + "    console.log('node '+nodeList[i]);\n"
            + "    arr.push(nodeList[i]);\n"
            + "};"
            + "return arr;")
    static native Object[] findElementsByCSSSelector_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body
            = "console.log('find by name '+using);\n"
            + "return element.querySelector('*[name='+using+']');\n")
    static native Object findElementByName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var nodeList = element.querySelectorAll('*[name='+using+']');\n"
            + "var arr = [];\n"
            + "for (var i = 0; i < nodeList.length; i++) {\n"
            + "    console.log('node '+nodeList[i]);\n"
            + "    arr.push(nodeList[i]);\n"
            + "};"
            + "return arr;")
    static native Object[] findElementsByName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body
            = "var nodeList = element.getElementsByTagName(using);\n"
            + "return nodeList[0];")
    static native Object findElementByTagName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var nodeList = element.getElementsByTagName(using);\n"
            + "var arr = [];\n"
            + "for (var i = 0; i < nodeList.length; i++) {\n"
            + "    console.log('node '+nodeList[i]);\n"
            + "    arr.push(nodeList[i]);\n"
            + "};"
            + "return arr;")
    static native Object[] findElementsByTagName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var nodeList = element.getElementsByClassName(using);\n"
            + "return nodeList[0];")
    static native Object findElementByClassName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var nodeList = element.getElementsByClassName(using);\n"
            + "var arr = [];\n"
            + "for (var i = 0; i < nodeList.length; i++) {\n"
            + "    console.log('node '+nodeList[i]);\n"
            + "    arr.push(nodeList[i]);\n"
            + "};"
            + "return arr;")
    static native Object[] findElementsByClassName_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "return document.evaluate(using, element, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;")
    static native Object findElementByXPath_impl(Object element, String using);

    @JavaScriptBody(args = {"element", "using"}, body = "var xpr =  document.evaluate(using, element, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);\n"
            + "var arr = [];\n"
            + "var current = xpr.iterateNext();\n"
            + "while (current){\n"
            + "    arr.push(current);\n"
            + "    current = xpr.iterateNext();\n"
            + "}"
            + "return arr;")
    static native Object[] findElementsByXPath_impl(Object element, String using);

    @JavaScriptBody(args = {}, body = "return document;")
    static native Object getDocument();

}
