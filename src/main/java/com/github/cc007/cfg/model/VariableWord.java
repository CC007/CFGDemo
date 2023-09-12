package com.github.cc007.cfg.model;

public record VariableWord(Variable left, Variable right) implements Word {

    public int compareTo(VariableWord other) {
        int leftCompare = left.compareTo(other.left);
        if (leftCompare != 0) return leftCompare;
        return right.compareTo(other.right);
    }

    @Override
    public int compareTo(Word o) {
        if (o instanceof TerminalWord) {
            return 1;
        } else if (o instanceof VariableWord) {
            return compareTo((VariableWord) o);
        } else {
            throw new IllegalStateException("Sealed interface only permits TerminalWord and VariableWord");
        }
    }
}
