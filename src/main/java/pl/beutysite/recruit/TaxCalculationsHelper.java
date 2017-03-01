package pl.beutysite.recruit;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TaxCalculationsHelper {

    private final static int CURRENCY_SCALE=2;
    private final static MathContext mathContext=new MathContext(CURRENCY_SCALE, RoundingMode.UP);

    public static BigDecimal addPercentage(BigDecimal base, BigDecimal percentage) {
        return base.add(getPercentagePart(base, percentage),mathContext);
    }

    public static BigDecimal getPercentagePart(BigDecimal base, BigDecimal percentage) {
        return percentage.divide(new BigDecimal(100),mathContext).multiply(base,mathContext);
    }

    public static BigDecimal subtractPercentage(BigDecimal base, BigDecimal percentage) {
        return base.subtract(getPercentagePart(base, percentage),mathContext);
    }
}
