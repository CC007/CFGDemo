package com.github.cc007.cfg.model;

public record Rule(Variable variable, Word word) implements Comparable<Rule> {

    @Override
    public int compareTo(Rule other) {
        int variableCompare = variable.compareTo(other.variable);
        if (variableCompare != 0)
            return variableCompare;
        return word.compareTo(other.word);
    }
}
