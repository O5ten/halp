package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;

public class LongDataPoint implements DataPoint<Long> {
    private Long data;

    public LongDataPoint(Long l) {
        this.data = l;

    }

    public LongDataPoint(String s) {
        try {
            this.data = Long.parseLong(s);
        } catch (IllegalArgumentException e) {
            System.out.println("Cannot parse long from " + s);
            e.printStackTrace();
        }
    }

    @Override
    public Long getData() {
        return data;
    }

    @Override
    public void setData(Long data) {
        this.data = data;
    }
}