package com.summercoding.howfar.utils;

import com.summercoding.howfar.RobolectricGradleTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Created by piotrek on 9/1/14.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class AverageQueueTest {
    private static float DELTA = 0.0001f;

    @Test public void averageOfZeroElementsIsZero() {
        AverageQueue queue = new AverageQueue(10);
        assertEquals(0.0f, queue.getAverage(), DELTA);
    }

    @Test public void averageOfOneElementIsTheElement() {
        AverageQueue queue = new AverageQueue(10);
        queue.addValue(1.0f);
        assertEquals(1.0f, queue.getAverage(), DELTA);
    }

    @Test public void addValueRemovesOldElements() {
        AverageQueue queue = new AverageQueue(1);
        queue.addValue(1.0f);
        queue.addValue(2.0f);
        assertEquals(2.0f, queue.getAverage(), DELTA);
    }

    @Test public void computesAverageOfTwoElements() {
        AverageQueue queue = new AverageQueue(2);
        queue.addValue(1.0f);
        queue.addValue(2.0f);
        assertEquals(1.5f, queue.getAverage(), DELTA);
    }
}
