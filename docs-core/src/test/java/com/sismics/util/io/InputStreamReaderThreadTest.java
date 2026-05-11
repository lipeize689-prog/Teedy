package com.sismics.util.io;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test of {@link InputStreamReaderThread}.
 */
public class InputStreamReaderThreadTest {

    private static final Logger LOGGER = Logger.getLogger(InputStreamReaderThread.class);

    @Test
    public void constructorAndRunWithDebugEnabledTest() {
        Level previousLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.DEBUG);
        try {
            InputStreamReaderThread thread = new InputStreamReaderThread(
                    new ByteArrayInputStream("line1\nline2".getBytes(StandardCharsets.UTF_8)),
                    "reader");

            Assert.assertEquals("reader InputStreamReader thread", thread.getName());
            thread.run();
        } finally {
            LOGGER.setLevel(previousLevel);
        }
    }

    @Test
    public void runWithDebugDisabledTest() {
        Level previousLevel = LOGGER.getLevel();
        LOGGER.setLevel(Level.INFO);
        try {
            InputStreamReaderThread thread = new InputStreamReaderThread(
                    new ByteArrayInputStream("line1".getBytes(StandardCharsets.UTF_8)),
                    "reader");

            thread.run();
        } finally {
            LOGGER.setLevel(previousLevel);
        }
    }

    @Test
    public void runWithIOExceptionTest() {
        FailingInputStream inputStream = new FailingInputStream();
        InputStreamReaderThread thread = new InputStreamReaderThread(inputStream, "reader");

        thread.run();

        Assert.assertTrue(inputStream.closed);
    }

    private static class FailingInputStream extends InputStream {
        private boolean closed;

        @Override
        public int read() throws IOException {
            throw new IOException("Failure while reading");
        }

        @Override
        public void close() throws IOException {
            closed = true;
            super.close();
        }
    }
}
