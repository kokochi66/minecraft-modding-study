package com.kokochi.tech.dbshard.shard.router;

import lombok.Getter;

import java.util.Iterator;
import java.util.List;

public class RoundRobin<T> {

    @Getter
    private final List<T> list;

    private final Iterator<T> iterator;
    private int index;

    public RoundRobin(List<T> list) {
        System.out.println("TEST :: RoundRobin - RoundRobin() :: 1");
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
        System.out.println("TEST :: RoundRobin - next() :: 1");
        return iterator.next();
    }

    public void add(T item) {
        System.out.println("TEST :: RoundRobin - add() :: 1");
        list.add(item);
    }
}
