package com.github.cc007.cfg.model;

import java.util.List;

public record Result<T extends Comparable<T>>(List<List<List<List<BackPointingTriple>>>> backPointers, ContextFreeGrammar<T> grammar) {

}
