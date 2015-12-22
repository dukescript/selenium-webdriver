package com.dukescript.impl.selenium;

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
import net.java.html.js.JavaScriptBody;

 final class ConsoleLogger {

    static final Logger LOGGER = Logger.getLogger(ConsoleLogger.class.getName());

    private ConsoleLogger() {
    }

    static void register(WebEngine eng) {
        ConsoleLogger c = new ConsoleLogger();
        register_impl("log", Level.INFO, c);
        register_impl("info", Level.INFO, c);
        register_impl("warn", Level.WARNING, c);
        register_impl("error", Level.SEVERE, c);
    }

    @JavaScriptBody(args = {"attr", "l", "c"}, body = ""
            + "window.console[attr] = function(msg) {"
            + "     c.@com.dukescript.impl.selenium.ConsoleLogger::log(Ljava/util/logging/Level;Ljava/lang/String;)(l, msg);"
            + "};"
          , javacall = true)
    private native static void register_impl(String attr, Level l, ConsoleLogger c);

    public void log(Level l, String msg) {
        LOGGER.log(l, msg);
    }

}
