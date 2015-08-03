package com.benweinshel.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
//import org.nevec.rjm.BigDecimalMath;
import hugo.weaving.DebugLog;


/**
 * Created by Ben Weinshel on 5/22/15.
 *
 * Contains all the operations (+ - * / ^) to do math
 */
class Operations {
     static BigDecimal addValues(List<BigDecimal> operationList) throws NumberFormatException {
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

    @DebugLog
    static BigDecimal exponentiateValues(List<BigDecimal> operationList) throws NumberFormatException {
        BigDecimal d1 = operationList.get(0);
        BigDecimal d2 = operationList.get(1);
        return new BigDecimal(Math.pow(d2.doubleValue(), d1.doubleValue()));
//        return BigDecimalMath.pow(d2,d1);
    }
    static BigDecimal calculateSin(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.sin(d.doubleValue()));
//        return BigDecimalMath.sin(d);
    }
    static BigDecimal calculateCos(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.cos(d.doubleValue()));
//        return BigDecimalMath.cos(d);
    }
    static BigDecimal calculateTan(BigDecimal d) throws NumberFormatException {
        return new BigDecimal(Math.tan(d.doubleValue()));
//        return BigDecimalMath.tan(d);
    }
    static BigDecimal calculateASin(BigDecimal d) throws Exception {
        if (d.abs().min(BigDecimal.ONE) == BigDecimal.ONE) {
            throw new Exception("Error: cannot calculate arcsin(" + d + ")");
        } else {
            return new BigDecimal(Math.asin(d.doubleValue()));
        }
       // return BigDecimalMath.asin(d);
    }
    static BigDecimal calculateACos(BigDecimal d) throws Exception {
        if (d.abs().min(BigDecimal.ONE) == BigDecimal.ONE) {
            throw new Exception("Error: cannot calculate arccos(" + d + ")");
        } else {
            return new BigDecimal(Math.acos(d.doubleValue()));
        }
//        return BigDecimalMath.acos(d);
    }
    static BigDecimal calculateATan(BigDecimal d) throws Exception {
        return new BigDecimal(Math.atan(d.doubleValue()));
//        return BigDecimalMath.atan(d);
    }
}
