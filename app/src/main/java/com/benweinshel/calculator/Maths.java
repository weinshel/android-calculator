package com.benweinshel.calculator;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by weinshel on 5/22/15.
 */
public class Maths {
    public static String doMath(String input) {
        Stack<String> postfix = convertToPostfix(input);
        return evaluatePostfix(postfix);
    }

    // Uses the shunting yard algorithm to generate a stack for the expression that is in postfix notation
    private static Stack<String> convertToPostfix(String input) {

        // TODO: unified list of operations
        String operations = "-+/*^";
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
        Matcher m = Pattern.compile("[-\\+/*\\^\\(\\)]|-?\\d+(\\.\\d+)?").matcher(input);
        while (m.find()) {
            allTokens.add(m.group());
        }

        for (String token : allTokens) {

            if (token.isEmpty()) {
                continue;
            }
            char c = token.charAt(0); // first character of a token
            // int operatorIndex = operations.indexOf(c);

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

            // TODO: fix ( 16 ^ 3 ) + 50 * 7 / ( 82 - 93 ) * 12
            else if (token.equals("(")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.push(stack.pop());
                }
                stack.pop();
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
    private static String evaluatePostfix(Stack<String> postfixIn) {

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
            // TODO: unified list of operations
            else if (token.matches("[-\\+/\\*\\^]")) {
                List<BigDecimal> operationList = new ArrayList<>();

                // TODO: error handling
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
        }

        if (stack.size() == 1) {
            return stack.pop().toString();
        }
        return "If you're reading this, there's been an error";
    }

    private static boolean isNumeric(String str){
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number, optionally with - and .
    }
}
