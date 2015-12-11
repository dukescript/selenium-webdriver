/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.test.selenium.webdriver;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.java.html.BrwsrCtx;
import net.java.html.boot.fx.FXBrowsers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A WebDriver you can use to test DukeScriptApplications
 *
 * @author antonepple
 */
public class WebDriverFX implements WebDriver {

    private DukeScriptBrowser dsBrowser;

    
    public WebDriverFX(URL url, Class onPageLoad, String methodName, String... args) throws Exception {
        JFXPanel jfxPanel = new JFXPanel(); // initializes toolkit
        Platform.runLater(
                new Runnable() {
            @Override
            public void run() {
                dsBrowser = new DukeScriptBrowser();
                dsBrowser.start(1000, 800);
                FXBrowsers.load(dsBrowser.getView(), url, onPageLoad, methodName, args);
            }
        }
        );
    }

    @Override
    public void get(String string) {
        throw new UnsupportedOperationException("Not implemented as not useful for DukeScript. Use load() instead!");
    }

    @Override
    public String getCurrentUrl() {
        return dsBrowser.getView().getEngine().getLocation();
    }

    @Override
    public String getTitle() {
        return dsBrowser.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return dsBrowser.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        
        return dsBrowser.findElement(by);
    }

    @Override
    public String getPageSource() {
        return dsBrowser.getPageSource();
    }

    @Override
    public void close() {
        // test are single page => close == quit
        quit();
    }

    @Override
    public void quit() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dsBrowser.close();
            }
        });
    }

    @Override
    public Set<String> getWindowHandles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getWindowHandle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TargetLocator switchTo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Navigation navigate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Options manage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
