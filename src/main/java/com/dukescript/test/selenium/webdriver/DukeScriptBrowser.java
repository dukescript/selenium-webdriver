package com.dukescript.test.selenium.webdriver;

/*
 * #%L
 * DukeScriptScriptBrowser - a file from the "selenium webdriver" project.
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
import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.java.html.BrwsrCtx;
import net.java.html.boot.fx.FXBrowsers;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.Locatable;

/**
 *
 * @author antonepple
 */
final class DukeScriptBrowser implements SearchContext, FindsById, FindsByXPath, FindsByCssSelector, FindsByLinkText, JavascriptExecutor, FindsByClassName,
        FindsByName, FindsByTagName, Locatable {

    static Logger LOGGER = Logger.getLogger(DukeScriptBrowser.class.getName());

    private WebView view;
    private BrwsrCtx ctx;
    private Stage stage;
    private Object document;

    DukeScriptBrowser(double width, double height) throws AWTException {
        this(new WebView());
        start(width, height);
    }

    /**
     * Manages existing WebView
     *
     * @param view
     * @throws AWTException
     */
    DukeScriptBrowser(WebView view) throws AWTException {
        this.view = view;
    }

    public void start(double width, double height) {
        this.stage = new Stage();
        WebEngine engine = view.getEngine();
        ConsoleLogger.register(engine);
        engine.titleProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                stage.setTitle(newValue);
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(view);
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.show();
    }

    public WebView getView() {
        return view;
    }

    String getPageSource() {
        return view.getEngine().getLocation();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            WebDriverFX.RunVal<List<WebElement>> runVal = new WebDriverFX.RunVal<List<WebElement>>() {
                List<WebElement> result;

                @Override
                public List<WebElement> get() {
                    return result;
                }

                @Override
                public void run() {
                    result = by.findElements(DukeScriptBrowser.this);
                    countDownLatch.countDown();
                }
            };
            ctx.execute(runVal);
            countDownLatch.await();
            return runVal.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(WebDriverFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public WebElement findElement(final By by) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final WebElement[] result = new WebElement[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    WebElement findElement = by.findElement(DukeScriptBrowser.this);
                    result[0] = findElement;
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
            return result[0];
        } catch (InterruptedException ex) {
            Logger.getLogger(WebDriverFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public WebElement findElementById(String using) {
        return new DomNodeWebElement(Finder.findElementByCSSSelector_impl(document, "#" + using), ctx);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return Finder.wrap(Finder.findElementsByCSSSelector_impl(document, "#"+using), ctx);
    }

    @Override
    public WebElement findElementByXPath(final String using) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(document, using), ctx);
    }

    @Override
    public List<WebElement> findElementsByXPath(final String using) {
        return wrap(Finder.findElementsByXPath_impl(document, using));
    }

    @Override
    public List<WebElement> findElementsByCssSelector(final String using) {
        return wrap(Finder.findElementsByCSSSelector_impl(document, using));
    }

    @Override
    public WebElement findElementByCssSelector(final String using) {
        return new DomNodeWebElement(Finder.findElementByCSSSelector_impl(document, using), ctx);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(document, "//a[text()='" + using + "']"), ctx);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return wrap(Finder.findElementsByXPath_impl(document, "//a[text()='" + using + "']"));
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return new DomNodeWebElement(Finder.findElementByXPath_impl(document, "//a[contains(text(), '" + using + "')]"), ctx);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return wrap(Finder.findElementsByXPath_impl(document, "//a[contains(text(), '" + using + "')]"));
    }

    void close() {
        if (stage != null) {
            stage.close();
        }
    }

    String getTitle() {
        return view.getEngine().getTitle();
    }

    @Override
    public Object executeScript(String script, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object executeAsyncScript(String script, Object... args) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return new DomNodeWebElement(Finder.findElementByClassName_impl(document, using), ctx);
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return wrap(Finder.findElementsByClassName_impl(document, using));
    }

    @Override
    public WebElement findElementByName(String using) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<WebElement> findElementsByName(String using) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WebElement findElementByTagName(String using) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<WebElement> findElementsByTagName(String using) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Coordinates getCoordinates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<WebElement> wrap(Object[] findElementsByXPath_impl) {
        ArrayList<WebElement> arrayList = new ArrayList<>();
        for (Object object : findElementsByXPath_impl) {
            arrayList.add(new DomNodeWebElement(object, ctx));
        }
        return arrayList;
    }

    void setContext(BrwsrCtx ctx) {
        try {
            this.ctx = ctx;
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            FXBrowsers.runInBrowser(view, new Runnable() {
                @Override
                public void run() {
                    document = Finder.getDocument();
                    countDownLatch.countDown();
                }
            });
            countDownLatch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
