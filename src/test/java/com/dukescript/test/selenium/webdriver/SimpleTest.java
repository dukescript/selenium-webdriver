package com.dukescript.test.selenium.webdriver;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
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
    }
    
}
