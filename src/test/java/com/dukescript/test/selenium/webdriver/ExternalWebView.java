package com.dukescript.test.selenium.webdriver;

/*
 * #%L
 * ExternalWebView - a file from the "selenium webdriver" project.
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
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.java.html.boot.fx.FXBrowsers;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExternalWebView {

    private static JFXPanel jfxPanel = new JFXPanel();
    private static WebDriverFX driver;
    private static TestModel testModel;

    @BeforeClass
    public static void test() throws InterruptedException, Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final Runnable done = new Runnable() {
            @Override
            public void run() {
                testModel = new TestModel("Hello", "World");
                testModel.applyBindings();
                countDownLatch.countDown();
            }
        };
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    final WebView webView = new WebView();
                    Scene scene = new Scene(webView);
                    stage.setScene(scene);
                    stage.show();
                    FXBrowsers.load(webView, ExternalWebView.class.getResource("testWithModel.html"), done);
                    driver = new WebDriverFX(webView);
                } catch (Exception ex) {
                    Logger.getLogger(ExternalWebView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        countDownLatch.await();
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

    @AfterClass
    public static void close() {
        driver.close();
    }

}
