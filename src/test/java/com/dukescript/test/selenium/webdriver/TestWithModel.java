/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.test.selenium.webdriver;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author antonepple
 */
public class TestWithModel {

    private static WebDriver driver;
    private static BrwsrCtx bc;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        System.out.println("doing it");
        driver = new WebDriverFX(TestWithModel.class.getResource("testWithModel.html"), TestWithModel.class, "start");
    }



    @Test
    public void testStndKeys() {
        String message = "DukeScript";
        WebElement element = driver.findElement(By.id("input"));
        element.sendKeys(message); // doesn't work with bindings, because currently implemented as element.value=...  
        
//        WebElement button = driver.findElement(By.id("button"));
//        System.out.println("button " + button);
//        button.click();
    }

    public static void start() {
        bc = BrwsrCtx.findDefault(SimpleTest.class);
        bc.execute(new Runnable() {
            @Override
            public void run() {
                TestModel testModel = new TestModel("Hallo", "Welt!");
                testModel.applyBindings();
//                WebElement target = driver.findElement(By.id("target"));
//                Assert.assertEquals(target.getText(), message);

            }
        });
    }
}
