package com.github.cc007.cfg.model;

import manifold.tuple.rt.api.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import static manifold.collections.api.range.RangeFun.to_;

/**
 * @param variables
 * @param terminals
 * @param rules
 * @param startVariable
 * @param <T>           terminal token type
 */
public record ContextFreeGrammar<T>(
    SortedSet<Variable> variables,
    SortedSet<Terminal<T>> terminals,
    SortedSet<Rule> rules,
    Variable startVariable
) {

    public List<List<List<List<Tuple>>>> parse(List<T> tokens) {
        int n = tokens.size();
        int r = variables.size();
        List<List<List<Boolean>>> P = new ArrayList<>(); //TODO init
        List<List<List<List<Tuple>>>> back = new ArrayList<>(); //TODO init

        for (int s : 0to_ n) {
            for (Rule rule: rules) {
                if (rule.word instanceof TerminalWord terminalWord) {
                    if (terminalWord.terminal.token.equals(tokens[s])) {
                        int v = variables.indexOf(rule.variable);
                        P[0][s][v] = true;
                    }
                }
            }
        }

        for (int l : 1to_ n) {
            for (int s : 0to_(n - l + 1)) {
                for (int p : 0to_(l - 1)) {
                    for (Rule rule: rules) {
                        if (rule.word instanceof VariableWord variableWord) {
                            int b = variables.indexOf(variableWord.left);
                            int c = variables.indexOf(variableWord.right);
                            if (P[p][s][b] && P[l - p][s + p][c]) {
                                int a = variables.indexOf(rule.variable);
                                P[l][s][a] = true;
                                back[l][s][a].add((p, b, c));
                            }
                        }
                    }
                }
            }
        }
        return back;
    }
}
