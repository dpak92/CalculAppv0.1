package com.example.calculapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ExpressionLIb : provides the static evalExp method to evaluate an arithmetic expression
 * from a String and return a Double result.
 *
 * code copied from https://codereview.stackexchange.com/questions/157290/
 * reading-integers-and-evaluating-an-arithmetic-operation-in-a-string
 * *
 */

public class ExpressionsLib {
    private static final Pattern EXPR_RE = Pattern.compile("\\G\\s*([+-]?)\\s*([^+-]+)"),
            TERM_RE = Pattern.compile("\\G(^|(?<!^)\\*|(?<!^)/)\\s*(\\d*\\.?\\d+)\\s*");

    public static double evalExp (String expr) {
        Matcher m = EXPR_RE.matcher(expr);
        double sum = 0;
        int matchEnd;
        for (matchEnd = -1; m.find(); matchEnd = m.end()) {
            sum += (("-".equals(m.group(1))) ? -1 : +1) * evalTerm(m.group(2));
        }
        if (matchEnd != expr.length()) {
            throw new IllegalArgumentException("Invalid expression \"" + expr + "\"");
        }
        return sum;
    }

    private static double evalTerm (String term) {
        Matcher m = TERM_RE.matcher(term);
        double product = Double.NaN;
        int matchEnd;
        for (matchEnd = -1; m.find(); matchEnd = m.end()) {
            switch (m.group(1)) {
                case "*": product *= Double.parseDouble(m.group(2)); break;
                case "/": product /= Double.parseDouble(m.group(2)); break;
                case "":  product  = Double.parseDouble(m.group(2)); break;
            }
        }
        if (matchEnd != term.length()) {
            throw new IllegalArgumentException("Invalid term \"" + term + "\"");
        }
        return product;
    }
}

