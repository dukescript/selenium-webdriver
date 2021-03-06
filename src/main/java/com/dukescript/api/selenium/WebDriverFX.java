package com.dukescript.api.selenium;

/*
 * #%L
 * WebDriverFX - a file from the "selenium webdriver" project.
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
import com.dukescript.impl.selenium.DukeScriptBrowser;
import java.awt.AWTException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;
import net.java.html.BrwsrCtx;
import net.java.html.boot.fx.FXBrowsers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * A WebDriver you can use to test DukeScriptApplications.
 *
 * The WebDriver takes care of Threading and BrwsrCtx. It has some additional
 * methods helpful for initializing a DukeScript Model. You can load a model in
 * {@literal @}BeforeClass and use it for example like this:
 * <p></p>
 * {@codesnippet com.dukescript.api.selenium.FindsByTest}
 *
 * @author antonepple
 */
public final class WebDriverFX implements WebDriver, Executor {

    private final CountDownLatch init = new CountDownLatch(1);
    private DukeScriptBrowser dsBrowser;
    private BrwsrCtx ctx;

    /**
     * Use this constructor only if you need to initialize your webview.
     * Otherwise use {@link #WebDriverFX(URL, double, double)}
     *
     * @param view an existing webview
     * @throws Exception Might throw InterruptedException or AWTException (uses
     * Robot)
     */
    public WebDriverFX(WebView view) throws Exception {

        FXBrowsers.runInBrowser(view, new Runnable() {
            @Override
            public void run() {
                ctx = BrwsrCtx.findDefault(WebDriverFX.class);
                init.countDown();
            }
        });
        init.await();
        dsBrowser = new DukeScriptBrowser(view);
        dsBrowser.setContext(ctx);
    }

    /**
     * Constructor that allows you to set a size for the browser window.
     *
     * @param url The URL of the page to be loaded
     * @param width width of the {@link javafx.stage.Stage}
     * @param height height of the {@link javafx.stage.Stage}
     * @throws Exception Might throw InterruptedException or AWTException (uses
     * Robot)
     */
    public WebDriverFX(final URL url, final double width, final double height) throws Exception {
        JFXPanel jfxPanel = new JFXPanel(); // initializes toolkit
        final Runnable done = new Runnable() {
            @Override
            public void run() {
                ctx = BrwsrCtx.findDefault(WebDriverFX.class);
                dsBrowser.registerLogger();
                init.countDown();
            }
        };
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    dsBrowser = new DukeScriptBrowser(width, height);
                    FXBrowsers.load(dsBrowser.getView(), url, done);
                } catch (AWTException ex) {
                    Logger.getLogger(WebDriverFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        init.await();
        dsBrowser.setContext(ctx);
    }

    /**
     * Opens URL in a 800 * 600 Stage.
     *
     * @param url The URL of the page to be loaded
     * @throws Exception
     */
    public WebDriverFX(final URL url) throws Exception {
        this(url, 800, 600);
    }

    @Override
    public void get(String string) {
        throw new UnsupportedOperationException("Not implemented as not useful for DukeScript.");
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
    public List<WebElement> findElements(final By by) {
        List<WebElement> found = dsBrowser.findElements(by);
        return found;
    }

    @Override
    public WebElement findElement(final By by) {
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

    /**
     * Executes a Runnable in BrwsrCtx and waits for it to finish.
     *
     * @param command The command to execute
     * @throws InterruptedException (uses CountDownLatch)
     */
    public void executeAndWait(final Runnable command) throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ctx.execute(new Runnable() {
            @Override
            public void run() {
                command.run();
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
    }

    @Override
    public void execute(Runnable command) {
        ctx.execute(command);
    }
}
