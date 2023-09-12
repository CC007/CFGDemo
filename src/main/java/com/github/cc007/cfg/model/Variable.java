package com.github.cc007.cfg.model;

public record Variable(String ref) implements Comparable<Variable> {

    @Override
    public int compareTo(Variable other) {
        return ref.compareTo(other.ref);
    }
}
