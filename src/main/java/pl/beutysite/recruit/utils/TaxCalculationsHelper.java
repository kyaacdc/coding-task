package pl.beutysite.recruit.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TaxCalculationsHelper {

    private final static int CURRENCY_SCALE = 4;
    private final static MathContext mathContext = new MathContext(CURRENCY_SCALE, RoundingMode.UP);

    //Increase percentage "+"
    public static BigDecimal addPercentage(BigDecimal base, BigDecimal percentage) {
        return base.add(getPercentagePart(base, percentage),mathContext);
    }

    //Get amount from base price by percentage
    public static BigDecimal getPercentagePart(BigDecimal base, BigDecimal percentage) {
        //return base.m(percentage, mathContext).divide(new BigDecimal(100),mathContext);
        return base.multiply(percentage, mathContext).divide(new BigDecimal(100),mathContext);

    }

    //Decrease percentage "-"
    public static BigDecimal subtractPercentage(BigDecimal base, BigDecimal percentage) {
        return base.subtract(getPercentagePart(base, percentage),mathContext);
    }

    //Get middle percentage for combined order with all Order Flags
    public static BigDecimal getMiddlePercentageAll(BigDecimal value1, BigDecimal value2, BigDecimal value3) {
        return value1.multiply(value2, mathContext).add(value3, mathContext).divide(new BigDecimal("3"), mathContext);
    }

    //Get middle percentage for combined order with two Order Flags
    public static BigDecimal getMiddlePercentage(BigDecimal value1, BigDecimal value2) {
        return value1.add(value2, mathContext).divide(new BigDecimal("2"), mathContext);
    }
}
