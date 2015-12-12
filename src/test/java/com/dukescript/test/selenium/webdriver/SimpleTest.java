package com.dukescript.test.selenium.webdriver;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(WebDriverFXRunner.class)
public class SimpleTest {
    private static WebDriver driver;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(SimpleTest.class.getResource("testWithModel.html"));
    }
    
    @Test
    public void withModel() {
        TestModel testModel = new TestModel("Hello", "World");
        testModel.applyBindings();
        WebElement element = driver.findElement(By.id("target"));
        Assert.assertEquals("Hello",element.getText());
        WebElement button = driver.findElement(By.id("button"));
        button.click();
        Assert.assertEquals("World",element.getText());
    }
}
