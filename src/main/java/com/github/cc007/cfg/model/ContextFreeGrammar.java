package com.github.cc007.cfg.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    public List<List<List<List<Variable>>>> parse(List<T> a) {
        int n = a.size();
        int r = variables.size();
        List<List<List<Boolean>>> P = new ArrayList<>(); //TODO init
        List<List<List<List<Variable>>>> back = new ArrayList<>(); //TODO init

        for (int s : 0to_ n) {
            int v = rules.index.filter(rule ->
                rule.word instanceof TerminalWord terminalWord
                    && terminalWord.terminal.token.equals(a[s])
            ).mapIndexed;
            P[1][s][v] = true;
        }

        for (int l : 1to_ n) {
            for (int s : 0to_(n - l + 1)) {
                for (int p : 0to_(l - 1)) {
                    int v = rules.indexOfFirst(rule ->
                        rule.word instanceof VariableWord variableWord
                            && variableWord..terminal.token.equals(a[s])
                    );
                }
            }
        }
        return back;
    }
}
