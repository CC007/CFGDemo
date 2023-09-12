package com.github.cc007.cfg.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Supplier;

import static manifold.collections.api.range.RangeFun.to_;

/**
 * @param variables
 * @param terminals
 * @param rules
 * @param startVariable
 * @param <T>           terminal token type
 */
public record ContextFreeGrammar<T extends Comparable<T>>(
    SortedSet<Variable> variables,
    SortedSet<Terminal<T>> terminals,
    SortedSet<Rule> rules,
    Variable startVariable
) {

    public Result<T> parse(List<T> tokens) {
        int tokenCount = tokens.size();
        int variableCount = variables.size();
        List<List<List<Boolean>>> possibilities = getStairs(tokenCount, variableCount, () -> false);
        List<List<List<List<BackPointingTriple>>>> backPointingTriples = getStairs(tokenCount, variableCount, LinkedList::new);

        for (int s : 0 to_ tokenCount) {
            for (Rule rule : rules) {
                if (rule.word instanceof TerminalWord terminalWord) {
                    if (terminalWord.terminal.token.equals(tokens[s])) {
                        int v = variables.indexOf(rule.variable);
                        possibilities[0][s][v] = true;
                    }
                }
            }
        }

        for (int layer : 1 to_ tokenCount) {
            for (int span : 0 to_ (tokenCount - layer)) {
                for (int partition : 0 to_ layer) {
                    for (Rule rule : rules) {
                        if (rule.word instanceof VariableWord variableWord) {
                            int leftIdx = variables.indexOf(variableWord.left);
                            int rightIdx = variables.indexOf(variableWord.right);
                            if (possibilities[partition][span][leftIdx] && possibilities[layer - partition][span + partition][rightIdx]) {
                                int a = variables.indexOf(rule.variable);
                                possibilities[layer][span][a] = true;
                                backPointingTriples[layer][span][a].add(new BackPointingTriple(partition, leftIdx, rightIdx));
                            }
                        }
                    }
                }
            }
        }
        printP(possibilities);
        return new Result<>(backPointingTriples, this);
    }

    private void printP(List<List<List<Boolean>>> possibilities) {
        for (int revLayer : 0 to_ (possibilities.size()) ) {
            int layer = possibilities.size() - revLayer - 1;
            System.out.print("$layer: | ");
            for (int span : 0 to_ (possibilities[layer].size())) {
                for (int variable : 0 to_ (possibilities[layer][span].size())) {
                    if (possibilities[layer][span][variable]) {
                        System.out.print("${variables[variable].ref} ");
                    }
                }
                System.out.print("\t| ");
            }
            System.out.println();
        }
    }

    private static <T> List<List<List<T>>> getStairs(int layerCount, int variables, Supplier<T> init) {
        List<List<List<T>>> stairs = new ArrayList<>(layerCount);
        for (int layer : 0 to_ layerCount) {
            int spanCount = layerCount - layer;
            stairs.add(new ArrayList<>(spanCount));
            for (int span : 0 to_ spanCount) {
                stairs[layer].add(new ArrayList<>(variables));
                for (int k : 0 to_ variables) {
                    stairs[layer][span].add(init.get());
                }
            }
        }
        return stairs;
    }
}
