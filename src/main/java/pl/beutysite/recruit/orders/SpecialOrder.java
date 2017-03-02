package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static pl.beutysite.recruit.OrderFlag.*;
import static pl.beutysite.recruit.TaxCalculationsHelper.*;

public class SpecialOrder extends Order{

    public SpecialOrder(int itemId, int customerId, BigDecimal price, List<OrderFlag> orderFlags) {
        super(itemId, customerId, price, orderFlags);
    }

    /**
     *   Method getPrice needed for get price of order, that should for
     *   special combined order, by base price that situated in super class.'
     *   And also get price depend from enum type of OrderFlag that can be with
     *   some various composition: PRIORITY, DISCOUNTED, INTERNATIONAL
     *  @return BigDecimal value of price with include Percentage
     */
    @Override
    public BigDecimal getPrice() {

        BigDecimal priceDISCOUNTED = subtractPercentage(super.getPrice(), new BigDecimal("11"));
        BigDecimal pricePRIORITY = addPercentage(super.getPrice(), new BigDecimal("1.5"));
        BigDecimal priceINTERNATIONAL = super.getPrice();

        if(getOrderFlags().containsAll(Arrays.asList(OrderFlag.values())))
            return getMiddlePercentageAll(priceDISCOUNTED, pricePRIORITY, priceINTERNATIONAL);
        else if(getOrderFlags().contains(DISCOUNTED) && getOrderFlags().contains(PRIORITY))
            return getMiddlePercentage(priceDISCOUNTED, pricePRIORITY);
        else if(getOrderFlags().contains(DISCOUNTED) && getOrderFlags().contains(INTERNATIONAL))
            return getMiddlePercentage(priceDISCOUNTED, priceINTERNATIONAL);
        else if(getOrderFlags().contains(PRIORITY) && getOrderFlags().contains(INTERNATIONAL)) {
            return getMiddlePercentage(pricePRIORITY, priceINTERNATIONAL);
        }
        else if(getOrderFlags().contains(DISCOUNTED))
            return priceDISCOUNTED;
        else if(getOrderFlags().contains(PRIORITY))
            return pricePRIORITY;
        else if(getOrderFlags().contains(INTERNATIONAL))
            return priceINTERNATIONAL;
        else
            return super.getPrice();
    }

    /**
     *   Method getTax is override superClass and needed
     *   for calculate tax value of order, that should be
     *   international type with 11% of tax, by calculated price'
     *   if OrderFlag INTERNATIONAL value .
     *   Or if OrderFlag has another value, such as PRIORITY,'
     *   DISCOUNTED or STANDARD values and not contain INTERNATIONAL value
     *   the tax will be get from super class and calculating like
     *   standard tax - 23.5%
     *   @return BigDecimal value of tax with getPercentagePart
     *  discount
     */
    @Override
    public BigDecimal getTax() {
        if(getOrderFlags().contains(INTERNATIONAL))
            return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("15.0"));
        return super.getTax();
    }
}
