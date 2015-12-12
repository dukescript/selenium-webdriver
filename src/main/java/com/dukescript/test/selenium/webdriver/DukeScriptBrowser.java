package com.dukescript.test.selenium.webdriver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.KeyStroke;
import net.java.html.BrwsrCtx;
import net.java.html.js.JavaScriptBody;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByXPath;

/**
 *
 * @author antonepple
 */
final class DukeScriptBrowser extends Stage implements SearchContext, FindsById, FindsByXPath {

    static Logger LOGGER = Logger.getLogger(DukeScriptBrowser.class.getName());
    private WebView view = new WebView();
    private static Robot robot;
    private BrwsrCtx ctx;

    DukeScriptBrowser(int width, int height) throws AWTException {
        start(width, height);
        robot = new Robot();
    }

    public void start(double width, double height) {

        WebEngine engine = view.getEngine();
        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                LOGGER.info("Browser State change " + newValue.name());
            }
        }
        );
        engine.titleProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                DukeScriptBrowser.this.setTitle(newValue);
            }
        });
        StackPane root = new StackPane();
        root.getChildren().add(view);
        Scene scene = new Scene(root, width, height);
        setScene(scene);
        show();

    }

    public WebView getView() {
        return view;
    }

    String getPageSource() {
        return view.getEngine().getLocation();
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
    public WebElement findElementById(String using) {
        return new DomNodeWebElement(findElementById_impl(using), ctx);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public WebElement findElementByXPath(String using) {
        return new DomNodeWebElement(findElementByXPath_impl(using), ctx);
    }

    @Override
    public List<WebElement> findElementsByXPath(String using) {
        return wrap(findElementsByXPath_impl(using));
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

    @JavaScriptBody(args = {"id"}, body = "return document.getElementById(id);")
    static native Object findElementById_impl(String id);

    private List<WebElement> wrap(Object[] findElementsByXPath_impl) {
        ArrayList<WebElement> arrayList = new ArrayList<WebElement>();
        for (Object object : findElementsByXPath_impl) {
            arrayList.add(new DomNodeWebElement(object, ctx));
        }
        return arrayList;
    }

    void setContext(BrwsrCtx ctx) {
        this.ctx = ctx;
    }

    final static class DomNodeWebElement implements WebElement {

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
            System.out.println("submit");
            submit_impl(nativeElement);
        }

        @Override
        public void sendKeys(final CharSequence... keysToSend) {
            // THIS ALSO DOESN'T UPDATE THE INPUT
//            for (CharSequence charSequence : keysToSend) {
//                for (int i = 0; i < charSequence.length(); i++) {
//                    System.out.println("sendKey "+charSequence.charAt(i));
//                    type_impl(nativeElement, String.valueOf(charSequence.charAt(i)));
//                }
//            }

            // setValue doesn't work with data-bind textInput
//            String res = "";
//            for (CharSequence charSequence : keysToSend) {
//                res += charSequence.toString();
//            }
//            setValue_impl(nativeElement, res);
            //  Robot implementation is not synchronous
            try {
                final CountDownLatch countDownLatch = new CountDownLatch(1);
                final String[] result = new String[1];
                ctx.execute(new Runnable() {
                    @Override
                    public void run() {
                        focus_impl(nativeElement);
                        for (int i = 0; i < keysToSend.length; i++) {
                            CharSequence cs = keysToSend[i];
                            for (int j = 0; j < cs.length(); j++) {
                                char c = cs.charAt(j);
                                type(c);
                            }
                        }
                        countDownLatch.countDown();
                    }
                });
                countDownLatch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(DukeScriptBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void clear() {
            clear_impl(nativeElement);
        }

        @Override
        public String getTagName() {
            return getTagName_impl(nativeElement);
        }

        @Override
        public String getAttribute(String name) {
            return getAttribute_impl(nativeElement, name);
        }

        @Override
        public boolean isSelected() {
            return isSelected_impl(nativeElement);
        }

        @Override
        public boolean isEnabled() {
            return isEnabled_impl(nativeElement);
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
            return isDisplayed_impl(nativeElement);
        }

        @Override
        public Point getLocation() {
            return new Point(getLeft_impl(nativeElement), getTop_impl(nativeElement));
        }

        @Override
        public Dimension getSize() {
            return new Dimension(getWidth_impl(nativeElement), getHeight_impl(nativeElement));
        }

        @Override
        public String getCssValue(String propertyName) {
            return getCssValue_impl(nativeElement, propertyName);
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

//        @JavaScriptBody(args = {"target", "c"}, body = "var evt = document.createEvent('KeyboardEvent');\n"
//                + "evt.initKeyboardEvent('keypress', true, true, null, false, false, false, false, c.charCodeAt(0), 0);"
//                + "target.dispatchEvent(evt);")
//        static native void sendKey_impl(Object target, String c);
        @JavaScriptBody(args = {"element", "name"}, body = "return element.getAttribute(name);")
        static native String getAttribute_impl(Object element, String name);

        @JavaScriptBody(args = {"element"}, body = "return element.tagName;")
        static native String getTagName_impl(Object element);

        @JavaScriptBody(args = {"element"}, body = "element.focus();", wait4js = true)
        static native void focus_impl(Object element);

        void type(char c) {

            KeyStroke key = KeyStroke.getKeyStroke("pressed " + Character.toUpperCase(c));
            if (null != key) {
                if (Character.isUpperCase(c)) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }

                robot.keyPress(key.getKeyCode());
                robot.keyRelease(key.getKeyCode());

                if (Character.isUpperCase(c)) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
            }
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
                + "element.dispatchEvent(releaseEvent);")
        static native void type_impl(Object element, String c);

        @JavaScriptBody(args = {"element", "toString"}, body = "element.value = toString;")
        static native void setValue_impl(Object element, String toString);
    }
}
