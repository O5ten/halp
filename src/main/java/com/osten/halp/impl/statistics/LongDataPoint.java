package com.osten.halp.impl.statistics;

import com.osten.halp.api.model.statistics.DataPoint;

public class LongDataPoint implements DataPoint<Long> {
    private Long data;

    public LongDataPoint(Long l) {
        this.data = l;

    }

    public LongDataPoint(String s) throws IllegalArgumentException{
        this.data = Long.valueOf( s );
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