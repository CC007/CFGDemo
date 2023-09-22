package com.github.cc007.cfg.model.result;

public record VariableNode(String ref, Node left, Node right) implements Node {
}
