package com.dukescript.test.selenium.webdriver;

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
        driver = new WebDriverFX(SimpleTest.class.getResource("index.html"));
    }

    @Test
    public void clickChangesTheMessage() {
        String message = "DukeScript";
        WebElement element = driver.findElement(By.id("input"));
        element.sendKeys(message);
        WebElement button = driver.findElement(By.id("button"));
        button.click();
        WebElement target = driver.findElement(By.id("target"));                 
        assert target.getText().equals(message); 
    }
}
