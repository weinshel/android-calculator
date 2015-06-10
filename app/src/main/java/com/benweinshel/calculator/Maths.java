package com.benweinshel.calculator;

import android.content.Context;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hugo.weaving.DebugLog;

/**
 * Created by Ben Weinshel on 5/22/15.
 * Does math
 */
class Maths {
    public static String doMath(String input, Context c) throws Exception {

        // Check for parenthesis errors before doing anything else:
        checkParen(input, c);
        Stack<String> postfix = convertToPostfix(input, c);
        return evaluatePostfix(postfix, c);
    }

    private static void checkParen(String input, Context c) throws Exception {
        int parenCounter = 0;
        boolean unbalanced = false;
        for (char ch: input.toCharArray()) {
            switch (ch) {
                case '(':
                    parenCounter++;
                    break;
                case ')':
                    parenCounter--;
                    break;
            }
            if (parenCounter == -1) {
                unbalanced = true;
            }
        }

        if (parenCounter < 0) {
            throw new Exception(c.getString(R.string.paren_r_over_l));
        }
        else if (parenCounter > 0) {
            throw new Exception(c.getString(R.string.paren_l_over_r));
        }
        else if (unbalanced) {
            throw new Exception(c.getString(R.string.paren_unbalanced));
        }
    }

    // Uses the shunting yard algorithm to generate a stack for the expression that is in postfix notation
    @DebugLog
    private static Stack<String> convertToPostfix(String input, Context c) throws Exception {

        Map<String, Integer> operationsMap = new HashMap<>();
        operationsMap.put("-", 1);
        operationsMap.put("+", 1);
        operationsMap.put("×", 2);
        operationsMap.put("÷", 2);
        operationsMap.put("^", 3);

        Stack<String> stack = new Stack<>();
        Stack<String> output = new Stack<>();

        // Match all of the possible tokens: either a number, an operator, or ()
        // and put it in an array list
        List<String> allTokens = new ArrayList<>();
        Matcher m = Pattern.compile("[-\\+÷×\\^\\(\\)]|-?\\d+(\\.\\d+)?|-?\\.\\d+|arcsin|arccos|arctan|sin|cos|tan|").matcher(input);
        while (m.find()) {
            allTokens.add(m.group());
        }
        if (allTokens.isEmpty()) {
            return output;
        }
        else if (operationsMap.containsKey(allTokens.get(0))) {
            throw new Exception("Error: unexpected ‟" + allTokens.get(0) + "”");
        }

        String prevToken = "";
        for (String token : allTokens) {

            if (token.isEmpty()) {
                continue;
            }

            // if the token is an operator, try to do some stack stuff
            if (operationsMap.containsKey(token)) {

                while (stack.size() > 0) {

                    String prev = stack.peek();

                    // handle an error where two consecutive operators are typed in
                    if (operationsMap.containsKey(prevToken)) {
                        throw new Exception("Two consecutive operators: ‟" + prev + "” and ‟" + token + "”");
                    }

                    // left-associative operations
                    if (token.equals("-") || token.equals("+") || token.equals("÷") || token.equals("×")) {
                        if (prev.equals("(")) {
                            break;
                        }
                        else if (operationsMap.get(token) <= operationsMap.get(prev)) {
                            output.push(stack.pop());
                        }
                        else {
                            break;
                        }
                    }
                    // right-associative operations
                    else if (token.equals("^")) {
                        if (prev.equals("(")) {
                            break;
                        }
                        else if (operationsMap.get(token) < operationsMap.get(prev)) {
                            output.push(stack.pop());
                        }
                        else {
                            break;
                        }
                    }
                }
                stack.push(token);
            }

            // if a left ( or a function, push the token directly to the stack
            else if (token.matches("sin|cos|tan|arcsin|arccos|arctan")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.push(stack.pop());
                }
                try {
                    stack.pop();
                } catch (EmptyStackException e) {
                    throw new Exception(c.getString(R.string.paren_generic));
                }
            }
            else if (token.equals("(")) {
                if (isNumeric(prevToken) || prevToken.equals(")")) {
                    stack.push("×");
                }
                stack.push(token);
            }
            // if the token is a number, push it to the output
            else {
                output.push(token);
            }

            // store the previous token to check for implied multiply with ()
            prevToken = token;
        }

        // clear out the stack
        while (stack.size() > 0) {
            output.push(stack.pop());
        }

        return output;
    }

    // Evaluates a postfix expression
    @DebugLog
    private static String evaluatePostfix(Stack<String> postfixIn, Context c) throws Exception {

        if (postfixIn.isEmpty()) {
            return null;
        }

        Stack<String> reversedPostfix = new Stack<>();
        Stack<BigDecimal> stack = new Stack<>();
        // reverse the stack
        while (postfixIn.size() > 0) {
            reversedPostfix.push(postfixIn.pop());
        }

        while (reversedPostfix.size() > 0) {
            String token = reversedPostfix.pop();
            if (isNumeric(token)) {
                BigDecimal bdToken = new BigDecimal(token);
                stack.push(bdToken);
            }
            else if (token.matches("[-\\+÷×\\^]")) {
                List<BigDecimal> operationList = new ArrayList<>();

                switch (stack.size()) {
                    case 0:
                        throw new Exception("Syntax error: nothing to operate ‟" + token + "” on");
                    case 1:
                        throw new Exception("Syntax error: unable to operate ‟" + token + "” on only ‟" + stack.peek() + "”");
                }
                for (int i = 1; i <=2; i++) {
                    operationList.add(stack.pop());
                }

                switch (token) {
                    case "+":
                        stack.push(Operations.addValues(operationList));
                        break;
                    case "-":
                        stack.push(Operations.subtractValues(operationList));
                        break;
                    case "×":
                        BigDecimal multResult = Operations.multiplyValues(operationList);
                        stack.push(multResult);
                        break;
                    case "÷":
                        BigDecimal divResult = Operations.divideValues(operationList);
                        stack.push(divResult);
                        break;
                    case "^":
                        stack.push(Operations.exponentiateValues(operationList));
                        break;
                }
            }
            else if (token.matches("sin|cos|tan|arcsin|arccos|arctan")) {
                try {
                    switch (token) {
                        case "sin":
                            stack.push(Operations.calculateSin(stack.pop()));
                            break;
                        case "cos":
                            stack.push(Operations.calculateCos(stack.pop()));
                            break;
                        case "tan":
                            stack.push(Operations.calculateTan(stack.pop()));
                            break;
                        case "arcsin":
                            stack.push(Operations.calculateASin(stack.pop()));
                            break;
                        case "arccos":
                            stack.push(Operations.calculateACos(stack.pop()));
                            break;
                        case "arctan":
                            stack.push(Operations.calculateATan(stack.pop()));
                            break;
                    }
                } catch (EmptyStackException e) {
                    throw new Exception("Syntax error: nothing to calculate \u201c" + token + "\u201d of");
                }
            }
            else {
                throw new Exception("Syntax error: unrecognized input ‟" + token + "”" );
            }
        }

        if (stack.size() == 1) {
            BigDecimal result = stack.pop();
            BigDecimal roundedResult = result.round(MathContext.DECIMAL32);
            return roundedResult.toString();
        }
        else {
            throw new Exception(c.getString(R.string.insufficient_operators));
        }
    }

    private static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?|-?\\.\\d+"); // match a number, optionally with - and .
    }
}
