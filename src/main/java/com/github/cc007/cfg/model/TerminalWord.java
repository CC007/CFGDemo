package com.github.cc007.cfg.model;

public record TerminalWord<T extends Comparable<T>>(Terminal<T> terminal) implements Word {

    public int compareTo(TerminalWord<T> other) {
        return terminal.compareTo(other.terminal);
    }

    @Override
    public int compareTo(Word o) {
        if (o instanceof TerminalWord) {
            return compareTo((TerminalWord<T>) o);
        } else if (o instanceof VariableWord) {
            return -1;
        } else {
            throw new IllegalStateException("Sealed interface only permits TerminalWord and VariableWord");
        }
    }
}
