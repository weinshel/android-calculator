package com.benweinshel.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * Created by weinshel on 5/22/15.
 */
public class Operations {
    public static BigDecimal addValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.add(d1);
    }
    public static BigDecimal subtractValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.subtract(d1);
    }
    public static BigDecimal multiplyValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return d2.multiply(d1);
    }
    public static BigDecimal divideValues(List<BigDecimal> operationList) throws NumberFormatException {
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
    public static BigDecimal exponentiateValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return new BigDecimal(Math.pow(d2.doubleValue(), d1.doubleValue()));
    }
}
