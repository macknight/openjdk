/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.nashorn.internal.runtime;

import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

/**
 * Wrapper class for Logging system. This is how you are supposed to register a logger and use it
 */

public class DebugLogger {
    @SuppressWarnings("NonConstantLogger")
    private final Logger  logger;
    private final boolean isEnabled;

    private int indent;

    /**
     * Constructor
     *
     * @param loggerName name of logger - this is the unique key with which it can be identified
     */
    public DebugLogger(final String loggerName) {
        this(loggerName, null);
    }

    /**
     * Constructor
     *
     * A logger can be paired with a property, e.g. {@code --log:codegen:info} is equivalent to {@code -Dnashorn.codegen.log}
     *
     * @param loggerName name of logger - this is the unique key with which it can be identified
     * @param property   system property activating the logger on {@code info} level
     */
    public DebugLogger(final String loggerName, final String property) {
        this.logger    = Logging.getLogger(loggerName);
        this.isEnabled = logger.getLevel() != Level.OFF || (property != null && Options.getBooleanProperty(property));
    }

    /**
     * Get the output writer for the logger. Loggers always default to
     * stderr for output as they are used mainly to output debug info
     *
     * @return print writer for log output.
     */
    public PrintWriter getOutputStream() {
        return Context.getContext().getErr();
    }

    /**
     * Check if the logger is enabled
     * @return true if enabled
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * If you want to change the indent level of your logger, call indent with a new position.
     * Positions start at 0 and are increased by one for a new "tab"
     *
     * @param pos indent position
     */
    public void indent(final int pos) {
        if (isEnabled) {
           indent += pos * 4;
        }
    }

    /**
     * Check if the logger is above of the level of detail given
     * @see java.util.logging.Level
     *
     * @param level logging level
     * @return true if level is above the given one
     */
    public boolean levelAbove(final Level level) {
        return logger.getLevel().intValue() > level.intValue();
    }

    /**
     * Shorthand for outputting a log string as log level {@link java.util.logging.Level#FINEST} on this logger
     * @param str the string to log
     */
    public void finest(final String str) {
        log(str, Level.FINEST);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#FINER} on this logger
     * @param str the string to log
     */
    public void finer(final String str) {
        log(str, Level.FINER);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#FINE} on this logger
     * @param str the string to log
     */
    public void fine(final String str) {
        log(str, Level.FINE);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#CONFIG} on this logger
     * @param str the string to log
     */
    public void config(final String str) {
        log(str, Level.CONFIG);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#INFO} on this logger
     * @param str the string to log
     */
    public void info(final String str) {
        log(str, Level.INFO);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#WARNING} on this logger
     * @param str the string to log
     */
    public void warning(final String str) {
        log(str, Level.WARNING);
    }

    /**
     * Shorthand for outputting a log string as log level
     * {@link java.util.logging.Level#SEVERE} on this logger
     * @param str the string to log
     */
    public void severe(final String str) {
        log(str, Level.SEVERE);
    }

    /**
     * Output log line on this logger at a given level of verbosity
     * @see java.util.logging.Level
     *
     * @param str   string to log
     * @param level minimum log level required for logging to take place
     */
    public void log(final String str, final Level level) {
        if (isEnabled) {
            final StringBuilder sb = new StringBuilder();

            for (int i = 0 ; i < indent ; i++) {
                sb.append(' ');
            }
            sb.append(str);
            logger.log(level, sb.toString());
        }
    }
}
