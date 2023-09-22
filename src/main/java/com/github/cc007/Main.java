package com.github.cc007;

import com.github.cc007.cfg.model.ContextFreeGrammar;
import com.github.cc007.cfg.model.result.Result;
import com.github.cc007.cfg.model.Rule;
import com.github.cc007.cfg.model.Terminal;
import com.github.cc007.cfg.model.TerminalWord;
import com.github.cc007.cfg.model.Variable;
import com.github.cc007.cfg.model.VariableWord;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Variable s = new Variable("S");
        Variable vp = new Variable("VP");
        Variable pp = new Variable("PP");
        Variable np = new Variable("NP");
        Variable v = new Variable("V");
        Variable p = new Variable("P");
        Variable n = new Variable("N");
        Variable det = new Variable("Det");
        TreeSet<Variable> variables = new TreeSet<>(
            Set.of(s, vp, pp, np, v, p, n, det)
        );
        Terminal<String> eats = new Terminal<>("eats");
        Terminal<String> she = new Terminal<>("she");
        Terminal<String> with = new Terminal<>("with");
        Terminal<String> fish = new Terminal<>("fish");
        Terminal<String> fork = new Terminal<>("fork");
        Terminal<String> a = new Terminal<>("a");
        TreeSet<Terminal<String>> terminals = new TreeSet<>(
            Set.of(eats, she, with, fish, fork, a)
        );
        TreeSet<Rule> rules = new TreeSet<>(
            Set.of(
                new Rule(s, new VariableWord(np, vp)),
                new Rule(vp, new VariableWord(vp, pp)),
                new Rule(vp, new VariableWord(v, np)),
                new Rule(vp, new TerminalWord<>(eats)),
                new Rule(pp, new VariableWord(p, np)),
                new Rule(np, new VariableWord(det, n)),
                new Rule(np, new TerminalWord<>(she)),
                new Rule(v, new TerminalWord<>(eats)),
                new Rule(p, new TerminalWord<>(with)),
                new Rule(n, new TerminalWord<>(fish)),
                new Rule(n, new TerminalWord<>(fork)),
                new Rule(det, new TerminalWord<>(a))
            )
        );
        ContextFreeGrammar<String> contextFreeGrammar = new ContextFreeGrammar<>(variables, terminals, rules, s);
        Result<String> result = contextFreeGrammar.match(
            List.of("she", "eats", "a", "fish", "with", "a", "fork")
        );
        System.out.println(result.hasMatch());
    }
}
