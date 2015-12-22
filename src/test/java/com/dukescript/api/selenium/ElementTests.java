/*
 * #%L
 * ElementTests - a file from the "selenium webdriver" project.
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
package com.dukescript.api.selenium;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 *
 * @author antonepple
 */
public class ElementTests {

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
    public void withModel() throws InterruptedException {
        WebElement element = driver.findElement(By.id("input"));
        element.clear();
        String value = element.getAttribute("value");
        element.sendKeys("Hallo");
        Thread.sleep(5000);
        value = element.getAttribute("value");
        Assert.assertEquals(value, "Hallo");
    }

    @AfterClass
    public static void close() {
        driver.close();
    }

}
