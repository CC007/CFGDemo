package com.github.cc007.cfg.model;

import com.github.cc007.utils.Indexed;
import dnl.utils.text.table.TextTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

        for (int tokenIdx : 0to_ tokenCount) {
            for (Rule rule : rules) {
                if (rule.word instanceof TerminalWord terminalWord) {
                    if (terminalWord.terminal.token.equals(tokens[tokenIdx])) {
                        int variableIdx = variables.indexOf(rule.variable);
                        possibilities[0][tokenIdx][variableIdx] = true;
                    }
                }
            }
        }

        for (int layer : 1 to_ tokenCount) {
            for (int span : 0 to_ (tokenCount - layer)) {
                printPossibilities(possibilities, tokens, layer, span);
                for (int partition : 0 to_ layer) {
                    printPossibilities(possibilities, tokens, layer, span, partition, span, layer - partition -1, span + partition + 1);
                    for (Rule rule : rules) {
                        if (rule.word instanceof VariableWord variableWord) {
                            int leftIdx = variables.indexOf(variableWord.left);
                            int rightIdx = variables.indexOf(variableWord.right);
                            if (possibilities[partition][span][leftIdx] && possibilities[layer - partition - 1][span + partition + 1][rightIdx]) {
                                int variableIdx = variables.indexOf(rule.variable);
                                possibilities[layer][span][variableIdx] = true;
                                backPointingTriples[layer][span][variableIdx].add(new BackPointingTriple(partition, leftIdx, rightIdx));
                            }
                        }
                    }
                }
            }
        }
        printPossibilities(possibilities, tokens);
        return new Result<>(backPointingTriples, this);
    }

    private void printPossibilities(List<List<List<Boolean>>> possibilities, List<T> tokens) {
        printPossibilities(possibilities, tokens, -1, -1);
    }

    private void printPossibilities(List<List<List<Boolean>>> possibilities, List<T> tokens, int layerPointer, int spanPointer) {
        printPossibilities(possibilities, tokens, layerPointer, spanPointer, -1, -1, -1, -1);
    }

    private void printPossibilities(List<List<List<Boolean>>> possibilities, List<T> tokens, int layerPointer, int spanPointer, int partitionLayerPointer1, int partitionSpanPointer1, int partitionLayerPointer2, int partitionSpanPointer2) {
        String[] columnNames = tokens
            .map(Object::toString)
            .map(tokenStr -> tokenStr + " ")
            .toArray(String[]::new);

        Object[][] data = possibilities
            .map(layer -> layer
                .map(span -> {
                        Function<Indexed<Boolean>, Variable> getVariable = indexed -> variables[indexed.index];
                        return span.filterIndexed((index, variableMatch) -> variableMatch)
                            .map(getVariable)
                            .map(Variable::ref)
                            .collect(Collectors.joining(", ")) + " ";
                    }
                )
                .toArray(Object[]::new)
            )
            .toArray(Object[][]::new);

        if (layerPointer >= 0) {
            data[layerPointer][spanPointer] = "x";
            if (partitionLayerPointer1 >= 0) {
                data[partitionLayerPointer1][partitionSpanPointer1] += " (a)";
                data[partitionLayerPointer2][partitionSpanPointer2] += " (b)";
            } else {
                for (int partitionPointer : 0 to_ layerPointer) {
                    data[partitionPointer][spanPointer] += " |";
                    data[layerPointer - partitionPointer - 1][spanPointer + partitionPointer + 1] += " /";
                }
            }
        }
        TextTable textTable = new TextTable(columnNames, data);
        textTable.setAddRowNumbering(true);
        textTable.printTable();
    }

    private void forVariables(List<List<List<Boolean>>> possibilities, int span, int layer, Consumer<Variable> action) {
        for (int variableIdx : 0to_(possibilities[layer][span].size())) {
            if (possibilities[layer][span][variableIdx]) {
                action.accept(variables[variableIdx]);
            }
        }
    }

    private static <T> List<List<List<T>>> getStairs(int layerCount, int variables, Supplier<T> init) {
        List<List<List<T>>> stairs = new ArrayList<>(layerCount);
        for (int layer : 0 to_ layerCount) {
            int spanCount = layerCount - layer;
            stairs.add(new ArrayList<>(spanCount));
            for (int span : 0 to_ spanCount) {
                stairs[layer].add(new ArrayList<>(variables));
                for (int ignored : 0 to_ variables) {
                    stairs[layer][span].add(init.get());
                }
            }
        }
        return stairs;
    }
}
