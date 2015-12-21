package com.dukescript.test.selenium.webdriver;

/*
 * #%L
 * FindsByTest - a file from the "selenium webdriver" project.
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
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FindsByTest {

    private static WebDriverFX driver;
    private static TestModel testModel;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(FindsByTest.class.getResource("testWithModel.html"));
        driver.executeAndWait(new Runnable() {
            @Override
            public void run() {
                testModel = new TestModel("Hello", "World");
                testModel.applyBindings();
            }
        });
    }

    @Test
    public void withModel() {
        // please note that this method is not executed in BrwsrCtx
        // to allow seeing updates in the Browser while debugging a test
        WebElement element = driver.findElement(By.id("target"));
        Assert.assertEquals("Hello", element.getText());
        WebElement button = driver.findElement(By.id("button"));
        button.click();
        Assert.assertEquals("World", element.getText());
        WebElement input = driver.findElement(By.id("input"));
        input.clear();
        input.sendKeys("DukeScript");
        button.click();
        Assert.assertEquals("DukeScript", element.getText());
        WebElement findElement = driver.findElement(By.cssSelector(".bla"));
        Assert.assertNotNull(findElement);
        Assert.assertEquals("DukeScript", findElement.getText());
        List<WebElement> findElements = driver.findElements(By.cssSelector(".bla"));
//        Assert.assertEquals(1, findElements.size());
    }

    @Test
    public void Element_tagName() {
        WebElement element = driver.findElement(By.id("input"));
        Assert.assertEquals("INPUT", element.getTagName());
    }

    @Test
    public void findElementByCSSSelector() {
        WebElement element = driver.findElement(By.cssSelector(".css-selector"));
        Assert.assertEquals("blabla", element.getText());
        WebElement child = driver.findElement(By.cssSelector(".parent .child"));
        Assert.assertEquals("blubla", child.getText());

    }

    @Test
    public void Element_findElementByCSSSelector() {
        WebElement parent = driver.findElement(By.cssSelector(".parent"));
        WebElement child = parent.findElement(By.cssSelector(".child"));
        Assert.assertEquals("blubla", child.getText());
    }

    @Test
    public void findElementsByCSSSelector() {
        List<WebElement> elements = driver.findElements(By.cssSelector(".css-selector"));
        Assert.assertEquals(1, elements.size());
        elements = driver.findElements(By.cssSelector(".some"));
        Assert.assertEquals(3, elements.size());
    }

    @Test
    public void Element_findElementsByCSSSelector() {
        WebElement parent = driver.findElement(By.cssSelector(".parent"));
        List<WebElement> elements = parent.findElements(By.cssSelector(".child"));
        Assert.assertEquals(2, elements.size());
        elements = parent.findElements(By.cssSelector(".some"));
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void findElementByClassName() {
        WebElement element = driver.findElement(By.className("css-selector"));
        Assert.assertEquals("blabla", element.getText());
    }

    @Test
    public void Element_findElementByClassName() {
        WebElement parent = driver.findElement(By.className("parent"));
        WebElement child = parent.findElement(By.className("child"));
        Assert.assertEquals("blubla", child.getText());
    }

    @Test
    public void findElementsByClassName() {
        List<WebElement> elements = driver.findElements(By.className("css-selector"));
        Assert.assertEquals(1, elements.size());
        elements = driver.findElements(By.className("some"));
        Assert.assertEquals(3, elements.size());
    }

    @Test
    public void Element_findElementsByClassName() {
        WebElement parent = driver.findElement(By.className("parent"));
        List<WebElement> elements = parent.findElements(By.className("child"));
        Assert.assertEquals(2, elements.size());
        elements = parent.findElements(By.className("some"));
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void findElementById() {
        WebElement element = driver.findElement(By.id("my-id"));
        Assert.assertEquals("blabla", element.getText());
    }

    @Test
    public void Element_findElementById() {
        WebElement parent = driver.findElement(By.id("parent-id"));
        WebElement child = parent.findElement(By.id("child-id"));
        Assert.assertEquals("blubla", child.getText());
    }

    @Test
    public void findElementsById() {
        List<WebElement> elements = driver.findElements(By.id("my-id"));
        Assert.assertEquals(1, elements.size());
        elements = driver.findElements(By.id("some-id"));
        Assert.assertEquals(3, elements.size());
    }

    @Test
    public void Element_findElementsById() {
        WebElement parent = driver.findElement(By.id("parent-id"));
        List<WebElement> elements = parent.findElements(By.id("child-id"));
        Assert.assertEquals(2, elements.size());
        elements = parent.findElements(By.id("some-id"));
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void findElementByXPath() {
        WebElement element = driver.findElement(By.xpath("/html/body/div/div/span"));
        Assert.assertEquals("xpath", element.getText());
    }

    @Test
    public void Element_findElementByXPath() {
        WebElement parent = driver.findElement(By.id("parent-id"));
        WebElement child = parent.findElement(By.xpath("div/span"));
        Assert.assertEquals("xpath", child.getText());
    }

    @Test
    public void findElementsByXPath() {
        List<WebElement> elements = driver.findElements(By.xpath("//div"));
        Assert.assertEquals(6, elements.size());
    }

    @Test
    public void Element_findElementsByXPath() {
        WebElement parent = driver.findElement(By.id("parent-id"));
        List<WebElement> elements = parent.findElements(By.xpath("div"));
        Assert.assertEquals(2, elements.size());
    }

    @Test
    public void findByLinkText() {
        WebElement element = driver.findElement(By.linkText("Text of link"));
        Assert.assertEquals("Text of link", element.getText());
    }

    @Test
    public void findByPartialLinkText() {
        WebElement element = driver.findElement(By.partialLinkText("Text"));
        Assert.assertEquals("Text of link", element.getText());
    }

    @Test
    public void findElementsByLinkText() {
        List<WebElement> findElements = driver.findElements(By.linkText("Text of link"));
        Assert.assertEquals(1, findElements.size());
        findElements = driver.findElements(By.linkText("Other link"));
        Assert.assertEquals(2, findElements.size());
    }

    @Test
    public void findElementsByPartialLinkText() {
        List<WebElement> findElements = driver.findElements(By.partialLinkText("Text"));
        Assert.assertEquals(2, findElements.size());
        findElements = driver.findElements(By.partialLinkText("Other link"));
        Assert.assertEquals(2, findElements.size());
        findElements = driver.findElements(By.partialLinkText("link"));
        Assert.assertEquals(4, findElements.size());
    }

    @Test
    public void Element_findByLinkText() {
        WebElement links = driver.findElement(By.id("links"));
        WebElement element = links.findElement(By.linkText("Text of link"));
        Assert.assertEquals("Text of link", element.getText());
    }

    @Test
    public void Element_findByPartialLinkText() {
        WebElement links = driver.findElement(By.id("links"));
        WebElement element = links.findElement(By.partialLinkText("Text"));
        Assert.assertEquals("Text of link", element.getText());
    }

    @Test
    public void Element_findElementsByLinkText() {
        WebElement links = driver.findElement(By.id("links"));
        List<WebElement> findElements = links.findElements(By.linkText("Text of link"));
        Assert.assertEquals(1, findElements.size());
        findElements = links.findElements(By.linkText("Other link"));
        Assert.assertEquals(2, findElements.size());
    }

    @Test
    public void Element_findElementsByPartialLinkText() {
        WebElement links = driver.findElement(By.id("links"));
        List<WebElement> findElements = links.findElements(By.partialLinkText("Text"));
        Assert.assertEquals(2, findElements.size());
        findElements = links.findElements(By.partialLinkText("Other link"));
        Assert.assertEquals(2, findElements.size());
        findElements = links.findElements(By.partialLinkText("link"));
        Assert.assertEquals(4, findElements.size());
    }

    @Test
    public void findElementByName() {
        WebElement element = driver.findElement(By.name("text"));
        Assert.assertEquals("INPUT", element.getTagName());
    }

    @Test
    public void Element_findElementByName() {
        WebElement parent = driver.findElement(By.id("form"));
        WebElement element = parent.findElement(By.name("text"));
        Assert.assertEquals("INPUT", element.getTagName());
    }

    @Test
    public void findElementsByName() {
        List<WebElement> found = driver.findElements(By.name("text"));
        Assert.assertEquals(1, found.size());
    }

    @Test
    public void Element_findElementsByName() {
        WebElement parent = driver.findElement(By.id("form"));
        List<WebElement> found = parent.findElements(By.name("text"));
        Assert.assertEquals(1, found.size());     
    }

    @Test
    public void findElementByTagName() {
        WebElement element = driver.findElement(By.tagName("input"));
        Assert.assertEquals("INPUT", element.getTagName());
    }

    @Test
    public void Element_findElementByTagName() {
        WebElement parent = driver.findElement(By.id("form"));
        WebElement element = parent.findElement(By.tagName("input"));
        Assert.assertEquals("INPUT", element.getTagName());
    }

    @Test
    public void findElementsByTagName() {
        List<WebElement> found = driver.findElements(By.tagName("input"));
        Assert.assertEquals(2, found.size());
    }

    @Test
    public void Element_findElementsByTagName() {
        WebElement parent = driver.findElement(By.id("form"));
        List<WebElement> found = parent.findElements(By.tagName("input"));
        Assert.assertEquals(2, found.size());     
    }
    
    @AfterClass
    public static void close() {
        driver.close();
    }

}
