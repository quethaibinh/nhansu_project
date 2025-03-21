package com.example.quanlynhansu.utils;

public class PairClass<T, N> {
    private T first;
    private N second;

    public PairClass(T first, N second){
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public N getSecond() {
        return second;
    }

    public void setSecond(N second) {
        this.second = second;
    }
}
