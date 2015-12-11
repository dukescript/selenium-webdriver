package com.dukescript.test.selenium.webdriver;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.java.html.boot.fx.FXBrowsers;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

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
                dsBrowser = new DukeScriptBrowser(500, 200);
              
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
        return new Options() {
            @Override
            public void addCookie(Cookie cookie) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void deleteCookieNamed(String name) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void deleteCookie(Cookie cookie) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void deleteAllCookies() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Set<Cookie> getCookies() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Cookie getCookieNamed(String name) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Timeouts timeouts() {
                AtomicLong implicit = new AtomicLong();
                AtomicLong load = new AtomicLong();
                AtomicLong script = new AtomicLong();
                return new Timeouts() {
                    @Override
                    public Timeouts implicitlyWait(long time, TimeUnit unit) {
                        implicit.set(unit.convert(time, TimeUnit.MILLISECONDS));
                        return this;
                    }

                    @Override
                    public Timeouts setScriptTimeout(long time, TimeUnit unit) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public Timeouts pageLoadTimeout(long time, TimeUnit unit) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                };
            }

            @Override

            public ImeHandler ime() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Window window() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Logs logs() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

}
