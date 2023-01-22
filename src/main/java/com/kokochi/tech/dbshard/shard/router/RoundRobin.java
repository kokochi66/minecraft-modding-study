package com.kokochi.tech.dbshard.shard.router;

import java.util.Iterator;
import java.util.List;

public class RoundRobin<T> {

    private final List<T> list;
    private final Iterator<T> iterator;
    private int index;

    public RoundRobin(List<T> list) {
        this.list = list;
        index = 0;
        this.iterator = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                T value = list.get(index);
                index = (index + 1) % list.size();
                return value;
            }
        };
    }

    public T next() {
        return iterator.next();
    }

    public void add(T item) {
        list.add(item);
    }
}
