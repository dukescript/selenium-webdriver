package com.dukescript.test.selenium.webdriver;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

//@RunWith(WebDriverFXRunner.class)
public class SimpleTest {

    private static WebDriverFX driver;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(SimpleTest.class.getResource("testWithModel.html"));
        driver.executeAndWait(new Runnable() {
            @Override
            public void run() {
                TestModel testModel = new TestModel("Hello", "World");
                testModel.applyBindings();
            }
        });
    }

    @Test
    public void withModel() {
        WebElement element = driver.findElement(By.id("target"));
        Assert.assertEquals("Hello", element.getText());
        WebElement button = driver.findElement(By.id("button"));
        button.click();
        Assert.assertEquals("World", element.getText());
        WebElement input = driver.findElement(By.id("input"));
        input.clear();
        input.sendKeys("DukeScript");
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        button.click();
        Assert.assertEquals("DukeScript", element.getText());
    }

    public static void withModel1() {
  
    }

    public static void main(String... args) {
        try {
            driver = new WebDriverFX(SimpleTest.class.getResource("testWithModel.html"), SimpleTest.class, "withModel1");
            driver.executeAndWait(new Runnable() {
                @Override
                public void run() {
                    TestModel testModel = new TestModel("Hello", "World");
                    testModel.applyBindings();
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
