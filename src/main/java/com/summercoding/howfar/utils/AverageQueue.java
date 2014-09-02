package com.summercoding.howfar.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by piotrek on 9/1/14.
 */
public class AverageQueue {

    private int size;
    private Queue<Float> values = new LinkedList<Float>();
    private float sum;

    public AverageQueue(int size) {
        this.size = size;
        this.sum = 0.0f;
    }

    public float getAverage() {
        return sum / Math.max(1, values.size());
    }

    public void addValue(float v) {
        values.offer(v);
        sum += v;

        if (values.size() > size) {
            sum -= values.poll();
        }
    }
}
