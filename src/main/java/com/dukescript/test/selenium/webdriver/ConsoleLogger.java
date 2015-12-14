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

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public final class ConsoleLogger {
    static final Logger LOGGER = Logger.getLogger(ConsoleLogger.class.getName());
    
    private ConsoleLogger() {
    }

    static void register(WebEngine eng) {
        JSObject fn = (JSObject) eng.executeScript(""
            + "(function(attr, l, c) {"
            + "  window.console[attr] = function(msg) { c.log(l, msg); };"
            + "})"
        );
        ConsoleLogger c = new ConsoleLogger();
        c.register_impl(fn, "log", Level.INFO);
        c.register_impl(fn, "info", Level.INFO);
        c.register_impl(fn, "warn", Level.WARNING);
        c.register_impl(fn, "error", Level.SEVERE);
    }
    
    private void register_impl(JSObject eng, String attr, Level l) {
        eng.call("call", null, attr, l, this);
    }
    
    public void log(Level l, String msg) {
        LOGGER.log(l, msg);
    }
}
