/*
 * #%L
 * DomNodeWebElement - a file from the "selenium webdriver" project.
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
package com.dukescript.test.selenium.webdriver;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import net.java.html.js.JavaScriptBody;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;

/**
 *
 * @author antonepple
 */
final class DomNodeWebElement implements WebElement, Serializable, FindsByCssSelector, FindsByClassName {

    private final Object nativeElement;
    private final BrwsrCtx ctx;
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(DomNodeWebElement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    DomNodeWebElement(Object nativeElement, BrwsrCtx ctx) {
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
                    result = by.findElements(DomNodeWebElement.this);
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
                    WebElement findElement = by.findElement(DomNodeWebElement.this);
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

    @JavaScriptBody(args = {"element"}, body = "var rect = element.getBoundingClientRect();\n" + "return rect.top;")
    static native int getTop_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "var rect = element.getBoundingClientRect();\n" + "return rect.left;")
    static native int getLeft_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "var top = element.offsetTop;\n" + "  var left = element.offsetLeft;\n" + "  var width = element.offsetWidth;\n" + "  var height = element.offsetHeight;\n" + "\n" + "  while(element.offsetParent) {\n" + "    el = element.offsetParent;\n" + "    top += element.offsetTop;\n" + "    left += element.offsetLeft;\n" + "  }\n" + "\n" + "  return (\n" + "    top < (window.pageYOffset + window.innerHeight) &&\n" + "    left < (window.pageXOffset + window.innerWidth) &&\n" + "    (top + height) > window.pageYOffset &&\n" + "    (left + width) > window.pageXOffset\n" + "  );")
    static native boolean isDisplayed_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "return element.disabled;")
    static native boolean isEnabled_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "return element.selected || element.checked;")
    static native boolean isSelected_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "element.click()", wait4js = true)
    static native void click_impl(Object element);

    @JavaScriptBody(args = {"element"}, body = "var form = element;\n" + "if (element.form){\n" + "  form = element.form;\n" + "}\n" + "if (typeof form.submit === 'function'){\n" + "   form.submit();\n" + "}\n", wait4js = true)
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
                doType(KeyEvent.VK_A);
                break;
            case 'b':
                doType(KeyEvent.VK_B);
                break;
            case 'c':
                doType(KeyEvent.VK_C);
                break;
            case 'd':
                doType(KeyEvent.VK_D);
                break;
            case 'e':
                doType(KeyEvent.VK_E);
                break;
            case 'f':
                doType(KeyEvent.VK_F);
                break;
            case 'g':
                doType(KeyEvent.VK_G);
                break;
            case 'h':
                doType(KeyEvent.VK_H);
                break;
            case 'i':
                doType(KeyEvent.VK_I);
                break;
            case 'j':
                doType(KeyEvent.VK_J);
                break;
            case 'k':
                doType(KeyEvent.VK_K);
                break;
            case 'l':
                doType(KeyEvent.VK_L);
                break;
            case 'm':
                doType(KeyEvent.VK_M);
                break;
            case 'n':
                doType(KeyEvent.VK_N);
                break;
            case 'o':
                doType(KeyEvent.VK_O);
                break;
            case 'p':
                doType(KeyEvent.VK_P);
                break;
            case 'q':
                doType(KeyEvent.VK_Q);
                break;
            case 'r':
                doType(KeyEvent.VK_R);
                break;
            case 's':
                doType(KeyEvent.VK_S);
                break;
            case 't':
                doType(KeyEvent.VK_T);
                break;
            case 'u':
                doType(KeyEvent.VK_U);
                break;
            case 'v':
                doType(KeyEvent.VK_V);
                break;
            case 'w':
                doType(KeyEvent.VK_W);
                break;
            case 'x':
                doType(KeyEvent.VK_X);
                break;
            case 'y':
                doType(KeyEvent.VK_Y);
                break;
            case 'z':
                doType(KeyEvent.VK_Z);
                break;
            case 'A':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_A);
                break;
            case 'B':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_B);
                break;
            case 'C':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_C);
                break;
            case 'D':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_D);
                break;
            case 'E':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_E);
                break;
            case 'F':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_F);
                break;
            case 'G':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_G);
                break;
            case 'H':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_H);
                break;
            case 'I':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_I);
                break;
            case 'J':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_J);
                break;
            case 'K':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_K);
                break;
            case 'L':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_L);
                break;
            case 'M':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_M);
                break;
            case 'N':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_N);
                break;
            case 'O':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_O);
                break;
            case 'P':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_P);
                break;
            case 'Q':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Q);
                break;
            case 'R':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_R);
                break;
            case 'S':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_S);
                break;
            case 'T':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_T);
                break;
            case 'U':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_U);
                break;
            case 'V':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_V);
                break;
            case 'W':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_W);
                break;
            case 'X':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_X);
                break;
            case 'Y':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Y);
                break;
            case 'Z':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_Z);
                break;
            case '`':
                doType(KeyEvent.VK_BACK_QUOTE);
                break;
            case '0':
                doType(KeyEvent.VK_0);
                break;
            case '1':
                doType(KeyEvent.VK_1);
                break;
            case '2':
                doType(KeyEvent.VK_2);
                break;
            case '3':
                doType(KeyEvent.VK_3);
                break;
            case '4':
                doType(KeyEvent.VK_4);
                break;
            case '5':
                doType(KeyEvent.VK_5);
                break;
            case '6':
                doType(KeyEvent.VK_6);
                break;
            case '7':
                doType(KeyEvent.VK_7);
                break;
            case '8':
                doType(KeyEvent.VK_8);
                break;
            case '9':
                doType(KeyEvent.VK_9);
                break;
            case '-':
                doType(KeyEvent.VK_MINUS);
                break;
            case '=':
                doType(KeyEvent.VK_EQUALS);
                break;
            case '~':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_QUOTE);
                break;
            case '!':
                doType(KeyEvent.VK_EXCLAMATION_MARK);
                break;
            case '@':
                doType(KeyEvent.VK_AT);
                break;
            case '#':
                doType(KeyEvent.VK_NUMBER_SIGN);
                break;
            case '$':
                doType(KeyEvent.VK_DOLLAR);
                break;
            case '%':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_5);
                break;
            case '^':
                doType(KeyEvent.VK_CIRCUMFLEX);
                break;
            case '&':
                doType(KeyEvent.VK_AMPERSAND);
                break;
            case '*':
                doType(KeyEvent.VK_ASTERISK);
                break;
            case '(':
                doType(KeyEvent.VK_LEFT_PARENTHESIS);
                break;
            case ')':
                doType(KeyEvent.VK_RIGHT_PARENTHESIS);
                break;
            case '_':
                doType(KeyEvent.VK_UNDERSCORE);
                break;
            case '+':
                doType(KeyEvent.VK_PLUS);
                break;
            case '\t':
                doType(KeyEvent.VK_TAB);
                break;
            case '\n':
                doType(KeyEvent.VK_ENTER);
                break;
            case '[':
                doType(KeyEvent.VK_OPEN_BRACKET);
                break;
            case ']':
                doType(KeyEvent.VK_CLOSE_BRACKET);
                break;
            case '\\':
                doType(KeyEvent.VK_BACK_SLASH);
                break;
            case '{':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET);
                break;
            case '}':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET);
                break;
            case '|':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH);
                break;
            case ';':
                doType(KeyEvent.VK_SEMICOLON);
                break;
            case ':':
                doType(KeyEvent.VK_COLON);
                break;
            case '\'':
                doType(KeyEvent.VK_QUOTE);
                break;
            case '"':
                doType(KeyEvent.VK_QUOTEDBL);
                break;
            case ',':
                doType(KeyEvent.VK_COMMA);
                break;
            case '<':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA);
                break;
            case '.':
                doType(KeyEvent.VK_PERIOD);
                break;
            case '>':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD);
                break;
            case '/':
                doType(KeyEvent.VK_SLASH);
                break;
            case '?':
                doType(KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH);
                break;
            case ' ':
                doType(KeyEvent.VK_SPACE);
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

    @JavaScriptBody(args = {"element"}, body = "var text = element.innerHTML;\n" + "return text;")
    static native String getText_impl(Object element);

    @JavaScriptBody(args = {"element", "c"}, body = "var pressEvent = document.createEvent('KeyboardEvent');\n" + "          pressEvent.initKeyboardEvent('keypress', true, true, window, \n" + "                                    false, false, false, false, \n" + "                                    0, 30);\n" + "          element.dispatchEvent(pressEvent);" + "var releaseEvent = document.createEvent('KeyboardEvent');\n" + "releaseEvent.initKeyboardEvent('keyup', true, true, window, \n" + "                                    false, false, false, false, \n" + "                                    0, 30);\n" + "document.dispatchEvent(releaseEvent);")
    static native void type_impl(Object element, String c);

    @JavaScriptBody(args = {"element", "toString"}, body = "var vm = ko.dataFor(element);\n" + "vm.text(toString);")
    static native void setValue_impl(Object element, String toString);

    @Override
    public WebElement findElementByCssSelector(String using) {
        return new DomNodeWebElement(Finder.findElementByCSSSelector_impl(nativeElement, using), ctx);
    }

    @Override
    public List<WebElement> findElementsByCssSelector(String using) {
        return Finder.wrap(Finder.findElementsByCSSSelector_impl(nativeElement, using), ctx);
    }

    @Override
    public WebElement findElementByClassName(String using) {
        return new DomNodeWebElement(Finder.findElementByClassName_impl(nativeElement, using), ctx);
    }

    @Override
    public List<WebElement> findElementsByClassName(String using) {
        return Finder.wrap(Finder.findElementsByClassName_impl(nativeElement, using), ctx);
    }

}
