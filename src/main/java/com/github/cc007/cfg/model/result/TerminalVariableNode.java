package com.github.cc007.cfg.model.result;

public record TerminalVariableNode<T extends Comparable<T>>(String ref, T token) implements Node {
}
