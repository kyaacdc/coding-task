package pl.beutysite.recruit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TaxCalculationsHelper {

    private final static int CURRENCY_SCALE=2;
    private final static MathContext mathContext=new MathContext(CURRENCY_SCALE, RoundingMode.UP);

    //Increase percentage "+"
    public static BigDecimal addPercentage(BigDecimal base, BigDecimal percentage) {
        return base.add(getPercentagePart(base, percentage),mathContext);
    }

    //Get amount from base price by percentage
    public static BigDecimal getPercentagePart(BigDecimal base, BigDecimal percentage) { // TODO: 01.03.17 tests
        return base.divide(percentage, mathContext).divide(new BigDecimal(100),mathContext);

        //Here is mistake, that was in old program release, that repaired myself.
        //return percentage.divide(new BigDecimal(100),mathContext).multiply(base,mathContext);
    }

    //Decrease percentage "-"
    public static BigDecimal subtractPercentage(BigDecimal base, BigDecimal percentage) {
        return base.subtract(getPercentagePart(base, percentage),mathContext);
    }

    //Get middle percentage for combined order with all Order Flags
    public static BigDecimal getMiddlePercentageAll(BigDecimal value1, BigDecimal value2, BigDecimal value3) {
        return value1.multiply(value2, mathContext).multiply(value3, mathContext).divide(new BigDecimal("3"), mathContext);
    }

    //Get middle percentage for combined order with two Order Flags
    public static BigDecimal getMiddlePercentage(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2, mathContext).divide(new BigDecimal("2"), mathContext);
    }
}
