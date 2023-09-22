package com.github.cc007.cfg.model.result;

import com.github.cc007.cfg.model.BackPointingTriple;
import com.github.cc007.cfg.model.ContextFreeGrammar;
import com.github.cc007.cfg.model.Variable;
import com.github.cc007.utils.Indexed;
import manifold.ext.rt.api.auto;

import java.util.Iterator;
import java.util.List;

public record Result<T extends Comparable<T>>(List<List<List<List<BackPointingTriple>>>> backPointers,
                                              ContextFreeGrammar<T> grammar, List<T> tokens) {
    public boolean hasMatch() {
        return !backPointers.last().first()
            .filterIndexed((index, elem) -> grammar.variables[index].equals(grammar.startVariable()))
            .map(Indexed::elem)
            .findFirst()
            .orElseThrow()
            .isEmpty();
    }

    public boolean hasSingleMatch() {
        return backPointers.last().first()
            .filterIndexed((index, elem) -> grammar.variables[index].equals(grammar.startVariable()))
            .map(Indexed::elem)
            .findFirst()
            .orElseThrow()
            .size() == 1; // todo check sub branches
    }


    public Node getFirstMatch() {
        int startVariableIdx = grammar.variables.indexOf(grammar.startVariable());
        var resolved = resolve(backPointers.size() - 1, 0, startVariableIdx, 0);
        return resolved.node;
    }

    private auto resolve(int layer, int span, int variableIdx, int matchIdx) {
        List<BackPointingTriple> backPointingTriples = backPointers[layer][span][variableIdx];
        Variable parent = grammar.variables[variableIdx];

        BackPointingTriple bpt = null;
        for (Iterator<BackPointingTriple> iterator = backPointingTriples.iterator(); iterator.hasNext(); ) {
            bpt = iterator.next();
            if (matchIdx == 0) break;
            matchIdx--;
        }
        Node node;
        if (bpt == null) {
            if (layer != tokens.size() - 1) throw new IllegalStateException("Couldn't find rule for variable '${parent.ref}'");
            node = new TerminalVariableNode<T>(parent.ref, tokens[span]);
        } else {
            var leftResolve = resolve(bpt.partition, span, bpt.leftVarIndex, matchIdx);
            var rightResolve = resolve(layer - bpt.partition - 1, span + bpt.partition + 1, bpt.rightVarIndex, leftResolve.currentMatchIdx);
            matchIdx = rightResolve.currentMatchIdx;
            node = new VariableNode(parent.ref, leftResolve.node, rightResolve.node);
        }
        var result = (node: node, currentMatchIdx: matchIdx);
        return result;
    }
}
