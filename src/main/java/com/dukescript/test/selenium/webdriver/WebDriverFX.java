package com.dukescript.test.selenium.webdriver;

import java.awt.AWTException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import net.java.html.BrwsrCtx;
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
public final class WebDriverFX implements WebDriver, Executor {

    private final CountDownLatch init = new CountDownLatch(1);
    private DukeScriptBrowser dsBrowser;
    private BrwsrCtx ctx;

    public WebDriverFX(final URL url) throws Exception {
        JFXPanel jfxPanel = new JFXPanel(); // initializes toolkit
        final Runnable done = new Runnable() {
            @Override
            public void run() {
                ctx = BrwsrCtx.findDefault(WebDriverFX.class);
                init.countDown();
            }
        };
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    dsBrowser = new DukeScriptBrowser(500, 200);

                    FXBrowsers.load(dsBrowser.getView(), url, done);
                } catch (AWTException ex) {
                    Logger.getLogger(WebDriverFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        init.await();
        dsBrowser.setContext(ctx);
    }

    public WebDriverFX(final URL url, final Class klass, final String method
    ) throws Exception {
        JFXPanel jfxPanel = new JFXPanel(); // initializes toolkit
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    dsBrowser = new DukeScriptBrowser(500, 200);
                    FXBrowsers.load(dsBrowser.getView(), url, klass, method);
                } catch (AWTException ex) {
                    Logger.getLogger(WebDriverFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
    public List<WebElement> findElements(final By by) {
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            RunVal<List<WebElement>> runVal = new RunVal<List<WebElement>>() {
                List<WebElement> result;

                @Override
                public List<WebElement> get() {
                    return result;
                }

                @Override
                public void run() {
                    result = findElements(by);
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
                    WebElement findElement = dsBrowser.findElement(by);
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
                final AtomicLong implicit = new AtomicLong();
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

    public static interface RunVal<T> extends Runnable {

        public T get();
    }
}
