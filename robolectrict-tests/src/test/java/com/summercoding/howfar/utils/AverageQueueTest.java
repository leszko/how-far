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


    @Test public void shouldReturnZeroWhenNoElements() {
        // given
        AverageQueue queue = new AverageQueue(10);
        // when
        float result = queue.getAverage();
        // then
        assertEquals(0.0f, result, DELTA);
    }

    @Test public void shouldReturnTheOnlyElementWhenOnlyOneElementInTheQueue() {
        // given
        AverageQueue queue = new AverageQueue(10);
        // when
        queue.addValue(1.0f);
        // then
        assertEquals(1.0f, queue.getAverage(), DELTA);
    }

    @Test public void shouldRemoveOldElementsWhenExceedingCapacity() {
        // given
        AverageQueue queue = new AverageQueue(1);
        // when
        queue.addValue(1.0f);
        queue.addValue(2.0f);
        // then
        assertEquals(2.0f, queue.getAverage(), DELTA);
    }

    @Test public void shouldComputeCorrectAverageWhenTwoElements() {
        // given
        AverageQueue queue = new AverageQueue(2);
        // when
        queue.addValue(1.0f);
        queue.addValue(2.0f);
        // then
        assertEquals(1.5f, queue.getAverage(), DELTA);
    }
}
