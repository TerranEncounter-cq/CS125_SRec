package com.example.srec;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    public void apiTest() {
        Method api;
        try {
            api = Class.forName("com.example.srec.MainActivity.")
                    .getMethod("sendApiRequest");
        } catch (Exception e) {
            test();
            return;
        }

    }
    public void test() {


    }
}