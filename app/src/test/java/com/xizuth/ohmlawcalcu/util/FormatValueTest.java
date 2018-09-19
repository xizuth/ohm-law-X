package com.xizuth.ohmlawcalcu.util;

import org.junit.Test;

import static org.junit.Assert.*;


public class FormatValueTest {

    @Test
    public void roundFormatTest(){
        String value = FormatValue.roundFormat(4.23);
        String value2 = FormatValue.roundFormat(4.00);
        assertEquals("4.23", value);
        assertEquals("4", value2);
    }
}
