package com.github.cc007.cfg.model;

public sealed interface Word extends Comparable<Word> permits TerminalWord, VariableWord {
}
