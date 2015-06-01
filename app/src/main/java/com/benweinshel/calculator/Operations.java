package com.benweinshel.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * Created by Ben Weinshel on 5/22/15.
 *
 * Contains all the operations (+ - * / ^) to do math
 */
class Operations {
    public static BigDecimal addValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.add(d1);
    }
    static BigDecimal subtractValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.subtract(d1);
    }
    static BigDecimal multiplyValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.multiply(d1);
    }
    static BigDecimal divideValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        BigDecimal result;
        try {
            result = d2.divide(d1);
        }
        catch (ArithmeticException e) {
            MathContext mc = MathContext.DECIMAL128;
            result = d2.divide(d1, mc);
        }
        return result;
    }
    static BigDecimal exponentiateValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return new BigDecimal(Math.pow(d2.doubleValue(), d1.doubleValue()));
    }
    static BigDecimal calculateSin(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.sin(d.doubleValue()));
    }
    static BigDecimal calculateCos(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.cos(d.doubleValue()));
    }
    static BigDecimal calculateTan(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.tan(d.doubleValue()));
    }
    static BigDecimal calculateASin(BigDecimal d) throws Exception {
        if (d.abs().min(BigDecimal.ONE) == BigDecimal.ONE) {
            throw new Exception("Error: cannot calculate arcsin(" + d + ")");
        } else {
            return new BigDecimal(Math.asin(d.doubleValue()));
        }
    }
    static BigDecimal calculateACos(BigDecimal d) throws Exception {
        if (d.abs().min(BigDecimal.ONE) == BigDecimal.ONE) {
            throw new Exception("Error: cannot calculate arccos(" + d + ")");
        } else {
            return new BigDecimal(Math.acos(d.doubleValue()));
        }
    }
    static BigDecimal calculateATan(BigDecimal d) throws Exception {
        return new BigDecimal(Math.atan(d.doubleValue()));
    }
}
