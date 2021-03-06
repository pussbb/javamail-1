/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.mail.util;

import java.io.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test handling of line terminators.
 * LineInputStream handles these different line terminators:
 *
 *	NL		- Unix
 *	CR LF		- Windows, MIME
 *	CR		- old MacOS
 *	CR CR LF	- broken internet servers
 *
 * @author Bill Shannon
 */

public class LineInputStreamTest {
    private static final String[] lines = {
	"line1\nline2\nline3\n",
	"line1\r\nline2\r\nline3\r\n",
	"line1\rline2\rline3\r",
	"line1\r\r\nline2\r\r\nline3\r\r\n"
    };

    private static final String[] empty = {
	"\n\n\n",
	"\r\n\r\n\r\n",
	"\r\r\r",
	"\r\r\n\r\r\n\r\r\n"
    };

    private static final String[] mixed = {
	"line1\n\nline3\n",
	"line1\r\n\r\nline3\r\n",
	"line1\r\rline3\r",
	"line1\r\r\n\r\r\nline3\r\r\n"
    };

    @Test
    public void testLines() throws IOException {
	for (String s : lines) {
	    LineInputStream is = createStream(s);
	    assertEquals("line1", is.readLine());
	    assertEquals("line2", is.readLine());
	    assertEquals("line3", is.readLine());
	    assertEquals(null, is.readLine());
	}
    }

    @Test
    public void testEmpty() throws IOException {
	for (String s : empty) {
	    LineInputStream is = createStream(s);
	    assertEquals("", is.readLine());
	    assertEquals("", is.readLine());
	    assertEquals("", is.readLine());
	    assertEquals(null, is.readLine());
	}
    }

    @Test
    public void testMixed() throws IOException {
	for (String s : mixed) {
	    LineInputStream is = createStream(s);
	    assertEquals("line1", is.readLine());
	    assertEquals("", is.readLine());
	    assertEquals("line3", is.readLine());
	    assertEquals(null, is.readLine());
	}
    }

    private LineInputStream createStream(String s) {
	try {
	return new LineInputStream(
	    new ByteArrayInputStream(s.getBytes("us-ascii")));
	} catch (UnsupportedEncodingException ex) {
	    return null;	// should never happen
	}
    }
}
