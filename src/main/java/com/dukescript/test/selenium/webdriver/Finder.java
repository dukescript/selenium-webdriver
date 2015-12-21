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
package com.dukescript.test.selenium.webdriver;

import java.util.ArrayList;
import java.util.List;
import net.java.html.BrwsrCtx;
import net.java.html.js.JavaScriptBody;
import org.openqa.selenium.WebElement;

/**
 *
 * @author antonepple
 */
public class Finder {

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

    @JavaScriptBody(args = {"element", "id"}, body = "return element.getElementById(id);")
    static native Object findElementById_impl(Object element, String id);

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
    
    
    @JavaScriptBody(args = { "element", "using" }, body = "return document.evaluate(using, element, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;")
    static native Object findElementByXPath_impl(Object element,String using);

    @JavaScriptBody(args = { "element", "using" }, body = "var xpr =  document.evaluate(using, element, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);\n"
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
