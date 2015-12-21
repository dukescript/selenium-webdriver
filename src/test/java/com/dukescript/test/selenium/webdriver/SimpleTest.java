package com.dukescript.test.selenium.webdriver;

/*
 * #%L
 * SimpleTest - a file from the "selenium webdriver" project.
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

public class SimpleTest {

    private static WebDriverFX driver;
    private static TestModel testModel;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(SimpleTest.class.getResource("testWithModel.html"));
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
    public void findByCSSSelector() {
        WebElement element = driver.findElement(By.cssSelector(".css-selector"));
        Assert.assertEquals("blabla", element.getText());
        WebElement child = driver.findElement(By.cssSelector(".parent .child"));
        Assert.assertEquals("blubla", child.getText());
    }

    @Test
    public void ElementfindByCSSSelector() {
        WebElement parent = driver.findElement(By.cssSelector(".parent"));
        WebElement child = parent.findElement(By.cssSelector(".child"));
        Assert.assertEquals("blubla", child.getText());
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

    @AfterClass
    public static void close() {
        driver.close();
    }

}
