/**
 * Copyright 2009-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dukescript.test.selenium.webdriver;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SimpleTest {

    private static BrwsrCtx bc;

//    @Test
    public static void test() throws InterruptedException, Exception {
// Find the text input element by its name
        driver = new WebDriverFX(SimpleTest.class.getResource("index.html"), SimpleTest.class, "start");
    }
    private static WebDriver driver;

    public static void main(String... args) {
        try {
            SimpleTest.test();
        } catch (Exception ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void start() {
       
        bc = BrwsrCtx.findDefault(SimpleTest.class);
        bc.execute(new Runnable() {
            @Override
            public void run() {
                WebElement element = driver.findElement(By.id("input"));
                // Enter something to search for
                element.sendKeys("Cheese!");

                WebElement button = driver.findElement(By.id("button"));
                // Now submit the form. WebDriver will find the form for us from the element
                button.click();
                WebElement target = driver.findElement(By.id("target"));
                Assert.assertEquals("Hallo", target.getText());
            }
        });
    }
}
