package com.dukescript.test.selenium.webdriver;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SimpleTest {

    private static WebDriver driver;
    private static BrwsrCtx bc;

    public static void test() throws InterruptedException, Exception {
        driver = new WebDriverFX(SimpleTest.class.getResource("index.html"), SimpleTest.class, "start");
    }

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
                    String message = "DukeScript";
                    WebElement element = driver.findElement(By.id("input"));
                    element.sendKeys(message);
                    WebElement button = driver.findElement(By.id("button"));
                    button.click();
                    WebElement target = driver.findElement(By.id("target"));                 
                    Assert.assertEquals(target.getText(), message);  
            }
        });
    }
}
