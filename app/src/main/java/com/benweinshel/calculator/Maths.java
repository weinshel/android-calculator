package com.benweinshel.calculator;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import hugo.weaving.DebugLog;

/**
 * Created by Ben Weinshel on 5/22/15.
 * Does math
 */
public class Maths {
    public static String doMath(String input) throws Exception {

        // Check for parenthesis errors before doing anything else:
        checkParen(input);
        Stack<String> postfix = convertToPostfix(input);
        return evaluatePostfix(postfix);
    }

    private static void checkParen(String input) throws Exception {
        int parenCounter = 0;
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
                throw new Exception("Error: parenthesis");
            }
        }
        if (parenCounter != 0) {
            throw new Exception("Error: parenthesis");
        }
    }

    // Uses the shunting yard algorithm to generate a stack for the expression that is in postfix notation
    @DebugLog
    private static Stack<String> convertToPostfix(String input) throws Exception {

        Map<String, Integer> operationsMap = new HashMap<>();
        operationsMap.put("-", 1);
        operationsMap.put("+", 1);
        operationsMap.put("/", 2);
        operationsMap.put("*", 2);
        operationsMap.put("^", 3);

        Stack<String> stack = new Stack<>();
        Stack<String> output = new Stack<>();

        // Match all of the possible tokens: either a number, an operator, or ()
        // and put it in an array list
        List<String> allTokens = new ArrayList<>();
        Matcher m = Pattern.compile("[-\\+/*\\^\\(\\)]|-?\\d+(\\.\\d+)?|sin|cos|tan|arcsin|arccos|arctan").matcher(input);
        while (m.find()) {
            allTokens.add(m.group());
        }
        if (allTokens.isEmpty()) {
            return output;
        }
        else if (operationsMap.containsKey(allTokens.get(0))) {
            throw new Exception("Error: unexpected " + allTokens.get(0));
        }

        for (String token : allTokens) {

            if (token.isEmpty()) {
                continue;
            }

            // if the token is an operator, try to do some stack stuff
            if (operationsMap.containsKey(token)) {

                while (stack.size() > 0) {

                    // left-associative operations
                    if (token.equals("-") || token.equals("+") || token.equals("/") || token.equals("*")) {
                        String prev = stack.peek();
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
                        String prev = stack.peek();
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
            else if (token.matches("\\(|sin|cos|tan|arcsin|arccos|arctan")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.push(stack.pop());
                }
                try {
                    stack.pop();
                } catch (EmptyStackException e) {
                    throw new Exception("Error: parenthesis");
                }
            }
            // if the token is a number, push it to the output
            else { // if (operatorIndex = -1)
                output.push(token);
            }
        }

        // clear out the stack
        while (stack.size() > 0) {
            output.push(stack.pop());
        }

        return output;
    }

    // Evaluates a postfix expression
    @DebugLog
    private static String evaluatePostfix(Stack<String> postfixIn) throws Exception {

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
            else if (token.matches("[-\\+/\\*\\^]")) {
                List<BigDecimal> operationList = new ArrayList<>();

                for (int i = 1; i <=2; i++) {
                    try {
                        operationList.add(stack.pop());
                    } catch (EmptyStackException e) {
                        throw new Exception("Syntax error");
                    }
                }

                switch (token) {
                    case "+":
                        stack.push(Operations.addValues(operationList));
                        break;
                    case "-":
                        stack.push(Operations.subtractValues(operationList));
                        break;
                    case "*":
                        BigDecimal multResult = Operations.multiplyValues(operationList);
                        stack.push(multResult);
                        break;
                    case "/":
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
                throw new Exception("Syntax error");
            }
        }

        if (stack.size() == 1) {
            return stack.pop().toString();
        }
        else {
            throw new Exception("Syntax error");
        }
    }

    private static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number, optionally with - and .
    }
}
