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
import java.awt.Robot;
import static java.awt.event.KeyEvent.*;
import java.io.Serializable;
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
import net.java.html.js.JavaScriptBody;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByXPath;

/**
 *
 * @author antonepple
 */
final class DukeScriptBrowser implements SearchContext, FindsById, FindsByXPath, FindsByCssSelector, FindsByLinkText {

    static Logger LOGGER = Logger.getLogger(DukeScriptBrowser.class.getName());
    private WebView view;
    private static Robot robot;
    private BrwsrCtx ctx;
    private Stage stage;

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
        robot = new Robot();
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
        return new DomNodeWebElement(findElementById_impl(using), ctx);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public WebElement findElementByXPath(final String using) {
        return new DomNodeWebElement(findElementByXPath_impl(using), ctx);
    }

    @Override
    public List<WebElement> findElementsByXPath(final String using) {
        return wrap(findElementsByXPath_impl(using));
    }

    @Override
    public List<WebElement> findElementsByCssSelector(final String using) {
        return wrap(findElementsByCSSSelector_impl(using));
    }

    @Override
    public WebElement findElementByCssSelector(final String using) {
        return new DomNodeWebElement(findElementByCSSSelector_impl(using), ctx);
    }

    @Override
    public WebElement findElementByLinkText(String using) {
        return new DomNodeWebElement(findElementByXPath_impl("//a[text()='"+using+"']"), ctx);
    }

    @Override
    public List<WebElement> findElementsByLinkText(String using) {
        return wrap(findElementsByXPath_impl("//a[text()='"+using+"']"));
    }

    @Override
    public WebElement findElementByPartialLinkText(String using) {
        return new DomNodeWebElement(findElementByXPath_impl("//a[contains(text(), '"+using+"')]"), ctx);
    }

    @Override
    public List<WebElement> findElementsByPartialLinkText(String using) {
        return wrap(findElementsByXPath_impl("//a[contains(text(), '"+using+"')]"));
    }

    private List<WebElement> wrap(Object[] findElementsByXPath_impl) {
        ArrayList<WebElement> arrayList = new ArrayList<>();
        for (Object object : findElementsByXPath_impl) {
            arrayList.add(new DomNodeWebElement(object, ctx));
        }
        return arrayList;
    }

    void setContext(BrwsrCtx ctx) {
        this.ctx = ctx;
    }

    @JavaScriptBody(args = {"using"}, body = "return document.evaluate(using, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;")
    static native Object findElementByXPath_impl(String using);

    @JavaScriptBody(args = {"using"}, body = "var xpr =  document.evaluate(using, document, null, XPathResult.UNORDERED_NODE_ITERATOR_TYPE, null);\n"
            + "var arr = [];\n"
            + "var current = xpr.iterateNext();\n"
            + "while (current){\n"
            + "    arr.push(current);\n"
            + "    current = xpr.iterateNext();\n"
            + "}"
            + "return arr;")
    static native Object[] findElementsByXPath_impl(String using);

    @JavaScriptBody(args = {"using"}, body = "return document.querySelector(using);")
    static native Object findElementByCSSSelector_impl(String using);

    @JavaScriptBody(args = {"using"}, body = "var nodeList = document.querySelectorAll(using);\n"
            + "console.log('nodeList '+nodeList);\n"
            + "var arr = [];\n"
            + "for (var i = 0; i < nodeList.length; i++) {\n"
            + "    console.log('node '+nodeList[i]);\n"
            + "    arr.push(nodeList[i]);\n"
            + "};"
            + "return arr;")
    static native Object[] findElementsByCSSSelector_impl(String using);

    @JavaScriptBody(args = {"id"}, body = "return document.getElementById(id);")
    static native Object findElementById_impl(String id);

    void close() {
        if (stage != null) {
            stage.close();
        }
    }

    String getTitle() {
        return view.getEngine().getTitle();
    }

    final static class DomNodeWebElement implements WebElement, Serializable {

        private final Object nativeElement;
        private final BrwsrCtx ctx;

        private DomNodeWebElement(Object nativeElement, BrwsrCtx ctx) {
            this.nativeElement = nativeElement;
            this.ctx = ctx;
        }

        @Override
        public void click() {
            try {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final String[] result = new String[1];
                ctx.execute(new Runnable() {
                    @Override
                    public void run() {
                        click_impl(nativeElement);
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void submit() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    submit_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void sendKeys(final CharSequence... keysToSend) {

            try {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final String[] result = new String[1];
                ctx.execute(new Runnable() {
                    @Override
                    public void run() {
                        focus_impl(nativeElement);
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
                for (int i = 0; i < keysToSend.length; i++) {
                    CharSequence cs = keysToSend[i];
                    for (int j = 0; j < cs.length(); j++) {
                        char c = cs.charAt(j);
                        type(c);
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void clear() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    clear_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public String getTagName() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final String[] result = new String[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = getTagName_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public String getAttribute(final String name) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final String[] result = new String[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = getAttribute_impl(nativeElement, name);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public boolean isSelected() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final boolean[] result = new boolean[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = isSelected_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

        @Override
        public boolean isEnabled() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final boolean[] result = new boolean[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = isEnabled_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

        @Override
        public String getText() {
            try {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final String[] result = new String[1];
                ctx.execute(new Runnable() {
                    @Override
                    public void run() {
                        result[0] = getText_impl(nativeElement);
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public List<WebElement> findElements(By by) {
            return by.findElements(this);
        }

        @Override
        public WebElement findElement(By by) {
            return by.findElement(this);
        }

        @Override
        public boolean isDisplayed() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final boolean[] result = new boolean[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = isDisplayed_impl(nativeElement);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        }

        @Override
        public Point getLocation() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final Point[] result = new Point[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = new Point(getLeft_impl(nativeElement), getTop_impl(nativeElement));
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public Dimension getSize() {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final Dimension[] result = new Dimension[1];
            ctx.execute(new Runnable() {
                @Override
                public void run() {
                    result[0] = new Dimension(getWidth_impl(nativeElement), getHeight_impl(nativeElement));
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public String getCssValue(final String propertyName) {
            try {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final String[] result = new String[1];
                ctx.execute(new Runnable() {
                    @Override
                    public void run() {
                        result[0] = getCssValue_impl(nativeElement, propertyName);
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
                return result[0];
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @JavaScriptBody(args = {"element", "name"}, body = "return window.getComputedStyle(element).getPropertyValue(name);")
        static native String getCssValue_impl(Object element, String name);

        @JavaScriptBody(args = {"element"}, body = "return element.offsetWidth;")
        static native int getWidth_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "return element.offsetHeight;")
        static native int getHeight_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "var rect = element.getBoundingClientRect();\n"
                + "return rect.top;")
        static native int getTop_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "var rect = element.getBoundingClientRect();\n"
                + "return rect.left;")
        static native int getLeft_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "var top = element.offsetTop;\n"
                + "  var left = element.offsetLeft;\n"
                + "  var width = element.offsetWidth;\n"
                + "  var height = element.offsetHeight;\n"
                + "\n"
                + "  while(element.offsetParent) {\n"
                + "    el = element.offsetParent;\n"
                + "    top += element.offsetTop;\n"
                + "    left += element.offsetLeft;\n"
                + "  }\n"
                + "\n"
                + "  return (\n"
                + "    top < (window.pageYOffset + window.innerHeight) &&\n"
                + "    left < (window.pageXOffset + window.innerWidth) &&\n"
                + "    (top + height) > window.pageYOffset &&\n"
                + "    (left + width) > window.pageXOffset\n"
                + "  );")
        static native boolean isDisplayed_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "return element.disabled;")
        static native boolean isEnabled_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "return element.selected || element.checked;")
        static native boolean isSelected_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "element.click()", wait4js = true)
        static native void click_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "var form = element;\n"
                + "if (element.form){\n"
                + "  form = element.form;\n"
                + "}\n"
                + "if (typeof form.submit === 'function'){\n"
                + "   form.submit();\n"
                + "}\n", wait4js = true)
        static native void submit_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "element.value=''", wait4js = true)
        static native void clear_impl(Object element);

        @JavaScriptBody(args = {"element", "name"}, body = "return element.getAttribute(name);")
        static native String getAttribute_impl(Object element, String name);

        @JavaScriptBody(args = {"element"}, body = "return element.tagName;")
        static native String getTagName_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "element.focus();", wait4js = true)
        static native void focus_impl(Object element);

        public void type(char character) {
            switch (character) {
                case 'a':
                    doType(VK_A);
                    break;
                case 'b':
                    doType(VK_B);
                    break;
                case 'c':
                    doType(VK_C);
                    break;
                case 'd':
                    doType(VK_D);
                    break;
                case 'e':
                    doType(VK_E);
                    break;
                case 'f':
                    doType(VK_F);
                    break;
                case 'g':
                    doType(VK_G);
                    break;
                case 'h':
                    doType(VK_H);
                    break;
                case 'i':
                    doType(VK_I);
                    break;
                case 'j':
                    doType(VK_J);
                    break;
                case 'k':
                    doType(VK_K);
                    break;
                case 'l':
                    doType(VK_L);
                    break;
                case 'm':
                    doType(VK_M);
                    break;
                case 'n':
                    doType(VK_N);
                    break;
                case 'o':
                    doType(VK_O);
                    break;
                case 'p':
                    doType(VK_P);
                    break;
                case 'q':
                    doType(VK_Q);
                    break;
                case 'r':
                    doType(VK_R);
                    break;
                case 's':
                    doType(VK_S);
                    break;
                case 't':
                    doType(VK_T);
                    break;
                case 'u':
                    doType(VK_U);
                    break;
                case 'v':
                    doType(VK_V);
                    break;
                case 'w':
                    doType(VK_W);
                    break;
                case 'x':
                    doType(VK_X);
                    break;
                case 'y':
                    doType(VK_Y);
                    break;
                case 'z':
                    doType(VK_Z);
                    break;
                case 'A':
                    doType(VK_SHIFT, VK_A);
                    break;
                case 'B':
                    doType(VK_SHIFT, VK_B);
                    break;
                case 'C':
                    doType(VK_SHIFT, VK_C);
                    break;
                case 'D':
                    doType(VK_SHIFT, VK_D);
                    break;
                case 'E':
                    doType(VK_SHIFT, VK_E);
                    break;
                case 'F':
                    doType(VK_SHIFT, VK_F);
                    break;
                case 'G':
                    doType(VK_SHIFT, VK_G);
                    break;
                case 'H':
                    doType(VK_SHIFT, VK_H);
                    break;
                case 'I':
                    doType(VK_SHIFT, VK_I);
                    break;
                case 'J':
                    doType(VK_SHIFT, VK_J);
                    break;
                case 'K':
                    doType(VK_SHIFT, VK_K);
                    break;
                case 'L':
                    doType(VK_SHIFT, VK_L);
                    break;
                case 'M':
                    doType(VK_SHIFT, VK_M);
                    break;
                case 'N':
                    doType(VK_SHIFT, VK_N);
                    break;
                case 'O':
                    doType(VK_SHIFT, VK_O);
                    break;
                case 'P':
                    doType(VK_SHIFT, VK_P);
                    break;
                case 'Q':
                    doType(VK_SHIFT, VK_Q);
                    break;
                case 'R':
                    doType(VK_SHIFT, VK_R);
                    break;
                case 'S':
                    doType(VK_SHIFT, VK_S);
                    break;
                case 'T':
                    doType(VK_SHIFT, VK_T);
                    break;
                case 'U':
                    doType(VK_SHIFT, VK_U);
                    break;
                case 'V':
                    doType(VK_SHIFT, VK_V);
                    break;
                case 'W':
                    doType(VK_SHIFT, VK_W);
                    break;
                case 'X':
                    doType(VK_SHIFT, VK_X);
                    break;
                case 'Y':
                    doType(VK_SHIFT, VK_Y);
                    break;
                case 'Z':
                    doType(VK_SHIFT, VK_Z);
                    break;
                case '`':
                    doType(VK_BACK_QUOTE);
                    break;
                case '0':
                    doType(VK_0);
                    break;
                case '1':
                    doType(VK_1);
                    break;
                case '2':
                    doType(VK_2);
                    break;
                case '3':
                    doType(VK_3);
                    break;
                case '4':
                    doType(VK_4);
                    break;
                case '5':
                    doType(VK_5);
                    break;
                case '6':
                    doType(VK_6);
                    break;
                case '7':
                    doType(VK_7);
                    break;
                case '8':
                    doType(VK_8);
                    break;
                case '9':
                    doType(VK_9);
                    break;
                case '-':
                    doType(VK_MINUS);
                    break;
                case '=':
                    doType(VK_EQUALS);
                    break;
                case '~':
                    doType(VK_SHIFT, VK_BACK_QUOTE);
                    break;
                case '!':
                    doType(VK_EXCLAMATION_MARK);
                    break;
                case '@':
                    doType(VK_AT);
                    break;
                case '#':
                    doType(VK_NUMBER_SIGN);
                    break;
                case '$':
                    doType(VK_DOLLAR);
                    break;
                case '%':
                    doType(VK_SHIFT, VK_5);
                    break;
                case '^':
                    doType(VK_CIRCUMFLEX);
                    break;
                case '&':
                    doType(VK_AMPERSAND);
                    break;
                case '*':
                    doType(VK_ASTERISK);
                    break;
                case '(':
                    doType(VK_LEFT_PARENTHESIS);
                    break;
                case ')':
                    doType(VK_RIGHT_PARENTHESIS);
                    break;
                case '_':
                    doType(VK_UNDERSCORE);
                    break;
                case '+':
                    doType(VK_PLUS);
                    break;
                case '\t':
                    doType(VK_TAB);
                    break;
                case '\n':
                    doType(VK_ENTER);
                    break;
                case '[':
                    doType(VK_OPEN_BRACKET);
                    break;
                case ']':
                    doType(VK_CLOSE_BRACKET);
                    break;
                case '\\':
                    doType(VK_BACK_SLASH);
                    break;
                case '{':
                    doType(VK_SHIFT, VK_OPEN_BRACKET);
                    break;
                case '}':
                    doType(VK_SHIFT, VK_CLOSE_BRACKET);
                    break;
                case '|':
                    doType(VK_SHIFT, VK_BACK_SLASH);
                    break;
                case ';':
                    doType(VK_SEMICOLON);
                    break;
                case ':':
                    doType(VK_COLON);
                    break;
                case '\'':
                    doType(VK_QUOTE);
                    break;
                case '"':
                    doType(VK_QUOTEDBL);
                    break;
                case ',':
                    doType(VK_COMMA);
                    break;
                case '<':
                    doType(VK_SHIFT, VK_COMMA);
                    break;
                case '.':
                    doType(VK_PERIOD);
                    break;
                case '>':
                    doType(VK_SHIFT, VK_PERIOD);
                    break;
                case '/':
                    doType(VK_SLASH);
                    break;
                case '?':
                    doType(VK_SHIFT, VK_SLASH);
                    break;
                case ' ':
                    doType(VK_SPACE);
                    break;
                default:
                    throw new IllegalArgumentException("Cannot type character " + character);
            }
        }

        private void doType(int... keyCodes) {
            doType(keyCodes, 0, keyCodes.length);
        }

        private void doType(int[] keyCodes, int offset, int length) {
            if (length == 0) {
                return;
            }

            robot.keyPress(keyCodes[offset]);
            doType(keyCodes, offset + 1, length - 1);
            robot.keyRelease(keyCodes[offset]);
        }

        @JavaScriptBody(args = {"element"}, body = "var text = element.innerHTML;\n"
                + "return text;")
        static native String getText_impl(Object element);

        @JavaScriptBody(args = {"element", "c"}, body = "var pressEvent = document.createEvent('KeyboardEvent');\n"
                + "          pressEvent.initKeyboardEvent('keypress', true, true, window, \n"
                + "                                    false, false, false, false, \n"
                + "                                    0, 30);\n"
                + "          element.dispatchEvent(pressEvent);"
                + "var releaseEvent = document.createEvent('KeyboardEvent');\n"
                + "releaseEvent.initKeyboardEvent('keyup', true, true, window, \n"
                + "                                    false, false, false, false, \n"
                + "                                    0, 30);\n"
                + "document.dispatchEvent(releaseEvent);")
        static native void type_impl(Object element, String c);

        @JavaScriptBody(args = {"element", "toString"}, body = "var vm = ko.dataFor(element);\n"
                + "vm.text(toString);")
        static native void setValue_impl(Object element, String toString);
    }
}
