package com.summercoding.howfar.utils;

import java.util.LinkedList;
import java.util.Queue;


public class AverageQueue {

    private final int capacity;
    private Queue<Float> values = new LinkedList<Float>();
    private float sum;

    public AverageQueue(int capacity) {
        Preconditions.greatherThanZero(capacity);
        this.capacity = capacity;
        this.sum = 0.0f;
    }

    public float getAverage() {
        return sum / Math.max(1, values.size());
    }

    public void addValue(float v) {
        values.offer(v);
        sum += v;
        adjustToCapacity();
    }

    private void adjustToCapacity() {
        if (values.size() > capacity) {
            sum -= values.poll();
        }
    }
}
