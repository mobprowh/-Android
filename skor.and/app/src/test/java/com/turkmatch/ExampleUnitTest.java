package com.turkmatch;

import com.bahisadam.utility.Utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void timeFormat_isCorrect() throws Exception {
        String a = "13:00";
        String b = Utilities.formatTime("2016-11-18T11:00:00.000Z");

        assertEquals(a,b);
    }
}