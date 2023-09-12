package com.github.cc007.cfg.model;

public record Terminal<T extends Comparable<T>>(T token) implements Comparable<Terminal<T>> {

    @Override
    public int compareTo(Terminal<T> other) {
        return token.compareTo(other.token);
    }
}
